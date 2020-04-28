package ru.droidcat.kicktimer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        projectViewModel.allProjects.observe(this, Observer { projects ->
            projects?.let { adapter.setProjects(it) }
        })

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
}