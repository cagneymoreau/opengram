package com.cagneymoreau.workstation.ui.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagneymoreau.workstation.RouteArgs
import com.cagneymoreau.workstation.repository.RepositoryInterface
import com.cagneymoreau.workstation.ui.chatlist.ChatDescriptionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    private val taskId: String? = savedStateHandle[RouteArgs.USERID]

    val messageList: MutableState<List<TdApi.Message>> = mutableStateOf(mutableListOf())

    init {
        viewModelScope.launch {

            repositoryInterface.viewableMessages.collect{
                    i -> messageList.value = i
            }

        }
    }


}