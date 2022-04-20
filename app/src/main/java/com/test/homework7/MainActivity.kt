package com.test.homework7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var recycler:RecyclerView
    private var readForJson: Boolean = false

    companion object {
         const val LIST_KEY = "list_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.jsonList)

        savedInstanceState?.let {
            readForJson = it.getBoolean(LIST_KEY)
        }

        findViewById<Button>(R.id.thread).setOnClickListener {
            readForJson = false
            findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.VISIBLE
            parseWithThread()
        }

        findViewById<Button>(R.id.executor).setOnClickListener {
            readForJson = false
            findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.VISIBLE
            parseWithExecutor()
        }

        findViewById<Button>(R.id.intent_service).setOnClickListener {
            readForJson = false
            findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.VISIBLE
            parseWithIntentService()
        }
    }

    private fun parseWithThread() {
        if (!readForJson) {
            Thread {
                Thread.sleep(5000)
                runOnUiThread {
                    setList(parseJson())
                    findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.GONE
                    readForJson = true
                }
            }.start()
        } else {
            Log.d("Loaded", "Already uploaded!")
        }
    }

    private fun parseWithExecutor() {
        if (!readForJson) {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                Thread.sleep(5000)
                runOnUiThread {
                    setList(parseJson())
//                Log.d("JSON", parseJson().toString())
                    findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.GONE
                    readForJson = true
                }
            }
            executor.shutdown()
        } else {
            Log.d("Loaded", "Already uploaded!")
        }
    }

    private fun parseWithIntentService() {
        if (!readForJson) {
            val myIntentService = Intent(this, MyIntentService::class.java)
            startService(myIntentService)
            Thread {
                Thread.sleep(5000)
                runOnUiThread {
                    findViewById<ProgressBar>(R.id.progress_indicator).visibility = View.GONE
                    readForJson = true
                }
            }.start()
        } else {
            Log.d("Loaded", "Already uploaded!")
        }
    }


    private fun setList(jsonItems: List<JsonItem>) {
        recycler.adapter = ItemsAdapter(jsonItems)

        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun parseJson(): List<JsonItem> {
        val jsonString = assets.open("data_Irlix.json")
            .bufferedReader()
            .use { it.readText() }
        val typeToken = object : TypeToken<List<JsonItem>>() {}.type

        return Gson().fromJson(jsonString, typeToken)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(LIST_KEY, readForJson)
        super.onSaveInstanceState(outState)
    }
}