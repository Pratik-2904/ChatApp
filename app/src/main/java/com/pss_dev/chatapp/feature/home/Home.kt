package com.pss_dev.chatapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    val addChannelState = remember {
        mutableStateOf(false)
    }
    val sheetstate = rememberModalBottomSheetState()
    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Blue)
                    .clickable {
                        addChannelState.value = true
                    }
            ) {
                Text(
                    text = "Add Channel",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(channels.value) { channel ->
                    Column {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Red.copy(alpha = 0.3f))
                                .padding(16.dp)
                                .clickable {
                                    navController.navigate("chat/${channel.id}")
                                },
                            text = channel.name
                        )
                    }
                }
            }
        }
        if (addChannelState.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    addChannelState.value = false
                },
                sheetState = sheetstate
            ) {
                AddChannelDialogue {
                    viewModel.addChannel(it)
                    addChannelState.value = false
                }
            }
        }
    }
}


@Composable
fun AddChannelDialogue(
    onAddChannel: (String) -> Unit
) {
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = channelName.value, onValueChange = { channelName.value = it },
            label = { Text(text = "Channel Name") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = { onAddChannel(channelName.value) }) {
            Text(text = "Add")
        }
    }
}