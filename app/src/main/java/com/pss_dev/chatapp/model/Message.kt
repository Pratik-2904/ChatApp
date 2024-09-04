package com.pss_dev.chatapp.model

data class Message(
    val id: String = "",
    val senderId: String = "",
//    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderImage: String? = null,
    val imageUrl: String? = null
)
