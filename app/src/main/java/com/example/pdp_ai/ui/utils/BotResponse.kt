package com.example.pdp_ai.ui.utils

import org.json.JSONArray

object BotResponse {

    fun basicResponses(question: String, jsonArray: JSONArray, language: String): String {

        val question = question.lowercase()
        for (i in 0 until jsonArray.length()) {
            var isHave = true
            val answerObj = jsonArray.getJSONObject(i).getJSONArray("keys")
            for (j in 0 until answerObj.length()) {
                if (!question.contains(answerObj[j].toString())) {
                    isHave = false
                    break
                }
            }
            if (isHave) {
                return jsonArray.getJSONObject(i).get("answer").toString()
            }
        }

        return when (language) {
            "English" -> "I can't answer that question\nPlease, contact the +998 78 777 47 47 number to get more information"
            "Uzbek" -> "Bu savolga javob bera olmayman\nKo'proq ma'lumot olish uchun, iltimos +998 78 777 47 47 raqamiga qo'ngiroq qiling"
            else -> "Error in Language / Tilda xatolik"
        }

    }

}