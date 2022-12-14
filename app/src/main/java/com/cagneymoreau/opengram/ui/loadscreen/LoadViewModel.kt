package com.cagneymoreau.opengram.ui.loadscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class LoadViewModel @Inject constructor (
    private val repositoryInterface: RepositoryInterface
        ): ViewModel() {

    var navToLogin = {}
    var navToChatList = {}

        init {
            viewModelScope.launch {

                repositoryInterface.authState.collect{
                    i -> handleAuthStateChanges(i)
                }

            }
        }





    fun handleAuthStateChanges(state: Int)
    {
        when( state){

            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR ->{

                navToLogin()

            }

            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                navToChatList()
            }


        }

    }

    fun setNavActions(login: () -> Unit , chatlist: () -> Unit )
    {
        navToLogin = login
        navToChatList = chatlist
    }


}