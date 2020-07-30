package com.kozaxinan.daily

import androidx.annotation.DrawableRes

data class Task(
  val id: Long,
  val name: String,
  @DrawableRes val imageId: Int
)
