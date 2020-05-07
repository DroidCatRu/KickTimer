package ru.droidcat.kicktimer.view_model

import android.widget.TextView
import ru.droidcat.kicktimer.database.model.Project
import androidx.databinding.BindingAdapter

@BindingAdapter("projectNameFormatted")
fun TextView.setProjectName(item: Project?) {
    item?.let{
        text = item.project_name
    }
}

@BindingAdapter("timeInSecFormatted")
fun TextView.setTimeFromSec(time: Int?) {
    time?.let {
        val h: Int = it/3600
        val m: Int = it/60 - h*60
        val s: Int = it - h*3600 - m*60
        val ph = when {
            h == 0 -> ""
            else -> "${h}h"
        }
        val pm = when {
            h == 0 && m == 0 -> ""
            else -> "${m}m"
        }
        val ps = "${s}s"
        text = "${ph} ${pm} ${ps}"
    }
}