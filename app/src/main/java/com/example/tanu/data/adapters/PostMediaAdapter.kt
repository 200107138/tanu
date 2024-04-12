package com.example.tanu.data.adapters
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemPostMediaImageBinding
import com.example.tanu.databinding.ItemPostMediaVideoBinding

class PostMediaAdapter(private val mediaUrls: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE) {
            val binding =
                ItemPostMediaImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImageViewHolder(binding)
        } else {
            val binding =
                ItemPostMediaVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VideoViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_IMAGE) {
            val imageHolder = holder as ImageViewHolder
            imageHolder.bind(mediaUrls[position])
        } else {
            val videoHolder = holder as VideoViewHolder
            videoHolder.bind(mediaUrls[position])
        }
    }

    override fun getItemCount(): Int {
        return mediaUrls.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mediaUrls[position].endsWith(".mp4")) {
            TYPE_VIDEO
        } else {
            TYPE_IMAGE
        }
    }

    inner class ImageViewHolder(private val binding: ItemPostMediaImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            Glide.with(binding.root)
                .load(url)
                .into(binding.imageView)
        }
    }

    inner class VideoViewHolder(private val binding: ItemPostMediaVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val videoView: VideoView = binding.videoView

        fun bind(url: String) {
            // Configure video view
            videoView.setVideoURI(Uri.parse(url))

            // Set up media controller
            val mediaController = MediaController(binding.root.context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            // Start playing video when scrolled into view
            videoView.setOnPreparedListener {
                videoView.start()
            }
        }
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }
}