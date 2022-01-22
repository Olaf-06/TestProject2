package com.example.aleftestproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.json.JSONArray
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val okHttpClient: OkHttpClient = OkHttpClient()
    private val request: Request = Request.Builder().url("http://dev-tasks.alef.im/task-m-001/list.php").build()
    private val call: okhttp3.Call = okHttpClient.newCall(request)
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialog: Dialog
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val messages: ArrayList<String> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.img_dialog)


        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //Если данное устройство - планшет, то изображения выстроятся в 3 ряда, если телефон, то в 2 ряда
        if (isTablet()) {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }

        val adapter: Adapter = Adapter(this, messages, dialog)
        recyclerView.adapter = adapter

        call.enqueue(object : okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("myLog", e.message ?: "null")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseJson: String? = response.body?.string()
                val jsonArray: JSONArray = JSONArray(responseJson)
                for (i in 0 until jsonArray.length()){
                    messages.add(jsonArray.getString(i))
                }
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }

        })

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    //проверяет: является ли текущее устройство планшетом или нет
    fun isTablet(): Boolean {
        val xlarge = this.getResources()
            .getConfiguration().screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE
        val large = this.getResources()
            .getConfiguration().screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }
}