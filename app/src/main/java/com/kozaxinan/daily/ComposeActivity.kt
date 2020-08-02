package com.kozaxinan.daily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.layout.fillMaxSize
import com.kozaxinan.daily.ui.DailyTheme

abstract class ComposeActivity(private val content: @Composable () -> Unit) : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      DailyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
          content()
        }
      }
    }
  }
}
