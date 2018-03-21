package com.mainli.d.d2018

import android.app.Activity
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.mainli.d.d2018.utils.ClassUtils

class MainActivity : ListActivity(), View.OnClickListener {
    lateinit var list: List<Class<out Activity>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = ClassUtils.getActivitiesClass(this, packageName, listOf(MainActivity::class.java))
        listAdapter = object : BaseAdapter() {
            override fun getItemId(position: Int): Long {
                return position.toLong();
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val button = Button(this@MainActivity)
                button.setAllCaps(false)
                button.setText("${list[position].simpleName}")
                button.setTag(position)
                button.setOnClickListener(this@MainActivity)
                return button
            }

            override fun getItem(position: Int): Any {
                return list[position]
            }

            override fun getCount(): Int {
                return list.size
            }
        }

    }

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun onClick(v: View) {
        val tag = v.getTag()
        if (tag is Int && tag > -1) {
            val clazz = list[tag]
            startActivity(Intent(this, clazz))
        }
    }

//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    external fun stringFromJNI(): String
//
//    companion object {
//
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }
}
