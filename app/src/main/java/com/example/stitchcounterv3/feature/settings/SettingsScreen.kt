package com.example.stitchcounterv3.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.feature.navigation.BottomNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@BottomNavGraph
@Destination
@Composable
fun SettingsScreen() {
    val (dark, setDark) = rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dark Mode")
        Switch(checked = dark, onCheckedChange = setDark)
    }
}

