package dog.snow.androidrecruittest.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.ui.model.ListItem
import kotlinx.android.synthetic.main.list_item.view.*
import timber.log.Timber.d

class ListAdapter(private val onClick: (item: ListItem, position: Int, view: View) -> Unit) :
        androidx.recyclerview.widget.ListAdapter<ListItem, ListAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        d("onCreateViewHolder")
        return ViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(getItem(position))

    class ViewHolder(
            itemView: View,
            private val onClick: (item: ListItem, position: Int, view: View) -> Unit
    ) :
            RecyclerView.ViewHolder(itemView) {
        fun bind(item: ListItem) = with(itemView) {
            iv_thumb.transitionName = "$adapterPosition-${item.thumbnailUrl}"
            tv_photo_title.transitionName = "$adapterPosition-${item.title}"
            tv_album_title.transitionName = "$adapterPosition-${item.albumTitle}"
            tv_photo_title.text = item.title
            tv_album_title.text = item.albumTitle
            Picasso.get().load(item.thumbnailUrl).into(iv_thumb);
            setOnClickListener { onClick(item, item.id, this) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                    oldItem == newItem
        }
    }
}