package com.kozaxinan.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.State
import androidx.compose.collectAsState
import androidx.compose.mutableStateOf
import androidx.compose.remember
import androidx.lifecycle.lifecycleScope
import androidx.ui.animation.Crossfade
import androidx.ui.core.Alignment.Companion.CenterHorizontally
import androidx.ui.core.Modifier
import androidx.ui.core.focus.ExperimentalFocus
import androidx.ui.core.focus.FocusModifier
import androidx.ui.core.layoutId
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet2
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Button
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.Surface
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.kozaxinan.daily.dashboard.TaskList
import com.kozaxinan.daily.ui.DailyTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@ExperimentalFocus
@FlowPreview
@ExperimentalCoroutinesApi
internal class MainActivity : AppCompatActivity() {

  private var routerState: MutableStateFlow<RouterState> = MutableStateFlow(RouterState.Empty)

  private val onNewClick: () -> Unit = {
    selectedTask = null
    routerState.value = RouterState.Edit
  }

  private val onSaveClick: (Task) -> Unit = {
    lifecycleScope.launch {
      TaskRepository.addTask(it)
      routerState.value = RouterState.List
    }
  }

  private val onItemClick: (Task) -> Unit = {
    selectedTask = it
    routerState.value = RouterState.Edit
  }

  private var selectedTask: Task? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      DailyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val state: State<Map<Long, Task>> = TaskRepository.tasks.collectAsState()
          val items: Map<Long, Task> = state.value

          val screen = routerState.collectAsState().value
          Crossfade(screen) {
            when (it) {
              RouterState.Empty -> OnlyAddButton(onNewClick)
              RouterState.Edit -> TaskEditView(selectedTask, onSaveClick)
              RouterState.List -> TaskListWithAdd(items.values.toList(), onNewClick, onItemClick)
            }
          }
        }
      }
    }
  }
}

@Composable
fun OnlyAddButton(onClick: () -> Unit) {
  val constraintSet = ConstraintSet2 {
    constrain(createRefFor(Tag.AddButtonTag)) {
      centerTo(parent)
    }
  }
  ConstraintLayout(
    constraintSet = constraintSet,
    modifier = Modifier.fillMaxSize()
  ) {
    AddButton(onClick)
  }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
  Button(
    onClick = onClick,
    modifier = Modifier
      .layoutId(Tag.AddButtonTag)
      .padding(16.dp)
  ) {
    Text(text = "Add")
  }
}

@Composable
fun TaskListWithAdd(
  items: List<Task>,
  onClick: () -> Unit,
  onItemClick: (Task) -> Unit
) {
  val constraintSet = ConstraintSet2 {
    val addButtonRef = createRefFor(Tag.AddButtonTag)
    constrain(addButtonRef) {
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(parent.bottom)
    }

    val taskListRef = createRefFor(Tag.TaskListTag)
    constrain(taskListRef) {
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      top.linkTo(parent.top)
      bottom.linkTo(addButtonRef.top)
    }
  }
  ConstraintLayout(
    constraintSet = constraintSet,
    modifier = Modifier.fillMaxSize()
  ) {
    TaskList(items = items, onItemClick = onItemClick)
    AddButton(onClick)
  }
}

@ExperimentalFocus
@Composable
fun TaskEditView(task: Task? = null, onSaveClick: (Task) -> Unit) {
  Column(
    modifier = Modifier.padding(16.dp)
  ) {
    val (error, setError) = remember { mutableStateOf(false) }
    val (taskName, setTaskName) = remember { mutableStateOf(task?.name ?: "") }
    val focusModifier = FocusModifier()
    val onValueChange = { newTaskName: String ->
      setTaskName(newTaskName)
      setError(newTaskName.isBlank())
    }

    TaskEditText(taskName, onValueChange, focusModifier, error)

    val onSave: () -> Unit = {
      val value: String = taskName
      if (value.isEmpty()) {
        focusModifier.requestFocus()
        setError(true)
      } else {
        val id: Long = task?.id ?: UUID.randomUUID().mostSignificantBits
        onSaveClick(Task(id, value, R.drawable.header))
      }
    }

    val saveButtonText: String = if (task == null) "Create" else "Apply"
    HorizontalButton(onSave, saveButtonText)
  }
}

@Composable
private fun TaskEditText(
  text: String,
  onValueChange: (String) -> Unit,
  focusModifier: FocusModifier,
  error: Boolean
) {
  OutlinedTextField(
    value = text,
    onValueChange = onValueChange,
    label = { Text(text = "What is the task?") },
    modifier = Modifier
      .fillMaxWidth() +
      focusModifier,
    isErrorValue = error
  )
}

@Composable
private fun HorizontalButton(onSaveClick: () -> Unit, text: String) {
  Button(
    onClick = onSaveClick,
    modifier = Modifier.Companion
      .gravity(CenterHorizontally)
      .padding(16.dp)
  ) {
    Text(text = text)
  }
}

val samples: List<Task> = listOf(
  Task(
    id = 1,
    name = "One ${System.currentTimeMillis()}",
    imageId = R.drawable.header
  )
)

@Preview
@Composable
fun ItemsPreview() {
  DailyTheme(darkTheme = true) {
    TaskListWithAdd(
      samples,
      onItemClick = {},
      onClick = {}
    )
  }
}

@ExperimentalFocus
@Preview
@Composable
fun TaskEditViewPreview() {
  DailyTheme(darkTheme = true) {
    TaskEditView(
      samples.first(),
      {}
    )
  }
}

//@Preview
//@Composable
//fun OnlyAddButtonPreview() {
//  DailyTheme(darkTheme = true) {
//    OnlyAddButton {}
//  }
//}

sealed class Tag {

  object AddButtonTag : Tag()
  object TaskListTag : Tag()
}

sealed class RouterState {

  object Empty : RouterState()
  object Edit : RouterState()
  object List : RouterState()
}
