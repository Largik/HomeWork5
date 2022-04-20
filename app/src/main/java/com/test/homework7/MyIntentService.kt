package com.test.homework7

import android.app.IntentService
import android.content.Intent

class MyIntentService: IntentService("MyService") {
    companion object {
        const val KEY_PARSED_LIST = "key_parsed_list"
        const val LIST_PARSED = "list_parsed"
    }
    override fun onHandleIntent(intent: Intent?) {
        val extraData = getJson()
        val responseIntent = Intent()
        responseIntent.action = LIST_PARSED
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT)
        responseIntent.putExtra(KEY_PARSED_LIST, extraData)
        sendBroadcast(responseIntent)
    }

    private fun getJson(): String {
        return assets.open("data_Irlix.json")
            .bufferedReader()
            .use { it.readText() }
    }
}