package com.kozaxinan.daily

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow

@FlowPreview
@ExperimentalCoroutinesApi
internal object TaskRepository {

  val tasks: MutableStateFlow<Map<Long, Task>> = MutableStateFlow(emptyMap())

  fun addTask(task: Task) {
    tasks.value = tasks.value + (task.id to task)
  }

  fun deleteTask(task: Task) {
    tasks.value = tasks.value - task.id
  }
}
