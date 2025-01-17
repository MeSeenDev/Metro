/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package io.github.muntashirakon.music.ui.fragments.player

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.ui.adapter.album.AlbumCoverPagerAdapter
import io.github.muntashirakon.music.ui.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment
import io.github.muntashirakon.music.ui.fragments.NowPlayingScreen.*
import io.github.muntashirakon.music.ui.fragments.base.AbsMusicServiceFragment
import io.github.muntashirakon.music.ui.transform.CarousalPagerTransformer
import io.github.muntashirakon.music.ui.transform.ParallaxPagerTransformer
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.color.MediaNotificationProcessor
import io.github.muntashirakon.music.util.helper.MusicPlayerRemote
import kotlinx.android.synthetic.main.fragment_player_album_cover.*

class PlayerAlbumCoverFragment : AbsMusicServiceFragment(R.layout.fragment_player_album_cover),
    ViewPager.OnPageChangeListener {

/*

    private var _binding : FragmentPlayerAlbumCoverBinding? = null
    val binding : FragmentPlayerAlbumCoverBinding = _binding!!
*/

    private var callbacks: Callbacks? = null
    private var currentPosition: Int = 0
    private val colorReceiver = object : AlbumCoverFragment.ColorReceiver {
        override fun onColorReady(color: MediaNotificationProcessor, request: Int) {
            if (currentPosition == request) {
                notifyColorChange(color)
            }
        }
    }

    fun removeSlideEffect() {
        val transformer = ParallaxPagerTransformer(R.id.player_image)
        transformer.setSpeed(0.3f)
    }


/*    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerAlbumCoverBinding.inflate(inflater ,container,false)
        return binding.root
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.addOnPageChangeListener(this)
        val nps = PreferenceUtil.nowPlayingScreen

        val metrics = resources.displayMetrics
        val ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()

        if (nps == Full || nps == Classic || nps == Fit || nps == Gradient) {
            viewPager.offscreenPageLimit = 2
        } else if (PreferenceUtil.isCarouselEffect) {
            viewPager.clipToPadding = false
            val padding =
                if (ratio >= 1.777f) {
                    40
                } else {
                    100
                }
            viewPager.setPadding(padding, 0, padding, 0)
            viewPager.pageMargin = 0
            viewPager.setPageTransformer(false, CarousalPagerTransformer(requireContext()))
        } else {
            viewPager.offscreenPageLimit = 2
            viewPager.setPageTransformer(
                true,
                PreferenceUtil.albumCoverTransform
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.removeOnPageChangeListener(this)
    }

    override fun onServiceConnected() {
        updatePlayingQueue()
    }

    override fun onPlayingMetaChanged() {
        viewPager.currentItem = MusicPlayerRemote.position
    }

    override fun onQueueChanged() {
        updatePlayingQueue()
    }

    private fun updatePlayingQueue() {
        viewPager.apply {
            adapter = AlbumCoverPagerAdapter(childFragmentManager, MusicPlayerRemote.playingQueue)
            viewPager.adapter!!.notifyDataSetChanged()
            viewPager.currentItem = MusicPlayerRemote.position
            onPageSelected(MusicPlayerRemote.position)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentPosition = position
        if (viewPager.adapter != null) {
            (viewPager.adapter as AlbumCoverPagerAdapter).receiveColor(colorReceiver, position)
        }
        if (position != MusicPlayerRemote.position) {
            MusicPlayerRemote.playSongAt(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    private fun notifyColorChange(color: MediaNotificationProcessor) {
        callbacks?.onColorChanged(color)
    }

    fun setCallbacks(listener: Callbacks) {
        callbacks = listener
    }

    interface Callbacks {

        fun onColorChanged(color: MediaNotificationProcessor)

        fun onFavoriteToggled()
    }

    companion object {
        val TAG: String = PlayerAlbumCoverFragment::class.java.simpleName
    }
}
