package com.example.memesharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject
import org.json.JSONTokener

class MainActivity : AppCompatActivity() {

    val url = "https://meme-api.com/gimme"
    lateinit var memeimage : ImageView
    lateinit var count_show : TextView
    lateinit var loading : ProgressBar
    var count_memes_shown = 0

    var memeurlArray : ArrayList<String> = arrayListOf<String>(" ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading = findViewById(R.id.loadingPanel)
        count_show = findViewById(R.id.textView2)
        memeimage = findViewById(R.id.imageView)
    }

    fun ShareMeme(view: View) {
        val shareMemeIntent : Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, memeurlArray[memeurlArray.size-1])
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(shareMemeIntent, null)
        startActivity(shareIntent)
        for (ele in memeurlArray){
            Log.d("elements",ele)
        }
    }

    fun loadmeme(){
        val queue = Volley.newRequestQueue(this)
// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                var memeurl = response.getString("url")
                memeurlArray.add(memeurl)
                Glide.with(this).load(memeurl).into(memeimage) // glide -> loads image from image url and isplay it on imageview
                count_memes_shown++
                count_show.setText("You have seen $count_memes_shown Memes\n")

                loading.visibility = View.INVISIBLE
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Log.d("failure",error.toString())
            }
        )
        queue.add(jsonObjectRequest) // Volley takes each request from queue and gives us the response
    }

    fun NextMeme(view: View) {
        loadmeme()
        loading.visibility = View.VISIBLE
    }
}