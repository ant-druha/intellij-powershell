package com.intellij.plugin.powershell.lang.util;

import com.intellij.openapi.util.text.StringUtilRt;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.text.StringUtil.isPrintableUnicode;

public class PowerShellStringUtil {

  public static boolean parseExpandableString(@NotNull String chars, @NotNull StringBuilder outChars, @NotNull int[] sourceOffsets,
                                              @Nullable Character quoteToEscape/*, @NotNull char... endChars*/) {
    assert sourceOffsets.length == chars.length() + 1;
    if (/*chars.indexOf('\\') < 0 && */chars.indexOf('`') < 0 && chars.indexOf('"') < 0) {
      outChars.append(chars);
      for (int i = 0; i < sourceOffsets.length; i++) {
        sourceOffsets[i] = i;
      }
      return true;
    }
    int index = 0;
    final int outOffset = outChars.length();
    while (index < chars.length()) {
      final char escChar = chars.charAt(index++);
      sourceOffsets[outChars.length() - outOffset] = index - 1;
      sourceOffsets[outChars.length() + 1 - outOffset] = index;

      char nextChar;
      if (quoteToEscape != null && escChar == quoteToEscape) {
        if (index == chars.length()) return false;
        nextChar = chars.charAt(index++);
        if (nextChar == quoteToEscape) {
          outChars.append(nextChar);
        } else {
          outChars.append(escChar).append(nextChar);//-> invalid string
        }
      } else if (escChar == '`' /*|| escChar == '\\'*/) {
        if (index == chars.length()) return false;
        nextChar = chars.charAt(index++);
        switch (nextChar) {
          case '"':
            outChars.append('"');
            break;

          case 'b':
            outChars.append('\b');
            break;

          case 't':
            outChars.append('\t');
            break;

          case 'n':
            outChars.append('\n');
            break;

          case 'f':
            outChars.append('\f');
            break;

          case 'r':
            outChars.append('\r');
            break;

          case '\\':
            outChars.append('\\');
            break;

          case 'a':
            outChars.append(" ");//audio alert bell/beep
            break;

          case 'v':
            outChars.append(" ");//Vertical tab (use with printer output)
            break;

          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
            char startC = nextChar;
            int v = (int) nextChar - '0';
            if (index < chars.length()) {
              nextChar = chars.charAt(index++);
              if ('0' <= nextChar && nextChar <= '7') {
                v <<= 3;
                v += nextChar - '0';
                if (startC <= '3' && index < chars.length()) {
                  nextChar = chars.charAt(index++);
                  if ('0' <= nextChar && nextChar <= '7') {
                    v <<= 3;
                    v += nextChar - '0';
                  } else {
                    index--;
                  }
                }
              } else {
                index--;
              }
            }
            outChars.append((char) v);
            break;

          case 'u':
            // uuuuu1234 is valid too
            while (index != chars.length() && chars.charAt(index) == 'u') {
              index++;
            }
            if (index + 4 <= chars.length()) {
              try {
                int code = Integer.parseInt(chars.substring(index, index + 4), 16);
                //line separators are invalid here
                if (code == 0x000a || code == 0x000d) return false;
                nextChar = chars.charAt(index);
                if (nextChar == '+' || nextChar == '-') return false;
                outChars.append((char) code);
                index += 4;
              } catch (Exception e) {
                return false;
              }
            } else {
              return false;
            }
            break;

          default:
            outChars.append(nextChar);
        }

      } else {
        outChars.append(escChar);
        continue;
      }

      sourceOffsets[outChars.length() - outOffset] = index;
    }
    return true;
  }


  @NotNull
  public static StringBuilder escapeStringCharacters(@NotNull String str,
                                                     @Nullable String additionalChars,
                                                     boolean escapeUnicode,
                                                     boolean escapeBacktick,
                                                     @NotNull @NonNls StringBuilder buffer/*, char quoteChar, char quoteEscapeChar*/) {
    char prev = 0;
    final int length = str.length();
    for (int idx = 0; idx < length; idx++) {
      char ch = str.charAt(idx);
      switch (ch) {
        case '\b':
          buffer.append("`b");
          break;

        case '\t':
          buffer.append("`t");
          break;

        case '\n':
          buffer.append("`n");
          break;

        case '\f':
          buffer.append("`f");
          break;

        case '\r':
          buffer.append("`r");
          break;

        case 0:
          buffer.append("`0");//null
          break;

        default:
          escapeDefaultChars(additionalChars, escapeUnicode, escapeBacktick, buffer, prev, ch);
      }
      prev = ch;
    }
    return buffer;
  }


  @NotNull
  public static StringBuilder escapeHereStringCharacters(@NotNull String str,
                                                         @Nullable String additionalChars,
                                                         boolean escapeUnicode,
                                                         boolean escapeBacktick,
                                                         @NotNull @NonNls StringBuilder buffer/*, char quoteChar, char quoteEscapeChar*/) {
    char prev = 0;
    final int length = str.length();
    for (int idx = 0; idx < length; idx++) {
      char ch = str.charAt(idx);
      escapeDefaultChars(additionalChars, escapeUnicode, escapeBacktick, buffer, prev, ch);
      prev = ch;
    }
    return buffer;
  }

  private static void escapeDefaultChars(@Nullable String charsToEscape, boolean escapeUnicode, boolean escapeBacktick, @NonNls @NotNull StringBuilder buffer, char prev, char ch) {
    if (escapeBacktick && ch == '`' /*&& prev != '`'*/) {
      buffer.append("``");
    } else if (charsToEscape != null && charsToEscape.indexOf(ch) > -1 && (escapeBacktick || prev != '`')) {
      buffer.append("`").append(ch);
    } else if (escapeUnicode && !isPrintableUnicode(ch)) {
      CharSequence hexCode = StringUtilRt.toUpperCase(Integer.toHexString(ch));
      buffer.append("\\u");
      int paddingCount = 4 - hexCode.length();
      while (paddingCount-- > 0) {
        buffer.append(0);
      }
      buffer.append(hexCode);
    } else {
      buffer.append(ch);
    }
  }
}
