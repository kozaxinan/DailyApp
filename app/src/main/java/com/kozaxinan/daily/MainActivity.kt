package com.kozaxinan.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.State
import androidx.compose.collectAsState
import androidx.ui.core.Alignment.Companion.CenterVertically
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
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
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
internal class MainActivity : AppCompatActivity() {

  private val items: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())

  private val onClick: () -> Unit = {
    items.value += listOf(
      Item("One ${System.currentTimeMillis()}", R.drawable.header)
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      DailyTheme {
        val state = items.collectAsState()
        if (state.value.isEmpty()) {
          OnlyAddButton(onClick)
        } else {
          ItemList(state)
        }
      }
    }
  }
}

@Composable
fun OnlyAddButton(onClick: () -> Unit) {
  Box {
    Button(onClick = onClick) {
      Text(text = "Add")
    }
  }
}

@Composable
fun ItemList(items: State<List<Item>>) {
  Column(
    modifier = Modifier.padding(all = 16.dp)
  ) {
    items
      .value
      .forEach {
        ItemView(item = it)
      }
  }
}

@Composable
private fun ItemView(item: Item) {
  Row(
    modifier = Modifier.padding(all = 8.dp)
  ) {
    val image: ImageAsset = imageResource(item.imageId)

    val imageModifier = Modifier
      .preferredHeightIn(maxHeight = 32.dp)
      .preferredWidthIn(maxWidth = 32.dp)
      .clip(shape = RoundedCornerShape(4.dp))

    Image(image, imageModifier, contentScale = ContentScale.Crop)

    Spacer(Modifier.preferredWidth(16.dp))

    Text(
      text = "Hello ${item.name}!",
      style = typography.body1,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.gravity(align = CenterVertically)
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
