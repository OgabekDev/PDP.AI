package com.example.pdp_ai.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pdp_ai.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.json.JSONArray

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val top = AnimationUtils.loadAnimation(this, R.anim.top)
        val bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)
        val splashTime = 2600
        if (isConnected()) {
            imageView.startAnimation(bottom)
            textView.startAnimation(top)
            Handler().postDelayed({
                startActivity(Intent(this, ChatScreen::class.java))
                finish()
            }, splashTime.toLong())
        } else {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Internet Connection Error")
            alertDialog.setMessage("Internet bilan aloqa yo'q. Iltimos internetni tekshiring va dasturni qayta ishga tushuring")
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "OK"
            ) { _: DialogInterface, _: Int ->
                finish()
            }
            alertDialog.setOnDismissListener{
                finish()
            }
            alertDialog.show()
        }
    }

    fun isConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}


