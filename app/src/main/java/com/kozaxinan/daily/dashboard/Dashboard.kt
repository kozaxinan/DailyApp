package com.kozaxinan.daily.dashboard

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Add
import com.kozaxinan.daily.Task
import com.kozaxinan.daily.TaskRepository
import com.kozaxinan.daily.samples
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
internal fun DashboardScreen() {
  Scaffold(
    topBar = TopBar(),
    floatingActionButton = AddButton {}
  ) {
    val items: List<Task> = TaskRepository
      .tasks
      .collectAsState()
      .value
      .values
      .toList()

    TaskList(items = items, onItemClick = {})
  }

  samples.forEach(TaskRepository::addTask)
}

@Composable
private fun AddButton(onClick: () -> Unit): @Composable() () -> Unit {
  return {
    FloatingActionButton(onClick = onClick) {
      Icon(asset = Icons.Default.Add)
    }
  }
}

@Composable
private fun TopBar(): @Composable() () -> Unit = {
  TopAppBar(
    title = {
      Text(text = "Daily items")
    }
  )
}
