package com.kozaxinan.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.Alignment.Companion.CenterVertically
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.layoutId
import androidx.ui.core.setContent
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet2
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.layout.preferredWidth
import androidx.ui.layout.width
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.kozaxinan.daily.ui.DailyTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
internal class MainActivity : AppCompatActivity() {

  private val onClick: () -> Unit = {
    lifecycleScope.launch {
      val task = Task("One ${System.currentTimeMillis()}", R.drawable.header)
      TaskRepository.addTask(task)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      DailyTheme {
        Surface {
          val state = TaskRepository.tasks.collectAsState(initial = emptyList())
          val items = state.value
          if (items.isEmpty()) {
            OnlyAddButton(onClick)
          } else {
            TaskListWithAdd(items, onClick)
          }
        }
      }
    }
  }
}

@Composable
fun OnlyAddButton(onClick: () -> Unit = {}) {
  val constraintSet = ConstraintSet2 {
    constrain(createRefFor(Tag.AddButtonTag)) {
      centerTo(parent)
    }
  }
  ConstraintLayout(constraintSet = constraintSet) {
    AddButton(onClick)
  }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.layoutId(Tag.AddButtonTag)) {
    Text(text = "Add")
  }
}

@Composable
fun TaskListWithAdd(items: List<Task>, onClick: () -> Unit = {}) {
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
    TaskList(items = items)
    AddButton(onClick)
  }
}

@Composable
fun TaskList(items: List<Task>) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .layoutId(Tag.TaskListTag)
  ) {
    items.forEach {
      TaskView(task = it)
    }
  }
}

@Composable
private fun TaskView(task: Task) {
  Row(
    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
  ) {
    val image: ImageAsset = imageResource(task.imageId)

    val imageModifier = Modifier
      .height(32.dp)
      .width(32.dp)
      .clip(shape = RoundedCornerShape(4.dp))

    Image(image, imageModifier, contentScale = ContentScale.Crop)

    Spacer(Modifier.preferredWidth(16.dp))

    Text(
      text = "Hello ${task.name}!",
      style = MaterialTheme.typography.body1,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      color = MaterialTheme.colors.onSurface,
      modifier = Modifier.gravity(align = CenterVertically)
    )
  }
}

val samples: List<Task> = listOf(
  Task("One ${System.currentTimeMillis()}", R.drawable.header)
)

@Preview
@Composable
fun ItemsPreview() {
  DailyTheme(darkTheme = true) {
    TaskListWithAdd(
      samples
    )
  }
}

@Preview
@Composable
fun OnlyAddButtonPreview() {
  DailyTheme(darkTheme = true) {
    OnlyAddButton()
  }
}

sealed class Tag {

  object AddButtonTag : Tag()
  object TaskListTag : Tag()
}
