package com.kozaxinan.daily.dashboard

import androidx.compose.Composable
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Add

@Composable
internal fun DashboardScreen() {
  Scaffold(
    topBar = TopBar(),
    floatingActionButton = AddButton {}
  ) {
    Text(text = "I am Body")
  }
}

@Composable
private fun AddButton(onClick: () -> Unit): @Composable() () -> Unit {
  return {
    FloatingActionButton(onClick = onClick) {
      Icon(asset = Icons.Default.Add)
    }
  }
}

@Composable
private fun TopBar(): @Composable() () -> Unit = {
  TopAppBar(
    title = {
      Text(text = "Daily items")
    }
  )
}
