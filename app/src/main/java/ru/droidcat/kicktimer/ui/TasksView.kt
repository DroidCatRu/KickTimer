package ru.droidcat.kicktimer.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.databinding.TasksViewBinding
import ru.droidcat.kicktimer.view_model.TaskClickListener
import ru.droidcat.kicktimer.view_model.TaskListAdapter
import ru.droidcat.kicktimer.view_model.TaskViewModel

class TasksView: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel
    lateinit var adapter: TaskListAdapter
    lateinit var binding: TasksViewBinding
    lateinit var projectId: String
    lateinit var projectName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        projectId = intent.extras?.getString("projectId")!!
        projectName = intent.extras?.getString("projectName")!!

        binding = DataBindingUtil.setContentView(this, R.layout.tasks_view)
        binding.projectName = projectName
        binding.executePendingBindings()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.tasks_list)
        adapter = TaskListAdapter(taskClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(MarginItemDecorator(
                resources.getDimension(R.dimen.task_card_vertical).toInt(),
                resources.getDimension(R.dimen.task_card_horizontal).toInt()))

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.setProjectId(projectId)
        taskViewModel.projectTasks.observe(this, Observer { tasks ->
            tasks?.let { adapter.submitList(it) }
        })

        adapter.setViewModel(taskViewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tasks_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.renameProject -> {
                renameProject()
            }
            R.id.deleteProject -> {
                taskViewModel.deleteProject()
                finish()
            }
        }

        return true
    }

    fun insertTask(view: View?) {
        val addDialog = AddDialog(
                "Add new task",
                "task name",
                "add task",
                "task name is empty",
                dialogInsertCallback)
        addDialog.show(supportFragmentManager, addDialog.tag)
    }

    private fun renameProject() {
        val addDialog = AddDialog(
                "Rename project",
                "new project name",
                "rename",
                "project name is empty",
                dialogRenameCallback)
        addDialog.show(supportFragmentManager, addDialog.tag)
    }

    private val dialogInsertCallback = AddDialogCallback { text ->
        taskViewModel.insert(text!!)
    }

    private val dialogRenameCallback = AddDialogCallback { text ->
        taskViewModel.renameProject(text!!)
        binding.projectName = text
        binding.executePendingBindings()
    }

    private val taskClickListener = TaskClickListener { task ->
        val intent = Intent(this, SessionsView::class.java)
        intent.putExtra("taskId", task.task_id)
        intent.putExtra("taskName", task.task_name)
        startActivity(intent)
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