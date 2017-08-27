import com.intellij.lexer.Lexer;
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.testFramework.LexerTestCase;

/**
 * Andrey 16/08/17.
 */
public class PowerShellLexerTest extends LexerTestCase {
  @Override
  protected Lexer createLexer() {
    return new PowerShellLexerAdapter();
  }

  @Override
  protected String getDirPath() {
    return null;
  }


  public void testLineComment() {
    doTest("hello#world", "SIMPLE_ID ('hello#world')");
    doTest("hello #world", "SIMPLE_ID ('hello')\n" + "WHITE_SPACE (' ')\n" + "COMMENT ('#world')");
  }

  public void testWhiteSpace() {
    doTest(" ", "WHITE_SPACE (' ')"); doTest("`\n", "WHITE_SPACE ('`\\n')");
  }

  public void testSimpleVariable() {
    doTest("$values[2]", "$ ('$')\n" + "SIMPLE_ID ('values')\n" + "[ ('[')\n" + "DIGITS ('2')\n" + "] (']')");
  }
}
