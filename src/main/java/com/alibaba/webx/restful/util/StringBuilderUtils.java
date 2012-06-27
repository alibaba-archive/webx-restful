package com.alibaba.webx.restful.util;

public class StringBuilderUtils {

    /**
     * Append a new value to the string builder. If the value contains non-token characters (e.g. control, white-space,
     * quotes, separators, etc.), the appended value is quoted and all the quotes in the value are escaped.
     * 
     * @param b string builder to be updated.
     * @param value value to be appended.
     */
    public static void appendQuotedIfNonToken(StringBuilder b, String value) {
        if (value == null) {
            return;
        }
        boolean quote = !GrammarUtil.isTokenString(value);
        if (quote) {
            b.append('"');
        }
        appendEscapingQuotes(b, value);
        if (quote) {
            b.append('"');
        }
    }

    /**
     * Append a new value to the string builder. If the value contains white-space characters, the appended value is
     * quoted and all the quotes in the value are escaped.
     * 
     * @param b string builder to be updated.
     * @param value value to be appended.
     */
    public static void appendQuotedIfWhitespace(StringBuilder b, String value) {
        if (value == null) {
            return;
        }
        boolean quote = GrammarUtil.containsWhiteSpace(value);
        if (quote) {
            b.append('"');
        }
        appendEscapingQuotes(b, value);
        if (quote) {
            b.append('"');
        }
    }

    /**
     * Append a new quoted value to the string builder. The appended value is quoted and all the quotes in the value are
     * escaped.
     * 
     * @param b string builder to be updated.
     * @param value value to be appended.
     */
    public static void appendQuoted(StringBuilder b, String value) {
        b.append('"');
        appendEscapingQuotes(b, value);
        b.append('"');
    }

    /**
     * Append a new value to the string builder. All the quotes in the value are escaped before appending.
     * 
     * @param b string builder to be updated.
     * @param value value to be appended.
     */
    public static void appendEscapingQuotes(StringBuilder b, String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '"') {
                b.append('\\');
            }
            b.append(c);
        }
    }

    /**
     * Prevents instantiation.
     */
    private StringBuilderUtils(){
    }
}
