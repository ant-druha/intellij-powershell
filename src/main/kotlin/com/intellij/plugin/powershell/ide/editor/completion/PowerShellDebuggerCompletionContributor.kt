package com.intellij.plugin.powershell.ide.editor.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.progress.runBlockingCancellable
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.findDocument
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns.or
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess
import com.intellij.plugin.powershell.ide.debugger.documentSessionKey
import com.intellij.plugin.powershell.ide.resolve.*
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.lang.lsp.ide.EditorEventManager
import com.intellij.plugin.powershell.lang.lsp.psi.LSPWrapperPsiElementImpl
import com.intellij.plugin.powershell.lang.lsp.util.DocumentUtils.offsetToLSPPos
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.types.PowerShellClassType
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.utils.vfs.getDocument
import com.intellij.util.ProcessingContext
import com.intellij.xdebugger.XDebugSession
import kotlinx.coroutines.runBlocking
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.eclipse.lsp4j.TextDocumentIdentifier

private val IS_BRACED_VARIABLE_CONTEXT: Key<Boolean> = Key.create("IS_BRACED_VARIABLE_CONTEXT")
private val IS_VARIABLE_CONTEXT: Key<Boolean> = Key.create("IS_VARIABLE_CONTEXT")
private val PUT_VARIABLE_CONTEXT: PatternCondition<PsiElement> =
  object : PatternCondition<PsiElement>("In variable") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
      context.put(IS_VARIABLE_CONTEXT, true)
      context.put(IS_BRACED_VARIABLE_CONTEXT, t.node.elementType === PowerShellTypes.BRACED_ID)
      return true
    }
  }
private val TYPE_REFERENCE: ElementPattern<PsiElement> =
  psiElement().withParent(PowerShellReferenceTypeElement::class.java)
private val VARIABLE: ElementPattern<PsiElement> = psiElement().withParent(PowerShellIdentifier::class.java)
  .withSuperParent(2, PowerShellVariable::class.java).with(PUT_VARIABLE_CONTEXT)

class PowerShellDebuggerCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
    val debugSession = parameters.editor.document.getUserData(documentSessionKey) ?: return
    val debugProcess = debugSession.debugProcess as PowerShellDebugProcess

    @Suppress("UnstableApiUsage") val gotResultFromHost = runBlockingCancellable {
      getCompletionFromLanguageHost(parameters, result, debugSession)
    }
    if (gotResultFromHost) return
    super.fillCompletionVariants(parameters, result)
  }

  private suspend fun getCompletionFromLanguageHost(parameters: CompletionParameters, result: CompletionResultSet, xDebugSession: XDebugSession): Boolean {
    val position = parameters.position
    val offset = parameters.offset
    val editor = parameters.editor
    val manager = EditorEventManager.forEditor(editor)
    if (manager != null) {
      val serverPos = offsetToLSPPos(editor, offset)
      val toAdd2 = manager.completion(serverPos)
      val psiFile = position.containingFile
        ?: PsiDocumentManager.getInstance(position.project).getPsiFile(editor.document)
      if (toAdd2.items.isNotEmpty()) {
        val newResult = adjustPrefixMatcher(result)
        for (item in toAdd2.items) {
          newResult.addElement(createLookupItem(item, position, psiFile ?: position))
        }
        if (cmdNameCondition.accepts(position, null)) {
          PowerShellTokenTypeSets.KEYWORDS.types.forEach { keyword -> newResult.addElement(buildKeyword(keyword)) }
        } else if (typeBody.accepts(position)) {
          addTypeBodyKeywords(newResult)
        }
        return true
      }
    }
    return false
  }

  private fun adjustPrefixMatcher(result: CompletionResultSet): CompletionResultSet {
    var prefix = result.prefixMatcher.prefix
    val lastDotIndex = prefix.indexOfLast { c -> c == '.' }
    if (lastDotIndex > 0 && lastDotIndex + 1 <= prefix.length) {
      prefix = prefix.drop(lastDotIndex + 1)
    }
    return result.withPrefixMatcher(CamelHumpMatcher(prefix, false))
  }

  private val cmdNameCondition = object : PatternCondition<PsiElement>("Command Name Condition") {
    override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
      return PsiTreeUtil.getParentOfType(element, PowerShellCommandName::class.java, true, PowerShellBlockBody::class.java) != null
    }
  }

  private val commandName: ElementPattern<PsiElement> = psiElement().withParent(PowerShellIdentifier::class.java)
    .withSuperParent(2, PowerShellCommandName::class.java)
  private val memberAccess: ElementPattern<PsiElement> = psiElement().withParent(PowerShellReferenceIdentifier::class.java)
    .withSuperParent(2, or(
      psiElement(PowerShellMemberAccessExpression::class.java),
      psiElement(PowerShellInvocationExpression::class.java)
    )
    ).with(PUT_VARIABLE_CONTEXT)
  private val typeBody: ElementPattern<PsiElement> = psiElement().withParent(PowerShellBlockBody::class.java)
    .withSuperParent(2, PowerShellTypeDeclaration::class.java)

  init {
    extend(CompletionType.BASIC, commandName,
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
          val ref = PsiTreeUtil.getContextOfType(parameters.position.context, PowerShellReferencePsiElement::class.java)
            ?: return
          val elements = ResolveCache.getInstance(parameters.originalFile.project)
            .resolveWithCaching(ref, PowerShellComponentResolveProcessor.INSTANCE, true, true) ?: emptyList()

          elements.forEach { e ->
            result.addElement(buildLookupElement(e, context))
            if (e is PowerShellVariable) {
              addFunctionCall(result, e)
            }
          }
          PowerShellTokenTypeSets.KEYWORDS.types.forEach { keyword -> result.addElement(buildKeyword(keyword)) }
        }
      })

    extend(CompletionType.BASIC, VARIABLE,
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
          val ref = PsiTreeUtil.getContextOfType(parameters.position.context, PowerShellReferencePsiElement::class.java)
            ?: return
          val elements = ResolveCache.getInstance(parameters.originalFile.project)
            .resolveWithCaching(ref, PowerShellComponentResolveProcessor.INSTANCE, true, true) ?: emptyList()

          elements.filterIsInstance<PowerShellVariable>().forEach { result.addElement(buildLookupElement(it, context)) }
          result.addElement(buildKeyword(PowerShellTypes.THIS))
        }
      })

    extend(CompletionType.BASIC, memberAccess,
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
          val expr = PsiTreeUtil.getContextOfType(parameters.position.context, PowerShellMemberAccessExpression::class.java)
            ?: return
          val qType = expr.qualifier?.getType()
          if (qType != null && qType != PowerShellType.UNKNOWN) {
            val membersProcessor = PowerShellMemberScopeProcessor()
            PowerShellResolveUtil.processMembersForType(qType, membersProcessor)
            val res = membersProcessor.getResult()
            res.map { it.element }.filter { it !is PowerShellConstructorDeclarationStatement }.forEach { result.addElement(buildLookupElement(it, context)) }
            if (expr.isTypeMemberAccess()) {
              addCallNew(result, qType)
            }
          }
        }
      })

    extend(CompletionType.BASIC, TYPE_REFERENCE,
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
          val ref = PsiTreeUtil.getContextOfType(parameters.position, PowerShellReferenceTypeElement::class.java)
            ?: return
          val resolveProcessor = PowerShellClassScopeProcessor()
          PsiTreeUtil.treeWalkUp(resolveProcessor, ref, null, ResolveState.initial())
          resolveProcessor.getResult().map { it.element }.forEach { result.addElement(buildLookupElement(it, context)) }
        }
      })

    extend(CompletionType.BASIC, typeBody,
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
          addTypeBodyKeywords(result)
        }
      })
  }

  private fun addTypeBodyKeywords(result: CompletionResultSet) {
    result.addElement(buildKeyword("$${PowerShellTypes.THIS}"))
    result.addElement(buildKeyword(PowerShellTypes.STATIC))
    result.addElement(buildKeyword(PowerShellTypes.HIDDEN))
  }

  private fun addFunctionCall(result: CompletionResultSet, variable: PowerShellVariable) {
    if (PsNames.NAMESPACE_FUNCTION.equals(variable.getScopeName(), true)) {
      result.addElement(buildCall(variable.name ?: variable.text))
    }
  }

  private fun buildCall(name: String) = LookupElementBuilder.create(name).withIcon(PowerShellComponentType.FUNCTION.getIcon())

  private fun addCallNew(result: CompletionResultSet, qualifierType: PowerShellType) {
    if (qualifierType !is PowerShellClassType) return
    val resolved = qualifierType.resolve() as? PowerShellClassDeclaration ?: return
    var e = LookupElementBuilder.create("new").withIcon(PowerShellComponentType.METHOD.getIcon())
    e = if (PowerShellResolveUtil.hasDefaultConstructor(resolved)) {
      e.withInsertHandler(ParenthesesInsertHandler.NO_PARAMETERS)
    } else {
      e.withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
    }
    result.addElement(e)
  }

  private fun buildKeyword(kw: IElementType): LookupElement {
    return buildKeyword(kw.toString())
  }

  private fun buildKeyword(kw: String): LookupElement {
    return LookupElementBuilder.create(kw.lowercase()).bold()
  }

  private fun buildLookupElement(e: PsiElement, context: ProcessingContext): LookupElement {
    val icon = e.getIcon(0)
    return when (e) {
      is PowerShellVariable -> {
        val lookupString = getVariableLookupString(context, e)
        LookupElementBuilder.create(e, lookupString).withIcon(icon).withPresentableText(lookupString)
      }
      is PowerShellCallableDeclaration -> LookupElementBuilder.create(e).withIcon(icon).withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
      is PowerShellComponent -> LookupElementBuilder.create(e).withIcon(icon)
      else -> LookupElementBuilder.create(e)
    }
  }

  private fun getVariableLookupString(context: ProcessingContext, e: PowerShellVariable): String {
    return if (context.get(IS_VARIABLE_CONTEXT) == true) {
      if (e.isBracedVariable() && context.get(IS_BRACED_VARIABLE_CONTEXT) == false) {
        (e.presentation.presentableText ?: e.text).trimStart('$')
      } else
        e.name ?: e.text
    } else {
      e.presentation.presentableText ?: e.text
    }
  }


  private fun createLookupItem(item: CompletionItem, position: PsiElement, parent: PsiElement): LookupElement {
    val insertText = item.insertText
    val kind = item.kind
    val label = item.label
    val presentableText = if (StringUtil.isNotEmpty(label)) label else insertText ?: ""
    val tailText = item.detail ?: ""
    val isCompletingType = TYPE_REFERENCE.accepts(position)
    val lspElement = LSPWrapperPsiElementImpl(item, parent)
    if (isCompletingType) {
      lspElement.setType(PowerShellComponentType.CLASS)
    }
    var builder: LookupElementBuilder = LookupElementBuilder.create(lspElement as PsiNamedElement).withIcon(lspElement.presentation.getIcon(false))
    if (lspElement.getType() == PowerShellComponentType.METHOD) {
      builder = builder.withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
    }
    if (kind == CompletionItemKind.Keyword) builder = builder.withBoldness(true)
    return builder.withPresentableText(presentableText).appendTailText("    $tailText", true).withAutoCompletionPolicy(AutoCompletionPolicy.SETTINGS_DEPENDENT)
  }
}
