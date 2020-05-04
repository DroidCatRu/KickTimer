package ru.droidcat.kicktimer.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.droidcat.kicktimer.database.model.Project
import ru.droidcat.kicktimer.databinding.ProjectListItemBinding


class ProjectListAdapter(val clickListener: ProjectListener): RecyclerView.Adapter<ProjectListAdapter.ViewHolder>(){

    private lateinit var viewModel: ProjectViewModel
    private var projects = emptyList<Project>()
    private var from: Int = 0
    private var to: Int =0

    override fun getItemCount(): Int = projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(projects[position], clickListener)
    }

    class ViewHolder private constructor(
            val binding: ProjectListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Project, clickListener: ProjectListener) {
            binding.project = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProjectListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    fun setViewModel(projectViewModel: ProjectViewModel) {
        this.viewModel = projectViewModel
    }

    fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    fun setMove(from: Int, to: Int) {
        this.from = from
        this.to = to
    }

    fun moveItem() {
        if(from != to) {
            viewModel.moveItem(this.from, this.to)
            from = 0
            to = 0
        }
    }

    fun getProjectId(position: Int): String? {
        return viewModel.getItemId(position)
    }
}

class ProjectListener(val clickListener: (project: Project) -> Unit) {
    fun onClick(project: Project) = clickListener(project)
}