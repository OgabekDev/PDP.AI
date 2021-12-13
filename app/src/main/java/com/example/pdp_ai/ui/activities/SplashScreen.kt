package com.example.pdp_ai.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pdp_ai.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.json.JSONArray

class SplashScreen : AppCompatActivity() {

    val API_ENGLISH = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/EnglishDB"
    val API_UZBEK = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/UzbekDB"

    var uzbekDB: JSONArray? = null
    var englishDB: JSONArray? = null

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

    private fun connectUzbekAPI() {
        val jsonArrayUZBEK = JSONArray()
        val queueUZBEK = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest (
            Request.Method.GET, API_UZBEK, jsonArrayUZBEK, {
                 forUzbek(it)
            }, {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Server Connection Error")
                alertDialog.setMessage("Server bilan aloqa yo'q. Iltimos internetni tekshiring va dasturni qayta ishga tushuring")
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
        )
        queueUZBEK.add(jsonArrayRequest)
    }

    private fun forUzbek(DB: JSONArray) {
        uzbekDB = DB
    }

    private fun connectEnglishAPI() {
        val jsonArrayENGLISH = JSONArray()
        val queueENGLISH = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest (
            Request.Method.GET, API_ENGLISH, jsonArrayENGLISH, {
                forEnglish(it)
            }, {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Server Connection Error")
                alertDialog.setMessage("Server bilan aloqa yo'q. Iltimos internetni tekshiring va dasturni qayta ishga tushuring")
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
        )
        queueENGLISH.add(jsonArrayRequest)
    }

    private fun forEnglish(DB: JSONArray) {
        englishDB = DB
    }

}


