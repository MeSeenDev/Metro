package io.github.muntashirakon.music.adapter.song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import io.github.muntashirakon.music.interfaces.CabHolder
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.util.MusicUtil
import java.util.*

class SimpleSongAdapter(
    context: FragmentActivity,
    songs: ArrayList<Song>,
    layoutRes: Int,
    cabHolder: CabHolder?
) : SongAdapter(context, songs, layoutRes, cabHolder) {

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

        holder.imageText?.text = if (fixedTrackNumber > 0) fixedTrackNumber.toString() else "-"
        holder.time?.text = MusicUtil.getReadableDurationString(dataSet[position].duration)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
