package com.climus.climeet.presentation.ui.main.shorts.player

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsDetailBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.util.Constants.TAG
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShortsDetailFragment @Inject constructor(
    private val data: ShortsUiData
): BaseFragment<FragmentShortsDetailBinding>(R.layout.fragment_shorts_detail) {

    private var player : ExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.item = data
        setImage()
        setPlayer()
    }

    private fun setImage(){
        Glide.with(this)
            .load(data.profileImgUrl)
            .error(TEST_IMG)
            .into(binding.ivProfile)
    }


    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }


    @OptIn(UnstableApi::class) private fun setPlayer(){
        player = ExoPlayer.Builder(requireContext())
            .build().apply {
                val source = if(data.videoUrl.contains("m3u8")) getHlsMediaSource()
                else getProgressiveMediaSource()
                setMediaSource(source)
                prepare()
                addListener(playerListener)
            }
    }

    @OptIn(UnstableApi::class) private fun getHlsMediaSource(): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory).
        createMediaSource(MediaItem.fromUri(data.videoUrl))
    }

    @OptIn(UnstableApi::class) private fun getProgressiveMediaSource(): MediaSource{
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(data.videoUrl)))
    }

    private fun releasePlayer(){
        player?.apply {
            playWhenReady = false
            release()
        }
        player = null
    }

    private fun pause(){
        player?.playWhenReady = false
    }

    private fun play(){
        player?.playWhenReady = true
    }

    private fun restartPlayer(){
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    private val playerListener = object: Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState){
                STATE_ENDED -> restartPlayer()
                STATE_READY -> {
                    binding.exoPlayer.player = player
                    play()
                }
                else -> {}
            }
        }
    }


}