package com.kozaxinan.daily

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
internal object TaskRepository {

  private val _channel: ConflatedBroadcastChannel<List<Task>> = ConflatedBroadcastChannel(emptyList())
  val tasks: Flow<List<Task>> get() = _channel.asFlow()

  suspend fun addTask(task: Task) {
    _channel.send(_channel.value + task)
  }

  suspend fun deleteTask(task: Task) {
    _channel.send(_channel.value - task)
  }
}
