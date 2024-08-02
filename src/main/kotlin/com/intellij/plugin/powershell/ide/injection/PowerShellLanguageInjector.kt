package com.intellij.plugin.powershell.ide.injection

import com.intellij.lang.ASTNode
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import org.intellij.plugins.intelliLang.inject.InjectedLanguage
import org.intellij.plugins.intelliLang.inject.InjectorUtils
import org.intellij.plugins.intelliLang.inject.InjectorUtils.InjectionInfo
import org.intellij.plugins.intelliLang.inject.TemporaryPlacesRegistry
import org.intellij.plugins.intelliLang.inject.config.BaseInjection

class PowerShellLanguageInjector : MultiHostInjector {

  private val injectionSupport: PowerShellLanguageInjectionSupport? by lazy {
    ArrayList(InjectorUtils.getActiveInjectionSupports()).filterIsInstance<PowerShellLanguageInjectionSupport>().firstOrNull()
  }

  override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
    return mutableListOf(PowerShellStringLiteralExpression::class.java)
  }

  override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
    if (context !is PowerShellStringLiteralExpression) return
    val project = context.project
    val file = context.containingFile

    val temporaryPlacesRegistry = TemporaryPlacesRegistry.getInstance(project)
    val tempInjectedLanguage = temporaryPlacesRegistry.getLanguageFor(context, file) ?: return

    val baseInjection = BaseInjection("PowerShell").apply {
      injectedLanguageId = tempInjectedLanguage.id
      prefix = tempInjectedLanguage.prefix
      suffix = tempInjectedLanguage.suffix
    }
    val support = injectionSupport ?: return
    if (context.hasSubstitutions()) {
      val parts = splitLiteralToInjectionParts(baseInjection, context)
      val language = InjectorUtils.getLanguageByString(baseInjection.injectedLanguageId) ?: return
      InjectorUtils.registerInjection(language, file, parts.ranges, registrar)
      InjectorUtils.registerSupport(support, false, context, language)
      InjectorUtils.putInjectedFileUserData(context, language, InjectedLanguageManager.FRANKENSTEIN_INJECTION,
                                            if (parts.isUnparsable) java.lang.Boolean.TRUE else null)
    } else {
      InjectorUtils.registerInjectionSimple(context, baseInjection, support, registrar)
    }
  }

  private fun splitLiteralToInjectionParts(baseInjection: BaseInjection, context: PowerShellStringLiteralExpression): PowerShellInjectionParts {
    val ranges = mutableListOf<InjectionInfo>()
    if (!context.isExpandable()) {
      ranges.add(createInjectionRange(context, baseInjection, context.textRange, baseInjection.prefix, baseInjection.suffix))
      return PowerShellInjectionParts(ranges, false)
    }
    val stringContentSet = TokenSet.create(PowerShellTypes.EXPANDABLE_STRING_PART, PowerShellTypes.EXPANDABLE_HERE_STRING_PART)
    val expressionContentSet = TokenSet.create(PowerShellTypes.SUB_EXPRESSION, PowerShellTypes.TARGET_VARIABLE_EXPRESSION)
    val childNodes = context.node.getChildren(TokenSet.orSet(stringContentSet, expressionContentSet)).toList()
    var unparsable = false
    var prefix = baseInjection.prefix
    val lastChild = childNodes.lastOrNull()
    var i = 0
    val len = childNodes.size
    while (i < len) {
      val c = childNodes[i]
      val partOffsetInParent = c.startOffset - c.treeParent.startOffset
      val part: Any = when {
        expressionContentSet.contains(c.elementType) -> {
          unparsable = true
          MISSING_VALUE
        }
        else -> {
          val partSize = childNodes.subList(i, len).asSequence().takeWhile { stringContentSet.contains(it.elementType) }.count()
          i += partSize - 1
          childNodes[i]
        }
      }
      val suffix = if (i == len - 1) baseInjection.suffix else ""
      if (part is ASTNode) {
        ranges.add(createInjectionRange(context, baseInjection, TextRange.create(partOffsetInParent, (part.startOffset - part.treeParent.startOffset) + part.textLength), prefix, suffix))
      } else if (!prefix.isEmpty() || i == 0) {
        ranges.add(createInjectionRange(context, baseInjection, TextRange.from(partOffsetInParent, 0), prefix, suffix))
      }
      prefix = part as? String ?: ""
      i++
    }

    if (lastChild != null && !prefix.isEmpty()) {
      // Last element was interpolated part, need to add a range after it
      createInjectionRange(context, baseInjection, TextRange.from((lastChild.startOffset - lastChild.treeParent.startOffset) + lastChild.textLength, 0), prefix, baseInjection.suffix)
    }

    return PowerShellInjectionParts(ranges, unparsable)
  }


  private fun createInjectionRange(literal: PowerShellStringLiteralExpression, injection: BaseInjection, range: TextRange, prefix: String, suffix: String): InjectionInfo {
    TextRange.assertProperRange(range, injection)
    val injectedLanguage = InjectedLanguage.create(injection.injectedLanguageId, prefix, suffix, true)!!
    return InjectionInfo(literal, injectedLanguage, range)
  }

  class PowerShellInjectionParts(val ranges: MutableList<InjectionInfo>, val isUnparsable: Boolean = false)
}

private const val MISSING_VALUE = "missingValue"
