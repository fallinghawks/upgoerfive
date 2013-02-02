package com.fallinghawks.upgoer;

import android.widget.MultiAutoCompleteTextView;

public class MyTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && Character.isLetter(text.charAt(i - 1))) {
            i--;
        }
        return i;
    }

    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (!Character.isLetter(text.charAt(i))) {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }

    @Override
    public CharSequence terminateToken(CharSequence text) {
//        int i = text.length();
//
//        while (i > 0 && text.charAt(i - 1) == ' ') {
//            i--;
//        }
//
//        if (i > 0 && text.charAt(i - 1) == ',') {
//            return text;
//        } else {
//            if (text instanceof Spanned) {
//                SpannableString sp = new SpannableString(text + ", ");
//                TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
//                        Object.class, sp, 0);
//                return sp;
//            } else {
//                return text + ", ";
//            }
//        }
        return  text+" ";
    }
}
