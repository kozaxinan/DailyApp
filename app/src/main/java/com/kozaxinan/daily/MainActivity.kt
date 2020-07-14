package com.kozaxinan.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.State
import androidx.compose.collectAsState
import androidx.compose.state
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.Alignment.Companion.Center
import androidx.ui.core.Alignment.Companion.CenterVertically
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.setContent
import androidx.ui.core.tag
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet2
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeightIn
import androidx.ui.layout.preferredWidth
import androidx.ui.layout.preferredWidthIn
import androidx.ui.material.Button
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.kozaxinan.daily.ui.DailyTheme
import com.kozaxinan.daily.ui.typography
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
        val state = TaskRepository.tasks.collectAsState(initial = emptyList())
        if (state.value.isEmpty()) {
          OnlyAddButton(onClick)
        } else {
          TaskListWithAdd(state, onClick)
        }
      }
    }
  }
}

@Composable
fun OnlyAddButton(onClick: () -> Unit) {
  Box(
    gravity = Center,
    modifier = Modifier.fillMaxSize()
  ) {
    AddButton(onClick)
  }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.tag(Tag.AddButtonTag)) {
    Text(text = "Add")
  }
}

@Composable
fun TaskListWithAdd(items: State<List<Task>>, onClick: () -> Unit) {
  val constraintSet = ConstraintSet2 {
    constrain(createRefFor(Tag.AddButtonTag)) {
      end.linkTo(parent.end, 32.dp)
      bottom.linkTo(parent.bottom, 32.dp)
    }

    constrain(createRefFor(Tag.TaskListTag)) {
      start.linkTo(parent.start)
      centerTo(parent)
    }
  }
  ConstraintLayout(
    constraintSet = constraintSet,
    modifier = Modifier.fillMaxWidth() +
      Modifier.fillMaxHeight()
  ) {
    TaskList(items = items)
    AddButton(onClick)
  }
}

@Composable
fun TaskList(items: State<List<Task>>) {
  Column(
    modifier = Modifier
      .padding(all = 16.dp)
      .fillMaxSize()
      .tag(Tag.TaskListTag)
  ) {
    items
      .value
      .forEach {
        TaskView(task = it)
      }
  }
}

@Composable
private fun TaskView(task: Task) {
  Row(
    modifier = Modifier.padding(all = 8.dp)
  ) {
    val image: ImageAsset = imageResource(task.imageId)

    val imageModifier = Modifier
      .preferredHeightIn(maxHeight = 32.dp)
      .preferredWidthIn(maxWidth = 32.dp)
      .clip(shape = RoundedCornerShape(4.dp))

    Image(image, imageModifier, contentScale = ContentScale.Crop)

    Spacer(Modifier.preferredWidth(16.dp))

    Text(
      text = "Hello ${task.name}!",
      style = typography.body1,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.gravity(align = CenterVertically)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ItemsPreview() {
  DailyTheme {
    TaskList(
      state {
        listOf(
          Task("One ${System.currentTimeMillis()}", R.drawable.header)
        )
      }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun OnlyAddButtonPreview() {
  DailyTheme {
    OnlyAddButton({ })
  }
}

sealed class Tag {

  object AddButtonTag: Tag()
  object TaskListTag: Tag()
}
