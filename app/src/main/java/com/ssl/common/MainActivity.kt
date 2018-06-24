package com.ssl.common

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ssl.common.library.utils.DeviceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_text.setText("名称："+ DeviceUtil.getVersion(this))
    }
}
