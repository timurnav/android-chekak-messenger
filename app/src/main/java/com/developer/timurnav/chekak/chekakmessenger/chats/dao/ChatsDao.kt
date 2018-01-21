package com.developer.timurnav.chekak.chekakmessenger.chats.dao

import com.developer.timurnav.chekak.chekakmessenger.chats.model.ChatInfo
import com.developer.timurnav.chekak.chekakmessenger.users.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.users.model.User

class ChatsDao {

    private val remoteRepository = ChatsRemoteRepository()
    private val userDao = UserDao()

    fun getChatIdWithUser(userId: String, onIdFetched: (id: String) -> Unit) {
        remoteRepository.fetchAllPrivateChatIds(onIdsFetched = {
            if (it.containsKey(userId)) {
                onIdFetched(it[userId]!!)
            } else {
                remoteRepository.createPrivateChatWith(
                        userId = userId,
                        onCreated = onIdFetched)
            }
        })
    }

    fun getAllChats(onChatsFetched: (List<ChatInfo>) -> Unit) {
        remoteRepository.fetchAllPrivateChatIds(onIdsFetched = { userIdOnChatMapping ->
            val userIds = userIdOnChatMapping.keys
            userDao.fetchAllUsers(onFetched = { allUsers ->
                val chatsList = allUsers
                        .filter { userIds.contains(it.id) }
                        .map { createChatInfo(it, userIdOnChatMapping) }
                onChatsFetched(chatsList)
            })

        })
    }

    private fun createChatInfo(it: User, userIdOnChatMapping: Map<String, String>) =
            ChatInfo(it.thumbImage, it.name, userIdOnChatMapping[it.id]!!)

}