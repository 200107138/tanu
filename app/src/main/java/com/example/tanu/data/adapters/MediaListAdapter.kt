package com.example.tanu.data.adapters
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.databinding.ItemMediaBinding

class MediaListAdapter(private val images: List<Uri>) : RecyclerView.Adapter<MediaListAdapter.MediaItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val imageUri = images[position]
        Glide.with(holder.binding.root)
            .load(imageUri)
            .into(holder.binding.imageViewMedia)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class MediaItemViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)
}