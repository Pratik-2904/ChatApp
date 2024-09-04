package com.pss_dev.chatapp.feature.chats

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pss_dev.chatapp.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()
    private val db = Firebase.database


        fun sendMessage(channelID: String, message: String) {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                val msg = Message(
                    id = db.reference.push().key ?: UUID.randomUUID().toString(),
                    senderId = currentUser.uid,
                    senderName = currentUser.displayName ?: "Unknown",
                    message = message,
                    timestamp = System.currentTimeMillis(),
                    imageUrl = null,
                    senderImage = null
                )
                db.getReference("message").child(channelID).child(msg.id).setValue(msg)
            } else {
                // Handle case when user is not logged in
            }
        }

    fun listenForMessages(channelID: String) {
        //firebase db
        db.getReference("message").child(channelID).orderByChild("timestamp")
//            .addValueEventListener(
//                object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val list = mutableListOf<Message>()
//                        snapshot.children.forEach { data ->
//                            val message = data.getValue(Message::class.java)
//                            message?.let {
//                                list.add(it)
//                            }
//                        }
//                        _messages.value = list
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        // Log the error or notify the user
//                        Log.e("ChatViewModel", "Error fetching messages: ${error.message}")
//                    }
//
//                }
//            )
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue(Message::class.java)?.let { message ->
                        _messages.value += message
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            )

    }
}