package com.intellij.plugin.powershell.ide.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

class PowerShellFoldingBuilder : CustomFoldingBuilder(), DumbAware {
    override fun isRegionCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }

    override fun buildLanguageFoldRegions(descriptors: MutableList<FoldingDescriptor>, root: PsiElement, document: Document, quick: Boolean) {
        collect(root, descriptors, HashSet())
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        return when (node.elementType) {
            SCRIPT_BLOCK_EXPRESSION -> "{...}"
            ARRAY_EXPRESSION -> "@(...)"
            BLOCK_BODY -> "..."
            else -> "..."
        }
    }

    private fun collect(element: PsiElement, descriptors: MutableList<FoldingDescriptor>, usedComments: MutableSet<PsiElement>) {
        val node = element.node
        when (node.elementType) {
            BLOCK_BODY, SCRIPT_BLOCK_EXPRESSION, ARRAY_EXPRESSION -> {
                if (isSpanMultipleLines(node)) descriptors.add(FoldingDescriptor(node, node.textRange))
            }
            COMMENT -> {
                if (usedComments.add(element)) {
                    if (!isCustomRegionElement(element)) {
                        var end: PsiElement? = null
                        var current: PsiElement? = element.getNextSiblingNonWhiteSpace()
                        while (current != null) {
                            if (COMMENT == current.node.elementType) {
                                if (isCustomRegionElement(current)) {
                                    // Stop current folding
                                    usedComments.add(current)
                                    break
                                }
                                end = current
                                usedComments.add(current)
                                current = current.getNextSiblingNonWhiteSpace()
                                continue
                            }
                            break
                        }
                        if (end != null) {
                            val range = TextRange(element.textRange.startOffset, end.textRange.endOffset)
                            descriptors.add(FoldingDescriptor(element, range))
                        }
                    }
                }
            }
        }

        val childUsedCommends = HashSet<PsiElement>()
        for (c in element.children) {
            collect(c, descriptors, childUsedCommends)
        }
    }

    private fun isSpanMultipleLines(node: ASTNode): Boolean {
        return node.textContains('\n') || node.textContains('\r')
    }

}

fun PsiElement.getNextSiblingNonWhiteSpace(): PsiElement? {
    var next = this.nextSibling
    while (next != null && (next is PsiWhiteSpace || next.node.elementType == NLS)) {
        next = next.nextSibling
    }
    return next
}
