package com.mainli

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.mainli.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by GreendaMi on 2018/5/3.
 */

class CrashHandler : Thread.UncaughtExceptionHandler {

    lateinit var mDefaultHandler: Thread.UncaughtExceptionHandler
    lateinit var mContext: Context
    // 保存手机信息和异常信息
    private lateinit var mPrefixInfo: String;

    //未捕获异常
    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        if (!handleException(ex)) {
            // 未经过人为处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex)
            }
        } else {
            // 已经人为处理,系统自己退出
            try {
                Thread.sleep(1000)
            } catch (e1: InterruptedException) {
                e1.printStackTrace()
            }
            //重启
            var intent = mContext?.packageManager?.getLaunchIntentForPackage(mContext?.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mContext?.startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    /**
     * 初始化默认异常捕获
     *
     * @param context context
     */
    fun init(context: Context) {
        mContext = context
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        collectErrorMessages()
    }

    /**
     * 是否人为捕获异常
     *
     * @param e Throwable
     * @return true:已处理 false:未处理
     */
    private fun handleException(e: Throwable?): Boolean {
        if (e == null) {// 异常是否为空
            return false
        }
//        object : Thread() {
//            // 在主线程中弹出提示
//            override fun run() {
//                Looper.prepare()
//                Toast.makeText(mContext, "程序发生未知异常，将重启。", Toast.LENGTH_SHORT).show()
//                Looper.loop()
//            }
//        }.start()
        saveErrorMessages(e)
        return false
    }

    /**
     * 手机手机信息
     */
    private fun collectErrorMessages() {
        mPrefixInfo = StringBuilder()
                .append("versionName: ").append(BuildConfig.VERSION_NAME).append('\n')
                .append("versionCode: ").append(BuildConfig.VERSION_CODE).append('\n')
                .append("手机品牌: ").append(android.os.Build.BRAND).append('\n')
                .append("手机型号: ").append(android.os.Build.MODEL).append('\n')
                .append("CPU: ").append(Arrays.toString(android.os.Build.SUPPORTED_ABIS)).append('\n')
                .toString()
//        try {
//            // 通过反射拿到错误信息
//            val fields = Build::class.java!!.fields
//            if (fields != null && fields.isNotEmpty()) {
//                for (field in fields!!) {
//                    field.isAccessible = true
//                    try {
//                        mMessage[field.name] = field.get(null).toString()
//                    } catch (e: IllegalAccessException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
    }

    private fun saveErrorMessages(e: Throwable) {
        val sb = StringBuilder()
        sb.append(mPrefixInfo)
        sb.append(getStackTrace(e))
        // 有无SD卡
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val dir = File(Environment.getExternalStorageDirectory().absolutePath, "Mainli-log")
            if (!dir.exists()) dir.mkdirs()
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(File(dir, createLogFileName()))
                fos!!.write(sb.toString().toByteArray())
            } catch (e1: Exception) {
                e1.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos!!.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
            }
        }
    }

    val timeFirmat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    private fun createLogFileName(): String {
        return "crash-${timeFirmat.format(Date())}.log"
    }

    /**
     * 获取堆栈信息
     */
    private fun getStackTrace(e: Throwable): String {
        val writer = StringWriter()
        val pw = PrintWriter(writer)
        e.printStackTrace(pw)
        var cause: Throwable? = e.cause
        // 循环取出Cause
        if (cause != null) {
            cause.printStackTrace(pw)
        }
        pw.close()
        val result = writer.toString()
        return result
    }
}