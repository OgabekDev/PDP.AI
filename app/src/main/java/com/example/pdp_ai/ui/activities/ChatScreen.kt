package com.example.pdp_ai.ui.activities

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pdp_ai.R
import com.example.pdp_ai.ui.data.MyLifeCycle
import com.example.pdp_ai.ui.data.Message
import com.example.pdp_ai.ui.utils.BotResponse
import com.example.pdp_ai.ui.utils.Constants
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class ChatScreen : AppCompatActivity(), TextToSpeech.OnInitListener {
    private val REQ_CODE = 100
    lateinit var textToSpeech: TextToSpeech
    private val uri = "https://maps.app.goo.gl/G5y2oFnRuAxswXRF9"
    private lateinit var myLifeCycle: MyLifeCycle

    var messagesList = mutableListOf<Message>()

    val API_ENGLISH = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/EnglishDB"
    val API_UZBEK = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/UzbekDB"

    lateinit var uzbekDB: JSONArray
    lateinit var englishDB: JSONArray

    var isSpoken = false

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Farida", "Mohichehra", "Nargiza")

    var language = "Uzbek"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        textToSpeech = TextToSpeech(this, this)
        recyclerView()
        clickEvents()
        changeLanguage("uz")
        myLifeCycle = MyLifeCycle(this, lifecycle)
        lifecycle.addObserver(myLifeCycle)

        connectEnglishAPI()
        connectUzbekAPI()

        flagBtn.setOnClickListener {
            textToSpeechFun("")
            if (language == "Uzbek") {
                flagBtn.setImageResource(R.drawable.usa)
                language = "English"
                if (et_message.text.isNotEmpty()) {
                    btnVoice.visibility = View.GONE
                    btnSend.visibility = View.VISIBLE
                } else {
                    btnVoice.visibility = View.VISIBLE
                    btnSend.visibility = View.GONE
                }
                changeLanguage("en")
            } else {
                flagBtn.setImageResource(R.drawable.uzb)
                language = "Uzbek"
                btnSend.visibility = View.VISIBLE
                btnVoice.visibility = View.GONE
                changeLanguage("uz")
            }
        }

        val random = (0..3).random()
        customBotMessage(
            "${getString(R.string.first_sms1)} ${botList[random]} ${getString(R.string.first_sms2)}"
        )

        if (language == "Uzbek") {
            btnVoice.visibility = View.GONE
            btnSend.visibility = View.VISIBLE
        } else {
            btnSend.visibility = View.GONE
            btnVoice.visibility = View.VISIBLE
        }
        et_message.addTextChangedListener {
            textToSpeechFun("")
            if (language == "Uzbek") {
                btnVoice.visibility = View.GONE
                btnSend.visibility = View.VISIBLE
            } else {
                if (et_message.text.isNotEmpty()) {
                    btnVoice.visibility = View.GONE
                    btnSend.visibility = View.VISIBLE
                } else {
                    btnVoice.visibility = View.VISIBLE
                    btnSend.visibility = View.GONE
                }
            }
        }

        recycleView.setOnClickListener {
            textToSpeechFun("")
        }
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
        if (DB == null) {
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
        } else {
            uzbekDB = DB
        }
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
        if (DB == null) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Server Connection Error")
            alertDialog.setMessage("Server bilan aloqa yo'q. IlAtimos internetni tekshiring va dasturni qayta ishga tushuring")
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
        } else {
            englishDB = DB
        }
    }

    private fun clickEvents() {
        btnSend.setOnClickListener {
            isSpoken = false
            sendMessage()
        }
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    recycleView.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                recycleView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()

        if (message.isNotEmpty()) {
            messagesList.add(Message(message, Constants.SEND_ID))
            et_message.setText("")

            adapter.insertMessage(Message(message, Constants.SEND_ID))
            recycleView.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {

        GlobalScope.launch {
            online.text = getText(R.string.type)
            delay(1800)

            withContext(Dispatchers.Main) {

                val response = if (language == "Uzbek") {
                    Log.e("DataBase", uzbekDB.toString())
                    BotResponse.basicResponses(message, uzbekDB!!, language)
                } else {
                    Log.e("DataBase", englishDB.toString())
                    BotResponse.basicResponses(message, englishDB!!, language)
                }

                messagesList.add(Message(response, Constants.RECEIVE_ID))

                adapter.insertMessage(Message(response, Constants.RECEIVE_ID))

                recycleView.scrollToPosition(adapter.itemCount - 1)
                online.text = getText(R.string.online)

                if (isSpoken) {
                    textToSpeechFun(response)
                    isSpoken = false
                }

                when (response) {
                    Constants.OPEN_PDP -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.pdp.uz/")
                        startActivity(site)
                    }
                    Constants.EXIT -> {
                        delay(4000)
                        finish()
                    }
                    Constants.CALL -> {
                        delay(2000)
                        val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998787774747"))
                        startActivity(call)
                    }
                    Constants.TELEGRAM -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.telegram.me/pdpuz")
                        startActivity(site)
                    }
                    Constants.INSTAGRAM -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.instagram.com/pdpuz")
                        startActivity(site)
                    }
                    Constants.FACEBOOK -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.facebook.com/pdpuz")
                        startActivity(site)
                    }
                    Constants.YOUTUBE -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.youtube.com/c/pdpuz")
                        startActivity(site)
                    }
                    Constants.TIKTOK -> {
                        delay(2000)
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.tiktok.com/@pdpuz")
                        startActivity(site)
                    }
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1400)
            withContext(Dispatchers.Main) {
                messagesList.add(Message(message, Constants.RECEIVE_ID))
                adapter.insertMessage(Message(message, Constants.RECEIVE_ID))

                recycleView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    fun menu(view: View) {
        val factory = LayoutInflater.from(this)
        val dialogView: View = factory.inflate(R.layout.menu_lay, null)
        val dialog = AlertDialog.Builder(this).create()
        dialog.setView(dialogView)
        dialogView.findViewById<TextView>(R.id.location).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            val intentChooser = Intent.createChooser(intent, "Map")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.telegram).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://t.me/pdpuz")
            val intentChooser = Intent.createChooser(intent, "Launch Telegram")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.official).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://pdp.uz/")
            val intentChooser = Intent.createChooser(intent, "Launch Chrome")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.instagram).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse("https:/instagram.com/pdpuz"))
            val intentChooser = Intent.createChooser(intent, "Launch Instagram")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.openYou).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse("https://www.youtube.com/c/pdpuz"))
            val intentChooser = Intent.createChooser(intent, "Launch Youtube")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun call(view: View) {
        textToSpeechFun("")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998787774747"))
        startActivity(intent)
    }

    fun voice(view: View) {
        textToSpeechFun("")
        speechFun()
    }

    private fun speechFun() {
        isSpoken = true
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.app_name))
        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (exp: ActivityNotFoundException) {
            Toast.makeText(applicationContext, getString(R.string.error_speak), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK || null != data) {
                val res: ArrayList<String> =
                    data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                et_message.setText(res[0])
                sendMessage()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val res: Int = textToSpeech.setLanguage(Locale.CANADA)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_speak),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                textToSpeechFun("")
            }
        } else {
            Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun textToSpeechFun(str: String) {
        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    private fun changeLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
        val editor: SharedPreferences.Editor = getSharedPreferences("Setting", MODE_PRIVATE).edit()
        editor.putString("MyLang", language)
        editor.apply()
    }

}