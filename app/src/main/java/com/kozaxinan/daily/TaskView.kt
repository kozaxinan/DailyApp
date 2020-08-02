package com.kozaxinan.daily

import androidx.compose.Composable
import androidx.ui.core.Alignment.Companion.CenterVertically
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.layout.preferredWidth
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp

@Composable
fun TaskRowView(task: Task, onItemClick: (Task) -> Unit) {
  Box(modifier = Modifier.clickable(onClick = { onItemClick(task) })) {
    Row(
      modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 16.dp)
        .fillMaxWidth()
    ) {
      val image: ImageAsset = imageResource(task.imageId)

      val imageModifier = Modifier
        .preferredSize(32.dp)
        .clip(shape = RoundedCornerShape(4.dp))

      Image(image, imageModifier, contentScale = ContentScale.Crop)

      Spacer(Modifier.preferredWidth(16.dp))

      Text(
        text = task.name,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.gravity(align = CenterVertically)
      )
    }
  }
}
