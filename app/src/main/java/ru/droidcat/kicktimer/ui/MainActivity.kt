package ru.droidcat.kicktimer.ui

import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import ru.droidcat.kicktimer.view_model.ProjectViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_appbar)

        val recyclerView: RecyclerView = findViewById(R.id.projects_list)
        val adapter = ProjectListAdapter(this)

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
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
                object : ItemTouchHelper.SimpleCallback(UP or
                        DOWN or
                        START or
                        END, 0) {

                    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                        super.onSelectedChanged(viewHolder, actionState)

                        if (actionState == ACTION_STATE_DRAG) {
                            viewHolder?.itemView?.alpha = 0.5f
                        }
                    }

                    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                        super.clearView(recyclerView, viewHolder)
                        viewHolder.itemView.alpha = 1.0f
                        val adapter = recyclerView.adapter as ProjectListAdapter
                        adapter.moveItem()
                    }

                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {

                        val adapter = recyclerView.adapter as ProjectListAdapter
                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition
                        // Tell adapter to render the model update.
                        adapter.notifyItemMoved(from, to)
                        adapter.setMove(from, to)

                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                          direction: Int) {
                        // 4. Code block for horizontal swipe.
                        //    ItemTouchHelper handles horizontal swipe as well, but
                        //    it is not relevant with reordering. Ignoring here.
                    }
                }
        ItemTouchHelper(simpleItemTouchCallback)
    }
}