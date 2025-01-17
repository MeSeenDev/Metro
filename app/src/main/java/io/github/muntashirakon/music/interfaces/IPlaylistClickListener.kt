package io.github.muntashirakon.music.interfaces

import android.view.View
import io.github.muntashirakon.music.data.db.PlaylistWithSongs

interface IPlaylistClickListener {
    fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View)
}