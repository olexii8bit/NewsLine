package com.example.newsline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val data = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (0..5).forEach { i -> data.add("$i element") }

        var recyclerView: RecyclerView = findViewById(R.id.rv_news)
        recyclerView.adapter = RecyclerAdapter(data)

        val btn: Button = findViewById(R.id.button)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener {
            data.add("System.currentTimeMillis().toString()")
            recyclerView.run { adapter?.notifyItemInserted(data.size - 1) }
        }
    }
}