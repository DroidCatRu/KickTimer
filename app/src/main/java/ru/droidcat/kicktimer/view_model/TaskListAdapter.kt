package ru.droidcat.kicktimer.view_model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.database.model.Task
import ru.droidcat.kicktimer.databinding.TasksListItemBinding

class TaskListAdapter(
        val clickListener: TaskClickListener
): ListAdapter<Task, TaskListAdapter.ViewHolder>(TasksDiffCallback()) {

    private lateinit var viewModel: TaskViewModel

    private val doneListener = TaskDoneListener { task ->
        viewModel.setIsDone(task.task_id, !task.task_is_done)
        Log.d("Tasks list", "task done")
    }

    private val favListener = TaskFavListener { task ->
        viewModel.setIsFav(task.task_id, !task.task_is_fav)
        Log.d("Tasks list", "task fav")
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, doneListener, favListener)
    }

    class ViewHolder private constructor(
            val binding: TasksListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task,
                 clickListener: TaskClickListener,
                 doneListener: TaskDoneListener,
                 favListener: TaskFavListener) {
            binding.task = item
            binding.taskClickListener = clickListener
            binding.taskDoneListener = doneListener
            binding.taskFavListener = favListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TasksListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun setViewModel(taskViewModel: TaskViewModel) {
        this.viewModel = taskViewModel
    }
}

class TaskDoneListener(val doneListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = doneListener(task)
}

class TaskFavListener(val favListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = favListener(task)
}

class TaskClickListener(val clickListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = clickListener(task)
}

class TasksDiffCallback: DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean
            = oldItem.task_id == newItem.task_id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean
            = oldItem == newItem

}