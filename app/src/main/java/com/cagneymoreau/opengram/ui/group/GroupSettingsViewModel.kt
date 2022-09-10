package com.cagneymoreau.opengram.ui.group

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import javax.inject.Inject

class GroupSettingsViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
) : ViewModel() {



    val members = listOf<UserInterface>()
    var chat: ChatInterface? = null



    var title = mutableStateOf("")
    var description  = mutableStateOf("")

    var action: (Long) -> Unit = {}


    fun save()
    {
        // TODO:  
    }



    fun getGroupDetails(id:Long, a: (Long) -> Unit)
    {
        chat = repositoryInterface.getChat(id)
        action = a

    }

    fun chooseImage()
    {

    }

    fun updateTitle(t: String)
    {
        title.value = t
    }

    fun updateDescription(d: String)
    {
        description.value = d
    }




}