package ru.droidcat.kicktimer.view_model

import android.widget.TextView
import ru.droidcat.kicktimer.database.model.Project
import androidx.databinding.BindingAdapter

@BindingAdapter("projectNameFormatted")
fun TextView.setProjectName(item: Project?) {
    item?.let{
        text = "${item.project_name} ${item.project_pos}"
    }
}