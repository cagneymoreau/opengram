package com.cagneymoreau.opengram.ui.group

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewGroupViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {

    private val contacts = repositoryInterface.getContacts().toMutableList()
    private val _unSelected = MutableStateFlow(listOf<UserInterface>())
    val unSelected = _unSelected.asStateFlow()

    private val group = mutableListOf<UserInterface>()
    private val _chosen = MutableStateFlow(listOf<UserInterface>())
    val chosen = _chosen.asStateFlow()




    fun choose(user: UserInterface)
    {
        group.add(user)
        contacts.remove(user)
        update()
    }

    fun unchoose(user:UserInterface)
    {
        contacts.add(user)
        group.remove(user)
        update()
    }

    private fun update()
    {
        _chosen.value = group.toList()
        _unSelected.value = contacts.toList()
    }







}