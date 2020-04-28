package ru.droidcat.kicktimer.view_model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.project_list_item.view.*
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.database.model.Project


class ProjectListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var projects = emptyList<Project>()

    override fun getItemCount() = projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val projectView = inflater.inflate(R.layout.project_list_item, parent, false)
        return ViewHolder(projectView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.projectNameText.text = projects[position].project_name
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val projectNameText: TextView = view.list_item_text
    }

    internal fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }
}