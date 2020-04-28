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
    private lateinit var viewModel: ProjectViewModel

    private var from: Int = 0
    private var to: Int =0

    override fun getItemCount() = projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val projectView = inflater.inflate(R.layout.project_list_item, parent, false)
        return ViewHolder(projectView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = projects[position].project_name
        val pos = projects[position].project_pos
        val text = "$name $pos"
        holder.projectNameText.text = text
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val projectNameText: TextView = view.list_item_text
    }

    internal fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    fun setViewModel(projectViewModel: ProjectViewModel) {
        this.viewModel = projectViewModel
    }

    fun setMove(from: Int, to: Int) {
        this.from = from
        this.to = to
    }

    fun moveItem() {
        viewModel.moveItem(this.from, this.to)
    }
}