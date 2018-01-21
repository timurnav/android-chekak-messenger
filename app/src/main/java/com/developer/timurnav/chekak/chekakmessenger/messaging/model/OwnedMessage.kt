package com.developer.timurnav.chekak.chekakmessenger.messaging.model

data class OwnedMessage(
        val isMine: Boolean,
        val message: Message
)