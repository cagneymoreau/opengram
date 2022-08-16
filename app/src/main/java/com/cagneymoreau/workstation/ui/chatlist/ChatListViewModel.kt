package com.cagneymoreau.workstation.ui.chatlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cagneymoreau.workstation.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {

    //val easyChatList: MutableList<ChatDescriptionEntity> = repositoryInterface.easyChatList
    var easyChatList: MutableState<List<ChatDescriptionEntity>> = mutableStateOf(listOf())

    init {

        repositoryInterface.getChatsList()

        viewModelScope.launch {

            repositoryInterface.easyChatList.collect{
                    i -> easyChatList.value = i
            }
        }


    }





}