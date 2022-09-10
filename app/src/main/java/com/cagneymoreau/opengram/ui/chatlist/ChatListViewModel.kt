package com.cagneymoreau.opengram.ui.chatlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.ui.structure.Searchable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel(), Searchable {


    var unfilteredList: List<ChatInterface> = listOf()
    var easyChatList: MutableState<List<ChatInterface>> = mutableStateOf(listOf())
    override var searchTarget: MutableState<String> = mutableStateOf("")

    init {

        repositoryInterface.getChatsList()

        viewModelScope.launch {

            repositoryInterface.easyChatList.collect{
                    i ->

                    unfilteredList = i
                    if (searchTarget.value.isEmpty()) easyChatList.value = unfilteredList


            }
        }
    }



    override fun query(query: String) {
        searchTarget.value = query
        if (searchTarget.value.isEmpty()) easyChatList.value = unfilteredList
        else{
            easyChatList.value = unfilteredList.filter { it -> it.title.lowercase().contains(query.lowercase()) }
        }

    }






}