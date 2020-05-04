package ru.droidcat.kicktimer.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.databinding.TasksViewBinding
import ru.droidcat.kicktimer.view_model.TaskListAdapter
import ru.droidcat.kicktimer.view_model.TaskViewModel

class TasksView: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel
    lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectId: String? = intent.extras?.getString("projectId")
        val projectName: String? = intent.extras?.getString("projectName")

        val binding: TasksViewBinding = DataBindingUtil.setContentView(this, R.layout.tasks_view)
        binding.projectName = projectName

        recyclerView = findViewById(R.id.tasks_list)
        adapter = TaskListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecorator(
                resources.getDimension(R.dimen.task_card_vertical).toInt(),
                resources.getDimension(R.dimen.task_card_horizontal).toInt()))

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.setProjectId(projectId!!)
        taskViewModel.projectTasks.observe(this, Observer { tasks ->
            tasks?.let { adapter.setTasks(it) }
        })

        adapter.setViewModel(taskViewModel)

        taskViewModel.insert()
    }
}

class MarginItemDecorator(
        private val spaceVertical: Int,
        private val spaceHorizontal: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceVertical
            }
            left =  spaceHorizontal
            right = spaceHorizontal
            bottom = spaceVertical
        }
    }
}