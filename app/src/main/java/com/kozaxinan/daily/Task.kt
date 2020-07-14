package com.kozaxinan.daily

import androidx.annotation.DrawableRes

data class Task(
  val name: String,
  @DrawableRes val imageId: Int
)
