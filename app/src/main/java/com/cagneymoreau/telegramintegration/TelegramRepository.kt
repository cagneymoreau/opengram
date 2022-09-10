package com.cagneymoreau.telegramintegration

import android.content.Context
import android.os.Build
import android.util.Log
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.apiinterfaceitems.ContentInterface
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class TelegramRepository : Client.ResultHandler, Client.ExceptionHandler, RepositoryInterface {

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
    override lateinit var mySelf: UserInterface


    //storage of all data objects the api has available
    private val usersFullInfo: ConcurrentMap<Long, UserFullInfo> = ConcurrentHashMap()
    private val basicGroupFullInfo: ConcurrentHashMap<Long, TdApi.BasicGroupFullInfo> = ConcurrentHashMap()
    private val supergroupsFullInfo: ConcurrentMap<Long, SupergroupFullInfo> = ConcurrentHashMap()
    private val groupUpgradeTable: ConcurrentMap<Long, Long> = ConcurrentHashMap()
    private val basicGroups: ConcurrentMap<Long, BasicGroup> = ConcurrentHashMap()
    private val supergroups: ConcurrentMap<Long, Supergroup> = ConcurrentHashMap()
    private val secretChats: ConcurrentMap<Int, SecretChat> = ConcurrentHashMap()
    private val users: ConcurrentMap<Long, User> = ConcurrentHashMap()

    var chatsUnsorted: ConcurrentMap<Long, TelegramChatEntity> = ConcurrentHashMap()



    /**
     * Lifecycle or batching
     */
    private var batchList = mutableListOf<Object>()
    var lastBatchMili: Long = 0
    val delay: Long = 500
    var currentlyProcessing: Boolean = false
    var closing: Boolean = false



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
                whoAmI()
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

    fun whoAmI() {
        telegramClient!!.send(GetMe()) { `object` ->
            val self  = `object` as User
            mySelf = TelegramUserEntity(this, self)
        }
    }

    //endregion


    //region  ----------------------------  result/exception


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


    override fun onException(e: Throwable) {
        Log.e(TAG, "onException: $e", e)
    }


    //endregion



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

                chatsUnsorted[chat.id] = TelegramChatEntity( this, chat)

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
                chat.chatDTO.title = updateChat.title
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
                if (currentHistoryId == updateChat.chatId) {
                    _messageList.add(0, TelegramMessageEntity(this, updateChat.lastMessage!!))
                }

                foundchat.chatDTO.lastMessage = updateChat.lastMessage

                if (updateChat.positions.isNotEmpty()) {
                    removeChat(foundchat)
                    foundchat.chatDTO.positions = updateChat.positions
                    addChat(foundchat)
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
                    foundchat.chatDTO.positions = arrayOf(updateChat.position)
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


    //region----- about

    override fun getAbout(): String {
        return "Opengram is a UI that could potentially overlay other app api as UI.\" +\n" +
                "            \" The goal is a community buildng app that includes chatting, commnity calendar\" +\n" +
                "            \" and hopefully p2p decentralyzed messaging\""
    }

    override fun aboutClickCounter() {
        TODO("Not yet implemented")
    }

    //endregion

    //region ------------------  callog

    //endregion


    //region -----------------------  Chat


    private val _messageList = mutableListOf<TelegramMessageEntity>()
    private  val _viewableMessagesHolder = MutableStateFlow(listOf<TelegramMessageEntity>())
    override val viewableMessages = _viewableMessagesHolder.asStateFlow()

    var messageRecieiver: Client.ResultHandler? = null

    var currentHistoryId: Long = 0
    var loading: Boolean = false
    var indexID: Long = 0

    override fun requestMessages(chatID: Long, amt: Int) {

        if (currentHistoryId != chatID){
            currentHistoryId = chatID
            loading = false
            messageRecieiver = null
            indexID = 0
            _messageList.clear()
            pushList()
        }

        if (loading) return

        loading = true

        if (messageRecieiver == null) {
            messageRecieiver =
                Client.ResultHandler { `object` -> // TODO: 12/28/2021 what ebout errors
                    if (`object`.constructor != Messages.CONSTRUCTOR) {
                        Log.d(TAG, "onResult: $`object`" )
                        return@ResultHandler
                    }
                    val messages = `object` as Messages
                    if (messages.messages != null) {
                        for (i in messages.messages.indices) {
                            _messageList.add(TelegramMessageEntity(this, messages.messages[i]))
                        }
                        pushList()
                    }

                    loading = false
                    if (_messageList.size < amt && messages.messages.isNotEmpty()) {
                        requestMessages(chatID, amt)
                    }


                }
        }


        if (_messageList.size >= 1) {
            indexID = _messageList.get(_messageList.size - 1).messageDto.id
        }

        getMessageHistory(chatID, indexID, amt, messageRecieiver)


    }

    fun getMessageHistory(chatid: Long, messageid: Long, limit: Int, handler: Client.ResultHandler?) {
        telegramClient!!.send(GetChatHistory(chatid, messageid, 0, limit, false), handler)
    }

    private fun pushList()
    {
        val nl = _messageList.toList()
        _viewableMessagesHolder.value = nl
    }


    override fun getChat(id: Long) : TelegramChatEntity
    {
        return chatsUnsorted.get(id)!!
    }

    override fun sendTextMessage(id: Long, mess: String) {

        val options = MessageSendOptions(true, false, MessageSchedulingStateSendAtDate(1))

        val textMess = InputMessageText()

        val textC = FormattedText()
        textC.text = mess
        textMess.text = textC

        sendMessage(id, 0, options, ReplyMarkupRemoveKeyboard(), textMess, this)

    }



    fun sendMessage(
        chat: Long,
        thread: Long,
        options: MessageSendOptions?,
        replyMarkup: ReplyMarkup?,
        content: InputMessageContent?,
        handler: Client.ResultHandler?
    ) {
        telegramClient!!.send(SendMessage(chat, thread, 0, options, replyMarkup, content), handler)
    }

    fun deleteMessage(chat: Long, messId: LongArray?, allUser: Boolean, h: Client.ResultHandler?) {
        if (h == null) {
            telegramClient!!.send(DeleteMessages(chat, messId, allUser), this)
        } else {
            telegramClient!!.send(DeleteMessages(chat, messId, allUser), h)
        }
    }

    fun forwardMessage() {
        // TODO: 6/24/2022
    }

    fun replyMessage() {}



    override fun bottomBarActions(chat: ChatInterface): Pair<List<String>, List<() -> Unit>> {

        val chatT = chatsUnsorted[chat.id]


        if (chatT!!.chatDTO.type.constructor == ChatTypeSupergroup.CONSTRUCTOR) {
            val supergroup = chatT!!.chatDTO.type as ChatTypeSupergroup
            if(supergroup.isChannel){

                // TODO: need to return either join or mute

            }
        }
        return Pair(listOf(), listOf())
    }




    //endregion



    //region ------------------------------------- chatlist


    //This object is the list you will edit
    private val _easyChatList =  mutableListOf<TelegramChatEntity>()
    //This object holds is a wrapper. if youhand it a new object it will call emit
    private val _easyChatListHolder = MutableStateFlow(listOf<TelegramChatEntity>())
    //this object sends out the immutable list
    override val easyChatList = _easyChatListHolder.asStateFlow()


    //this should only be called in one location to avoid resorting too much
    fun sortUpdatedChatLists()
    {
        _easyChatList.sort()

        val nl = _easyChatList.toList()

        _easyChatListHolder.value = nl
    }

    fun addChat(chat: TelegramChatEntity)
    {
        _easyChatList.add(chat)
    }

    fun removeChat(chat: TelegramChatEntity)
    {

        _easyChatList.remove(chat)
    }

    override fun getChatsList()
    {
        telegramClient!!.send(
            GetChats(ChatListMain(), Int.MAX_VALUE)
        ) { `object` ->
            val list = `object` as Chats
            if (list.chatIds.size < 5) {
                getChatsList()
            }

        }
    }



    //endregion


    //region -----------  login

    override fun submitPhoneNumber(phoneNumber: String)
    {
        telegramClient!!.send(SetAuthenticationPhoneNumber(phoneNumber, null), this)
    }

    override fun submitVerificationCode(code: String)
    {
        telegramClient!!.send(CheckAuthenticationCode(code), this)

    }


    //endregion


    //region -------------  general interactions



    fun downloadFile(fileId: Int, priority: Int, handler: Client.ResultHandler?) {
        telegramClient!!.send(DownloadFile(fileId, priority, 0, 0, true), handler)
        //getremotefile
    }



    //endregion


    //region ------- contacts

    override fun getContacts(): List<UserInterface> {

        val list = users.values.toList()

        return List(users.size) { it -> TelegramUserEntity(this, list[it])}
    }

    override fun getUser(id: Long): UserInterface {

        if  (users[id] == null) timber.log.Timber.d("Not working")

        return TelegramUserEntity(this, users[id]!!)
    }

    //endregion

    //region -------- Media Display

    val map: HashMap<Int, ContentInterface> = HashMap()

    override fun setMediaToView(key: Int, value: ContentInterface) {
        map[key] = value
    }

    override fun getMediaToView(key: Int): ContentInterface {
        return map.remove(key)!!
    }

    //endregion

}