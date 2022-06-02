package com.emproto.hoabl.feature.investment.views.mediagallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityYoutubeBinding
import com.emproto.hoabl.model.MediaViewItem
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlin.collections.ArrayList

class YoutubeActivity : YouTubeBaseActivity() {

    lateinit var binding:ActivityYoutubeBinding
    var videoId = ""
    var index = 0
    var videoList = ArrayList<MediaViewItem>()
    var videoTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDatas()
        playYoutubeVideo(videoId)

        binding.ivCloseButton.setOnClickListener{
            onBackPressed()
        }

        when(videoList.size){
            0 -> {
                binding.ivMediaRightArrow.visibility = View.GONE
                binding.ivMediaRightArrow.visibility = View.GONE
            }
        }

        binding.ivMediaRightArrow.setOnClickListener {
//            if(index < videoList.size-1){
//                binding.ivMediaRightArrow.visibility = View.VISIBLE
//                index++
//                Toast.makeText(this, index.toString(), Toast.LENGTH_SHORT).show()
//                val item = videoList[index]
//                val youtubeUrl = item.media.replace("https://www.youtube.com/embed/","")
//                playYoutubeVideo(youtubeUrl)
//            }
//            if(index == videoList.size-1){
//                binding.ivMediaRightArrow.visibility = View.GONE
//                binding.ivMediaLeftArrow.visibility = View.VISIBLE
//            }
        }

        binding.ivMediaLeftArrow.setOnClickListener {
//            if(index > 0){
//                binding.ivMediaLeftArrow.visibility = View.VISIBLE
//                index--
//                Toast.makeText(this, index.toString(), Toast.LENGTH_SHORT).show()
//                val item = videoList[index]
//                val youtubeUrl = item.media.replace("https://www.youtube.com/embed/","")
//                playYoutubeVideo(youtubeUrl)
//            }
//            if(index == 0){
//                binding.ivMediaLeftArrow.visibility = View.GONE
//                binding.ivMediaRightArrow.visibility = View.VISIBLE
//            }
        }
    }

    private fun initDatas() {
        videoId = intent.getStringExtra("YoutubeVideoId").toString()
        videoTitle = intent.getStringExtra("VideoTitle").toString()
        binding.tvMediaImageName.text = videoTitle
//        val videoList = intent.getSerializableExtra("VideoList") as ArrayList<MediaViewItem>
//        Log.d("jdjshdsh",videoList.toString())
//
//        for(i in 0..videoList.size-1){
//            val youtubeUrl = videoList[i].media.replace("https://www.youtube.com/embed/","")
//            if(youtubeUrl == videoId){
//                index = i
//            }
//        }
    }

    private fun playYoutubeVideo(youtubeId:String){
        binding.ytPlayer.initialize(resources.getString(R.string.youtube_api_key),object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.loadVideo(youtubeId)
                p1?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "Video player Failed", Toast.LENGTH_SHORT).show();
            }

        })
    }
}