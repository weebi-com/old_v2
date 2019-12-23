/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weebinatidi.ui;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

public class CalculatorExpressionBuilder extends SpannableStringBuilder {

    private final CalculatorExpressionTokenizer mTokenizer;
    private boolean mIsEdited;

    public CalculatorExpressionBuilder(
            CharSequence text, CalculatorExpressionTokenizer tokenizer, boolean isEdited) {
        super(text);

        mTokenizer = tokenizer;
        mIsEdited = isEdited;
    }

    @Override
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart,
                                          int tbend) {
        if (start != length() || end != length()) {
            mIsEdited = true;
            return super.replace(start, end, tb, tbstart, tbend);
        }

        Log.d("CalcExprBuilder", "Raw tb : " + tb);
        Log.d("CalcExprBuilder", "Raw tb length = " + tb.length());
        String appendExpr = mTokenizer.getNormalizedExpression(tb.subSequence(tbstart, tbend).toString());
        Log.d("CalcExprBuilder", "append expression : " + appendExpr);
        int addOpCount = 0;
        int otherOpCount = 0;
        if (appendExpr.length() == 1) {
            final String expr = mTokenizer.getNormalizedExpression(toString());
            Log.d("CalcExprBuilder", "Tokenized expression = " + expr);
            start = expr.length();
            switch (appendExpr.charAt(0)) {
                case '.':
                    // don't allow two decimals in the same number
                    final int index = expr.lastIndexOf('.');
                    if (index != -1 && TextUtils.isDigitsOnly(expr.substring(index + 1, start))) {
                        appendExpr = "";
                    }
                    break;
                case '+':
                case '*':
                case '/':
                    // don't allow leading operator
                    if (start == 0) {
                        appendExpr = "";
                        break;
                    }

                    // don't allow multiple successive operators
                    while (start > 0 && "+-*/".indexOf(expr.charAt(start - 1)) != -1) {
                        if ("+".indexOf(expr.charAt(start - 1)) != -1)
                            addOpCount++;
                        else
                            otherOpCount++;
                        --start;
                    }
                    // fall through
                case '-':
                    // don't allow -- or +-
                    if (start > 0 && "+-".indexOf(expr.charAt(start - 1)) != -1) {
                        if ("+".indexOf(expr.charAt(start - 1)) != -1)
                            addOpCount++;
                        else
                            otherOpCount++;
                        --start;
                    }

                    // mark as edited since operators can always be appended
                    mIsEdited = true;
                    break;
                default:
                    break;
            }
        }

        start = toString().length();
        Log.d("CalcExprBuilder", "Add operation count " + addOpCount);
        start = start - (addOpCount * CalculatorExpressionTokenizer.ADD_ESC.length()); // remove exceding + operation
        Log.d("CalcExprBuilder", "Other operation count " + otherOpCount);
        start = start - otherOpCount; // remove other exceding operation

        // since this is the first edit replace the entire string
        if (!mIsEdited && appendExpr.length() > 0) {
            start = 0;
            mIsEdited = true;
        }

        appendExpr = mTokenizer.getLocalizedExpression(appendExpr);
        return super.replace(start, end, appendExpr, 0, appendExpr.length());
    }
}
