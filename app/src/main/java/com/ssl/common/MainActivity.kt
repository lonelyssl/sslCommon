package com.ssl.common

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ssl.common.library.utils.SSLDeviceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_text.setText("名称："+SSLDeviceUtil.getVersion(this))
    }
}
