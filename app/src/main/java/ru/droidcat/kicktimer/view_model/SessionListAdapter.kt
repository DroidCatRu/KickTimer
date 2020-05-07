package ru.droidcat.kicktimer.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.databinding.SessionsListItemBinding

class SessionListAdapter
    : ListAdapter<ListSession, SessionListAdapter.ViewHolder>(SessionsDiffCallback()) {

    private lateinit var viewModel: SessionViewModel

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
            val binding: SessionsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListSession) {
            binding.session = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SessionsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun setViewModel(sessionViewModel: SessionViewModel) {
        this.viewModel = sessionViewModel
    }
}

class SessionsDiffCallback: DiffUtil.ItemCallback<ListSession>() {
    override fun areContentsTheSame(oldItem: ListSession, newItem: ListSession): Boolean
            = oldItem.session_id == newItem.session_id

    override fun areItemsTheSame(oldItem: ListSession, newItem: ListSession): Boolean
            = oldItem == newItem
}

data class ListSession(
        val session_id: String,
        val session_name: String,
        val task_id: String,
        val work_time: Int,
        val rest_time: Int
)
