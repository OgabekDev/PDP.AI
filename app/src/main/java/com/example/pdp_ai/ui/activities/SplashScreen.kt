package com.example.pdp_ai.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.pdp_ai.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val top = AnimationUtils.loadAnimation(this, R.anim.top)
        val bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)
        val splashTime = 2600
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            imageView.startAnimation(bottom)
            textView.startAnimation(top)
            Handler().postDelayed({
                startActivity(Intent(this, ChatScreen::class.java))
                finish()
            }, splashTime.toLong())
        } else {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Internet Connection Error")
            alertDialog.setMessage("Server bilan aloqa yo'q. Iltimos internetni tekshiring va dasturni qayta ishga tushuring")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") {
                    _: DialogInterface, _: Int -> finish()
            }
            alertDialog.show()
        }
    }
}


