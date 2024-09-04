package com.pss_dev.chatapp.feature.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pss_dev.chatapp.model.Message

@Composable
fun ChatScreen(
    navController: NavController,
    channelId: String,
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val messages = viewModel.messages.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            ChatMessages(
                messages = messages.value,
                onSendMessage = {
                    viewModel.sendMessage(channelId, it)
                }
            )

        }
    }


}


@Composable
fun ChatMessages(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    val msg = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.LightGray),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f),
                value = msg.value,
                onValueChange = { msg.value = it },
                placeholder = { Text(text = "Type Something") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        keyboardController?.hide()
                        //Todo sending the message logic
                        onSendMessage(msg.value.trim())
                        msg.value = ""
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        //Todo sending the message logic
                        onSendMessage(msg.value.trim())
                        msg.value = ""

                    }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Send,
                            contentDescription = "send messages"
                        )
                    }
                }

            )
        }
    }
}


@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Blue
    } else {
        Color.Green
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {

        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp)),
            contentAlignment = if (!isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd
        ) {
            Text(
                text = message.message,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}
