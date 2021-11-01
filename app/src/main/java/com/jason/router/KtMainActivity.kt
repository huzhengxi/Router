package com.jason.router

import android.app.Activity
import com.jason.router.annotation.Destination

@Destination(url = "router://test", description = "登录主页面")
class KtMainActivity : Activity() {
}