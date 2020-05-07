package ru.droidcat.kicktimer.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.databinding.SessionsViewBinding
import ru.droidcat.kicktimer.view_model.SessionListAdapter
import ru.droidcat.kicktimer.view_model.SessionViewModel
import java.text.SimpleDateFormat
import java.util.*

class SessionsView: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var taskId: String
    lateinit var taskName: String
    lateinit var binding: SessionsViewBinding
    lateinit var adapter: SessionListAdapter
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = intent.extras?.getString("taskId")!!
        taskName = intent.extras?.getString("taskName")!!

        binding = DataBindingUtil.setContentView(this, R.layout.sessions_view)
        binding.taskName = taskName
        binding.executePendingBindings()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.sessions_list)
        adapter = SessionListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        sessionViewModel.setTaskId(taskId)
        sessionViewModel.taskSessions.observe(this, Observer { sessions ->
            sessions?.let { adapter.submitList(it) }
        })

        adapter.setViewModel(sessionViewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sessions_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.renameTask -> {
                renameTask()
            }
            R.id.deleteTask -> {
                sessionViewModel.deleteTask()
                finish()
            }
        }

        return true
    }

    fun newSession(view: View?) {
        val intent = Intent(this, TimerView::class.java)
        val sdfId = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val sessionId = sdfId.format(Date())
        sessionViewModel.insert(sessionId)
        intent.putExtra("sessionId", sessionId)
        startActivity(intent)
    }

    private fun renameTask() {
        val addDialog = AddDialog(
                "Rename task",
                "new task name",
                "rename",
                "task name is empty",
                dialogRenameCallback)
        addDialog.show(supportFragmentManager, addDialog.tag)
    }

    private val dialogRenameCallback = AddDialogCallback { text ->
        sessionViewModel.renameTask(text!!)
        binding.taskName = text
        binding.executePendingBindings()
    }
}