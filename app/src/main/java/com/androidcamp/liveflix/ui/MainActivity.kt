package com.androidcamp.liveflix.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.androidcamp.liveflix.adapters.ChannelsAdapter
import com.androidcamp.liveflix.adapters.GenresAdapter
import com.androidcamp.liveflix.databinding.ActivityMainBinding
import com.androidcamp.liveflix.models.Channel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ChannelsAdapter.OnClick, GenresAdapter.OnClick {
    private lateinit var genres: HashMap<String, String>
    private lateinit var binding: ActivityMainBinding
    private lateinit var channelsAdapter: ChannelsAdapter
    private lateinit var genresAdapter: GenresAdapter
    private var fileName = "movies"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        channelsAdapter = ChannelsAdapter(this,this)
        binding.channelsRecycler.adapter = channelsAdapter
        genresAdapter = GenresAdapter(this,this)
        binding.genresAdapter.adapter = genresAdapter

        genres = hashMapOf<String,String>().apply {
            this["movies"] = "Movies"
            this["series"] = "Series"
            this["news"] = "News"
            this["culture"] = "Culture"
            this["classic"] = "Classic"
            this["animation"] = "Animation"
            this["cooking"] = "Cooking"
            this["comedy"] = "Comedy"
            this["documentary"] = "Documentary"
            this["education"] = "Education"
            this["entertainment"] = "Entertainment"
            this["family"] = "Family"
            this["kids"] = "Kids"
            this["general"] = "General"
            this["legislative"] = "Legislative"
            this["lifestyle"] = "Lifestyle"
            this["local"] = "Local"
            this["music"] = "Music"
            this["outdoor"] = "Outdoor"
            this["relax"] = "Relax"
            this["religious"] = "Religious"
            this["science"] = "Science"
            this["shop"] = "Shop"
            this["sport"] = "Sport"
            this["travel"] = "Travel"
            this["weather"] = "Weather"
            this["auto"] = "Auto"
            this["business"] = "Business"
            this["un_categorized"] = "Other"
        }
        genresAdapter.setData(genres.values.toList())

        lifecycleScope.launchWhenCreated{
            getChannelsList()
        }
    }

    private fun getChannelsList() = CoroutineScope(Dispatchers.IO).launch {
        val jsonFileString: String = getJsonFromAssets(applicationContext, "$fileName.json")
        val gson = Gson()
        val listUserType = object : TypeToken<List<Channel?>?>(){}.type
        var channels: ArrayList<Channel> = gson.fromJson(jsonFileString, listUserType)
        channels = channels.shuffled() as ArrayList<Channel>

        delay(2500)
        CoroutineScope(Dispatchers.Main).launch {
            channelsAdapter.setData(channels)
            binding.animationView.visibility = View.GONE
        }
    }

    private fun getJsonFromAssets(context: Context, fileName: String?): String {
        val jsonString = try {
                val `is` = context.assets.open(fileName!!)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }
        return jsonString
    }

    override fun onClickChannel(channelName: String, url: String) {
        val i = Intent(this, PlayerActivity::class.java)
        i.putExtra("URL", url)
        i.putExtra("NAME", channelName)
        startActivity(i)
    }

    override fun onClickGenre(genre: String) {
        fileName = genre.lowercase()
        binding.animationView.visibility = View.VISIBLE
        getChannelsList()
    }
}