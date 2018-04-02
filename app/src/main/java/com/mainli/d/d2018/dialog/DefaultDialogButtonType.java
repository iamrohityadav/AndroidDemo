package com.mainli.d.d2018.dialog;

import android.support.annotation.IntDef;

@IntDef({DefaultDialogButtonType.FIRST_BUTTON, DefaultDialogButtonType.SECOND_BUTTON, DefaultDialogButtonType.THIRD_BUTTON})
    public @interface DefaultDialogButtonType {
        int FIRST_BUTTON = 0;
        int SECOND_BUTTON = 1;
        int THIRD_BUTTON = 2;
    }