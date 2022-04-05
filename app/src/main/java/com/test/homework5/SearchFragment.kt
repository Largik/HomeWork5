package com.test.homework5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding4.widget.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var jsonList: List<JsonItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.jsonList)

        readJson()

        view.findViewById<SearchView>(R.id.searchRequest).queryTextChanges()
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.toString().isNotEmpty()) {
                    if(search(it.toString()).isNotEmpty()){
                        setList(search(it.toString()))
                        recycler.visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.searchResult).visibility = View.GONE
                    }
                    else {
                        recycler.visibility = View.GONE
                        view.findViewById<TextView>(R.id.searchResult)?.visibility  = View.VISIBLE
                    }
                } else{
                    recycler.visibility = View.VISIBLE
                }
            }, {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            })
    }

    private fun search(request: String): List<JsonItem> {
        return jsonList.filter {
            request in it.id.toString() ||
            request in it.userId.toString() ||
            request in it.title ||
            request in it.body
        }
    }

    private fun readJson() {
        Observable.create<List<JsonItem>> {
            it.onNext(parseJson())
        }.subscribe({
            setList((it))
            jsonList = it
        }, {
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setList(jsonItems: List<JsonItem>) {
        recycler.adapter = ItemsAdapter(jsonItems)

        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun parseJson(): List<JsonItem> {
        val jsonString = requireContext().assets.open("data_Irlix.json")
            .bufferedReader()
            .use { it.readText() }
        val typeToken = object : TypeToken<List<JsonItem>>() {}.type

        return Gson().fromJson(jsonString, typeToken)
    }
}