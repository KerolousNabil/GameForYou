package com.example.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*
import View.LoginScreen

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.statusBarColor = this.resources.getColor(R.color.backgrounds)
        window.navigationBarColor = this.resources.getColor(R.color.backgrounds)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        imageView.alpha = 0f
        imageView.animate().setDuration(3000).alpha(1f).withEndAction{
            val i = Intent(this,LoginScreen::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()

        }
    }
}