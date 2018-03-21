package com.mainli.d.d2018.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import android.widget.TextView
import com.mainli.d.d2018.R

/**
 * Created by mobimagic on 2018/3/2.
 */
class MessageDialog(val cont: Context, val message: String) : Dialog(cont, R.style.AppDialog), DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    override fun onDismiss(dialog: DialogInterface?) {
//        context.startActivity(Intent(context, DiceActivity::class.java))
    }

    override fun onCancel(dialog: DialogInterface?) {
//        context.startActivity(Intent(context, DiceActivity::class.java))
        if (cont is Activity) {
            cont.finish()
        }
    }

    init {
        val textView = TextView(context)
        textView.setTextColor(0xffc7edcc.toInt())
        textView.text = message
        textView.setBackgroundColor(0xff333333.toInt())
        setContentView(textView, ViewGroup.LayoutParams(-1, -1))
        setOnCancelListener(this)
        setOnDismissListener(this)
    }
}