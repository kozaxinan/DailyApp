package com.kozaxinan.daily

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
internal object TaskRepository {

  private val _channel: ConflatedBroadcastChannel<HashSet<Task>> = ConflatedBroadcastChannel(HashSet())
  val tasks: Flow<HashSet<Task>> get() = _channel.asFlow()

  suspend fun addTask(task: Task) {
    val values = _channel.value
    values.add(task)
    _channel.send(values)
  }

  suspend fun deleteTask(task: Task) {
    val values = _channel.value
    values.remove(task)
    _channel.send(values)
  }
}
