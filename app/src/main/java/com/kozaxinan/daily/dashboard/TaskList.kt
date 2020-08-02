package com.kozaxinan.daily.dashboard

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.layoutId
import androidx.ui.layout.Column
import com.kozaxinan.daily.Tag
import com.kozaxinan.daily.Task
import com.kozaxinan.daily.TaskRowView

@Composable
fun TaskList(items: List<Task>, onItemClick: (Task) -> Unit) {
  Column(
    modifier = Modifier.layoutId(Tag.TaskListTag)
  ) {
    items.forEach {
      TaskRowView(task = it, onItemClick = onItemClick)
    }
  }
}
