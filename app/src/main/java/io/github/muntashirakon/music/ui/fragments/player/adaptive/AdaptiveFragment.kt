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
package io.github.muntashirakon.music.ui.fragments.player.adaptive

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.surfaceColor
import io.github.muntashirakon.music.extensions.textColorPrimary
import io.github.muntashirakon.music.extensions.textColorSecondary
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.ui.fragments.base.AbsPlayerFragment
import io.github.muntashirakon.music.ui.fragments.player.PlayerAlbumCoverFragment
import io.github.muntashirakon.music.util.color.MediaNotificationProcessor
import io.github.muntashirakon.music.util.helper.MusicPlayerRemote
import kotlinx.android.synthetic.main.fragment_adaptive_player.*

class AdaptiveFragment : AbsPlayerFragment(R.layout.fragment_adaptive_player) {

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0
    private lateinit var playbackControlsFragment: AdaptivePlaybackControlsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
        setUpPlayerToolbar()
    }

    private fun setUpSubFragments() {
        playbackControlsFragment =
            childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as AdaptivePlaybackControlsFragment
        val playerAlbumCoverFragment =
            childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragment.apply {
            removeSlideEffect()
            setCallbacks(this@AdaptiveFragment)
        }
    }

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            ToolbarContentTintHelper.colorizeToolbar(this, surfaceColor(), requireActivity())
            setTitleTextColor(textColorPrimary())
            setSubtitleTextColor(textColorSecondary())
            setOnMenuItemClickListener(this@AdaptiveFragment)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateIsFavorite()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        updateIsFavorite()
        updateSong()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        playerToolbar.apply {
            title = song.title
            subtitle = song.artistName
        }
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        playbackControlsFragment.setColor(color)
        lastColor = color.primaryTextColor
        libraryViewModel.updateColor(color.primaryTextColor)
        ToolbarContentTintHelper.colorizeToolbar(
            playerToolbar,
            ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
            requireActivity()
        )
    }

    override fun onShow() {
    }

    override fun onHide() {
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal)
    }

    override val paletteColor: Int
        get() = lastColor
}
