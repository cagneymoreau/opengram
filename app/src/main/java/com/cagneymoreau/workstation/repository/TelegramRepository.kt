package com.cagneymoreau.workstation.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import com.cagneymoreau.workstation.ui.chatlist.ChatDescriptionEntity
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class TelegramRepository : Client.ResultHandler, Client.ExceptionHandler, RepositoryInterface {

    /**
     * Interface with telegram client and and map to output
     *
     */




    /**
     * android
     */
    val TAG: String = "TelegramController"




    /**
     * telegram
     */
    //basic access to telegram controls
    var telegramClient: Client? = null
    var dbDir: String = ""
    var filDir: String = ""
    //var parameters: TdApi.TdlibParameters? = null
    var authorizationState: TdApi.AuthorizationState? = null
    //private val _authStateSharedFlow =  MutableSharedFlow<TdApi.AuthorizationState>()
    //val authSTateSharedFlow = _authStateSharedFlow.asSharedFlow()

    private val _authState = MutableStateFlow(TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR)
    override val authState = _authState.asStateFlow()


    var haveAuthorization: Boolean = false
    var thisUser: TdApi.User? = null

    //storage of all data objects the api has available
    private val usersFullInfo: ConcurrentMap<Long, UserFullInfo> = ConcurrentHashMap()
    private val basicGroupFullInfo: ConcurrentHashMap<Long, TdApi.BasicGroupFullInfo> = ConcurrentHashMap()
    private val supergroupsFullInfo: ConcurrentMap<Long, SupergroupFullInfo> = ConcurrentHashMap()
    private val groupUpgradeTable: ConcurrentMap<Long, Long> = ConcurrentHashMap()
    private val basicGroups: ConcurrentMap<Long, BasicGroup> = ConcurrentHashMap()
    private val supergroups: ConcurrentMap<Long, Supergroup> = ConcurrentHashMap()
    private val secretChats: ConcurrentMap<Int, SecretChat> = ConcurrentHashMap()
    private val users: ConcurrentMap<Long, User> = ConcurrentHashMap()
    //var chatsUnsorted: ConcurrentMap<Long, Chat> = ConcurrentHashMap()
    var chatsUnsorted: ConcurrentMap<Long, ChatMapper> = ConcurrentHashMap()


    //these are lists of viewmodels that can be sorted and displayed
    //todo  sort need to be called on these after each update

    //
    //private val _easyChatList = MutableStateFlow(easyChatListr)
    //override val easyChatList = _easyChatList.asStateFlow()



    //This object is the list you will edit
    private val _easyChatList =  mutableListOf<ChatDescriptionEntity>()
    //This object holds is a wrapper. if youhand it a new object it will call emit
    private val _easyChatListHolder = MutableStateFlow(listOf<ChatDescriptionEntity>())
    //this object sends out the immutable list
    override val easyChatList = _easyChatListHolder.asStateFlow()

    //private val _easyChatlist = mutableListOf<ChatDescriptionEntity>()
    //override val easyChatList: MutableList<ChatDescriptionEntity> = mutableListOf<ChatDescriptionEntity>()


    private  val _viewableMessages = MutableStateFlow(mutableListOf<TdApi.Message>())
    override val viewableMessages = _viewableMessages.asStateFlow()


    var channChatList = mutableListOf<Chat>()
    var groupChatList = mutableListOf<Chat>()
    var privateChatList = mutableListOf<Chat>()

    /**
     * Lifecycle or batching
     */
    private var batchList = mutableListOf<Object>()
    var lastBatchMili: Long = 0
    val delay: Long = 500
    var currentlyProcessing: Boolean = false
    var closing: Boolean = false
    var liveChatId: Long = 0


    //region ---------------------------   config and lifecycl


    fun configure(activityContext: Context)
    {

        dbDir = activityContext.applicationContext.filesDir.absolutePath.toString() + "/"
       filDir = activityContext.applicationContext.getExternalFilesDir(null)!!
            .absolutePath.toString() + "/"


        lastBatchMili = System.currentTimeMillis()

        Client.execute(SetLogVerbosityLevel(0))

        telegramClient = Client.create(this, this, this)

    }


    fun Dissconnect()
    {
        closing = true;
        telegramClient!!.close()

    }


    //endregion


    //region ------------------------------- telegram authorizaton

    // TODO: We need to check at app startup if we can log in and of not navigate there and fill in viewmodel

    private fun onAuthorizationStateUpdate(`object`: Object) {
        val auth = (`object` as UpdateAuthorizationState).authorizationState
        if (auth != null) {
            _authState.value = auth.constructor
        }
        when (_authState.value) {
            AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                val parameters = TdlibParameters()
                parameters.databaseDirectory = dbDir
                parameters.filesDirectory = filDir
                try {
                    parameters.apiId = 9885367 //Integer.valueOf(Utilities.getProperty("api_id", this));
                    parameters.apiHash = "91e4215dc7a35faf0881579226f49653" //Utilities.getProperty("api_hash", this);
                } catch (e: Exception) {
                    Log.e(TAG, "onAuthorizationStateUpdate: ", e)
                }

                //parameters.databaseDirectory
                parameters.useMessageDatabase = true
                parameters.useSecretChats = true
                parameters.systemLanguageCode = "en-US"
                parameters.applicationVersion = "1.0"
                val reqString = (Build.MANUFACTURER
                        + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                        + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)
                parameters.deviceModel = reqString
                telegramClient!!.send(SetTdlibParameters(parameters), this)
            }
            AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> telegramClient!!.send(
                CheckDatabaseEncryptionKey(),
                this
            )
            AuthorizationStateWaitPhoneNumber.CONSTRUCTOR ->                 //we need to log in for first time
                // TODO: navigate to login screen and pass viewmodel
                Log.d(TAG, "onAuthorizationStateUpdate: ")
            AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                val link =
                    (auth as AuthorizationStateWaitOtherDeviceConfirmation).link
                println("Please confirm this login link on another device: $link")
            }
            AuthorizationStateWaitCode.CONSTRUCTOR -> {
                // TODO: confirm viewing login and update viewmodel
                Log.d(TAG, "onAuthorizationStateUpdate: ") //// TODO: 12/23/2021 remove me/
            }
            AuthorizationStateWaitRegistration.CONSTRUCTOR -> {
                // TODO: 12/22/2021 remove debug info throughout
                val firstName = "f"
                val lastName = "m"
                telegramClient!!.send(RegisterUser(firstName, lastName), this)
            }
            AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                val password = "idk"
                telegramClient!!.send(CheckAuthenticationPassword(password), this)
            }
            AuthorizationStateReady.CONSTRUCTOR -> {
                haveAuthorization = true
                // TODO: navigate to home screen
            }
            AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                haveAuthorization = false
                // TODO: 12/22/2021 need to make sure errors are covered in thie log in process such as wrong #, access or api failure
                Log.d(TAG, "onAuthorizationStateUpdate: logging out")
            }
            AuthorizationStateClosing.CONSTRUCTOR -> {
                haveAuthorization = false
                Log.d(TAG, "onAuthorizationStateUpdate: Closing")
            }
            AuthorizationStateClosed.CONSTRUCTOR -> {
                Log.d(TAG, "onAuthorizationStateUpdate: Closed")
                if (!closing) {
                    telegramClient =
                        Client.create(this, this, this) // recreate client after previous has closed
                }
            }
            else -> Log.e(
                TAG,
                "onAuthorizationStateUpdate: $auth"
            )
        }
    }



    //endregion


    //region  ----------------------------  updates


    override fun onResult(`object`: Object) {
        if (`object`.constructor == UpdateAuthorizationState.CONSTRUCTOR) {
            onAuthorizationStateUpdate(`object`)
        } else if (`object`.constructor == UpdateNewCallSignalingData.CONSTRUCTOR) {
            // TODO: 6/21/2022
        } else {
            batchList.add(`object`)
        }

        //onuithreadmethod

        if (!currentlyProcessing && System.currentTimeMillis() > lastBatchMili + delay) {
            currentlyProcessing = true

            // TODO: This required runonui thread but now should not
                while (batchList.size > 0) {
                    performBatch(batchList.removeAt(0))

                sortUpdatedChatLists()
                lastBatchMili = System.currentTimeMillis()
                currentlyProcessing = false
            }
        }
    }

    //endregion


    //region -------------- telegram getter/setters and callbacks


    override fun onException(e: Throwable) {
        Log.e(TAG, "onException: $e", e)
    }


    //region -------------  updates
    private fun performBatch(`object`: Object?) {
        if (`object` == null) {
            Log.e(TAG, "performBatch: NUll OBJECT TO PROCESS", null)
            return
        }
        when (`object`.constructor) {
            UpdateAuthorizationState.CONSTRUCTOR -> Log.e(
                TAG,
                "performBatch: shouldnt happen",
                null
            )
            UpdateUser.CONSTRUCTOR -> {
                val updateUser = `object` as UpdateUser
                users[updateUser.user.id] = updateUser.user
            }
            UpdateUserStatus.CONSTRUCTOR -> {
                val updateUserStatus = `object` as UpdateUserStatus
                val user = users[updateUserStatus.userId]
                // FIXME: 2/14/2022 null?
                if (user != null) {
                    synchronized(user) { user.status = updateUserStatus.status }
                }
            }
            UpdateBasicGroup.CONSTRUCTOR -> {
                val updateBasicGroup = `object` as UpdateBasicGroup
                basicGroups[updateBasicGroup.basicGroup.id] = updateBasicGroup.basicGroup
                //Log.d(TAG, "performBatch: upgrade to " + updateBasicGroup.basicGroup.id + " - "  + updateBasicGroup.basicGroup.upgradedToSupergroupId);
                groupUpgradeTable[updateBasicGroup.basicGroup.upgradedToSupergroupId] =
                    updateBasicGroup.basicGroup.id
            }
            UpdateSupergroup.CONSTRUCTOR -> {
                val updateSupergroup = `object` as UpdateSupergroup
                supergroups[updateSupergroup.supergroup.id] = updateSupergroup.supergroup
            }
            UpdateSecretChat.CONSTRUCTOR -> {
                val updateSecretChat = `object` as UpdateSecretChat
                secretChats[updateSecretChat.secretChat.id] = updateSecretChat.secretChat
            }
            UpdateNewChat.CONSTRUCTOR -> {
                val updateNewChat = `object` as UpdateNewChat
                val chat = updateNewChat.chat
                //dont add to the sorted list until we have sorting information

                chatsUnsorted[chat.id] = ChatMapper(chat, createListViewModel(chat))

                // TODO: 2/18/2022  check for upgrades to supergroup here?

                //if a new chat appears and is an upgraded supergroup remove the old group. the api should adjust position really quickly so dont pass stuff on
                if (chat.type.constructor == ChatTypeSupergroup.CONSTRUCTOR) {
                    val supergroup = chat.type as ChatTypeSupergroup
                    if (groupUpgradeTable.containsKey(supergroup.supergroupId)) {
                        val basicgroupid = groupUpgradeTable[supergroup.supergroupId]!!
                        //controller.checkForUpgrade(basicgroupid, supergroup.supergroupId);
                        val c = chatsUnsorted.remove(basicgroupid)
                        c?.let { removeChat(it) }
                    }
                }
            }
            UpdateChatTitle.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatTitle
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.e(TAG, "performBatch: MISSING CHAT: chattitle", null)
                    return
                }
                chat.updateTitle(updateChat.title)
            }
            UpdateChatPhoto.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatPhoto
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.e(TAG, "performBatch: MISSING CHAT: chatphoto", null)
                    return
                }
                // TODO:      chat.photo = updateChat.photo
            }
            UpdateChatLastMessage.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatLastMessage
                val foundchat = chatsUnsorted[updateChat.chatId]
                if (foundchat == null) {
                    Log.e(TAG, "performBatch: MISSING CHAT last message", null)
                    return
                }

                //check if we are viewing this chat live and need it displayed
                if (liveChatId == updateChat.chatId) {
                    // TODO: need to update viewmodel here for live chat
                    //chatViewModel.newMessage(updateChat.lastMessage!!)
                }


                // TODO:       foundchat.lastMessage = updateChat.lastMessage
                if (updateChat.positions.size != 0) {
                    //easyChatList.remove(foundchat);
                    // TODO:           removeChat(foundchat)
                    // TODO:      foundchat.positions = updateChat.positions
                    //easyChatList.add(foundchat);
                    // TODO:      addChat(foundchat)
                }
            }
            UpdateChatPosition.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatPosition
                if (updateChat.position.list.constructor != ChatListMain.CONSTRUCTOR) {
                    Log.e(TAG, "performBatch: MISSING CHAT: chatposition 1", null)
                    return
                }
                val foundchat = chatsUnsorted[updateChat.chatId]
                if (foundchat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: chatposition 2")
                    return
                }
                //easyChatList.remove(foundchat);
                    removeChat(foundchat)
                     //foundchat.api.positions = arrayOf(updateChat.position)
                    foundchat.updateOrder(updateChat.position)
                //easyChatList.add(foundchat);
                    addChat(foundchat)
            }
            UpdateChatReadInbox.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatReadInbox
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: chatreadinbox2")
                    return
                }
                // TODO:     chat.lastReadInboxMessageId = updateChat.lastReadInboxMessageId
                // TODO:      chat.unreadCount = updateChat.unreadCount
            }
            UpdateChatReadOutbox.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatReadOutbox
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: chat read outbox")
                    return
                }
                // TODO:     chat.lastReadOutboxMessageId = updateChat.lastReadOutboxMessageId
            }
            UpdateChatUnreadMentionCount.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatUnreadMentionCount
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: unread mentioncount")
                    return
                }
                // TODO:      chat.unreadMentionCount = updateChat.unreadMentionCount
            }
            UpdateMessageMentionRead.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateMessageMentionRead
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: mention read")
                    return
                }
                // TODO:     chat.unreadMentionCount = updateChat.unreadMentionCount
            }
            UpdateChatReplyMarkup.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatReplyMarkup
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: reply markup")
                    return
                }
                // TODO:    chat.replyMarkupMessageId = updateChat.replyMarkupMessageId
            }
            UpdateChatDraftMessage.CONSTRUCTOR -> {
                val updateChat = `object` as UpdateChatDraftMessage
                val chat = chatsUnsorted[updateChat.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: chat read outbox")
                    return
                }
                // TODO:     chat.draftMessage = updateChat.draftMessage
                //easyChatList.add(chat);
                // TODO:     addChat(chat)
            }
            UpdateChatPermissions.CONSTRUCTOR -> {
                val update = `object` as UpdateChatPermissions
                val chat = chatsUnsorted[update.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: chat read outbox")
                    return
                }
                // TODO:   chat.permissions = update.permissions
            }
            UpdateChatNotificationSettings.CONSTRUCTOR -> {
                val update = `object` as UpdateChatNotificationSettings
                val chat = chatsUnsorted[update.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: notification settings")
                    return
                }
                // TODO:    chat.notificationSettings = update.notificationSettings
            }
            UpdateChatDefaultDisableNotification.CONSTRUCTOR -> {
                val update = `object` as UpdateChatDefaultDisableNotification
                val chat = chatsUnsorted[update.chatId]
                if (chat == null) {
                    Log.d(TAG, "performBatch: MISSING CHAT: disable notifications")
                    return
                }
                // TODO:     chat.defaultDisableNotification = update.defaultDisableNotification
            }
            UpdateChatIsMarkedAsUnread.CONSTRUCTOR -> {
                val update = `object` as UpdateChatIsMarkedAsUnread
                val chat = chatsUnsorted[update.chatId]
            }
            UpdateChatIsBlocked.CONSTRUCTOR -> {
                val update = `object` as UpdateChatIsBlocked
                val chat = chatsUnsorted[update.chatId]
            }
            UpdateChatHasScheduledMessages.CONSTRUCTOR -> {
                val update = `object` as UpdateChatHasScheduledMessages
                val chat = chatsUnsorted[update.chatId]
            }
            UpdateUserFullInfo.CONSTRUCTOR -> {
                val updateUserFullInfo = `object` as UpdateUserFullInfo
                usersFullInfo[updateUserFullInfo.userId] = updateUserFullInfo.userFullInfo
            }
            UpdateBasicGroupFullInfo.CONSTRUCTOR -> {
                val updateBasicGroupFullInfo = `object` as UpdateBasicGroupFullInfo
                    basicGroupFullInfo.put(
                    updateBasicGroupFullInfo.basicGroupId,
                    updateBasicGroupFullInfo.basicGroupFullInfo
                )
            }
            UpdateSupergroupFullInfo.CONSTRUCTOR -> {
                val updateSupergroupFullInfo = `object` as UpdateSupergroupFullInfo
                supergroupsFullInfo[updateSupergroupFullInfo.supergroupId] =
                    updateSupergroupFullInfo.supergroupFullInfo
            }
            UpdateUnreadChatCount.CONSTRUCTOR -> {}
            UpdateOption.CONSTRUCTOR -> {}
            Error.CONSTRUCTOR -> {}
            Ok.CONSTRUCTOR -> {}
            UpdateCall.CONSTRUCTOR -> {}
            else -> {}
        }
    }


    //endregion


    //region -------------------------------------  helper

    //this should only be called in one location to avoid resorting too much
    fun sortUpdatedChatLists()
    {
        _easyChatList.sort()

        val nl = _easyChatList.toList()

        _easyChatListHolder.value = nl


    }

    fun addChat(chat: ChatMapper)
    {
        _easyChatList.add(chat.entity)
    }

    fun removeChat(chat: ChatMapper)
    {

        _easyChatList.remove(chat.entity)
    }




    //endregion


    //region -------------  interactions

    override fun getChatsList()
    {
        telegramClient!!.send(
            GetChats(ChatListMain(), Int.MAX_VALUE)
        ) { `object` ->
            val list = `object` as Chats
            if (list.chatIds.size < 5) {
                getChatsList()
            }

            //Log.e(TAG, "onResult: " + object.toString(), null);
        }
    }

    override fun submitPhoneNumber(phoneNumber: String)
    {
        telegramClient!!.send(SetAuthenticationPhoneNumber(phoneNumber, null), this)
    }

    override fun submitVerificationCode(code: String)
    {
        telegramClient!!.send(CheckAuthenticationCode(code), this)

    }


}