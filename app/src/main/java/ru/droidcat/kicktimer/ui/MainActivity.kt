package ru.droidcat.kicktimer.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.view_model.ProjectListAdapter
import ru.droidcat.kicktimer.view_model.ProjectListener
import ru.droidcat.kicktimer.view_model.ProjectViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProjectListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_appbar)

        recyclerView = findViewById(R.id.projects_list)
        adapter = ProjectListAdapter(projectListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        itemTouchHelper.attachToRecyclerView(recyclerView)

        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        projectViewModel.allProjects.observe(this, Observer { projects ->
            projects?.let { adapter.setProjects(it) }
        })

        adapter.setViewModel(projectViewModel)

        fab = findViewById(R.id.add_project_button)
        fab.setOnClickListener {
            projectViewModel.insert()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawer()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
                object : ItemTouchHelper.SimpleCallback(UP or
                        DOWN, 0) {

                    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                        super.onSelectedChanged(viewHolder, actionState)
                        viewHolder?.itemView?.translationZ = 8.0f
                        viewHolder?.itemView?.foreground = resources.getDrawable(R.drawable.project_list_hovered)
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                        super.clearView(recyclerView, viewHolder)
                        viewHolder.itemView.translationZ = 0.0f
                        viewHolder.itemView.foreground = null
                        //val adapter = recyclerView.adapter as ProjectListAdapter
                        adapter.moveItem()
                    }

                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {

                        //val adapter = recyclerView.adapter as ProjectListAdapter
                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition
                        // Tell adapter to render the model update.
                        adapter.notifyItemMoved(from, to)
                        adapter.setMove(from, to)

                        return true
                    }

                }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    private val projectListener = ProjectListener { project ->
        val intent = Intent(this, TasksView::class.java)
        intent.putExtra("projectId", project.project_id)
        intent.putExtra("projectName", project.project_name)
        startActivity(intent)
    }

}