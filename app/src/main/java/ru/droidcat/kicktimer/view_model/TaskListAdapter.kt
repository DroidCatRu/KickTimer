package ru.droidcat.kicktimer.view_model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.project_list_item.view.list_item_text
import kotlinx.android.synthetic.main.tasks_list_item.view.*
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.database.model.Task

class TaskListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks = emptyList<Task>()
    private lateinit var viewModel: TaskViewModel

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskView = inflater.inflate(R.layout.tasks_list_item, parent, false)
        return ViewHolder(taskView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = tasks[position].task_name
        val isDone = tasks[position].task_is_done
        holder.taskNameText.text = name
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val taskNameText: TextView = view.list_item_text
    }

    internal fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun setViewModel(taskViewModel: TaskViewModel) {
        this.viewModel = taskViewModel
    }
}