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
package io.github.muntashirakon.music.ui.adapter.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import io.github.muntashirakon.music.interfaces.ICabHolder
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.util.MusicUtil
import java.util.*

class SimpleSongAdapter(
    context: FragmentActivity,
    songs: ArrayList<Song>,
    layoutRes: Int,
    ICabHolder: ICabHolder?
) : SongAdapter(context, songs, layoutRes, ICabHolder) {

    override fun swapDataSet(dataSet: List<Song>) {
        this.dataSet = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val fixedTrackNumber = MusicUtil.getFixedTrackNumber(dataSet[position].trackNumber)
        val trackAndTime = (if (fixedTrackNumber > 0) "$fixedTrackNumber | " else "") +
                MusicUtil.getReadableDurationString(dataSet[position].duration)

        holder.imageText?.visibility = View.GONE
        holder.time?.text = trackAndTime
        holder.text2?.text = dataSet[position].artistName
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
