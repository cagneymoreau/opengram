package com.cagneymoreau.workstation.ui.login

import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagneymoreau.workstation.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*
import javax.inject.Inject


// TODO:  i think button 2 needs to be a back button


@HiltViewModel
class LoginViewModel @Inject constructor (
    private val repositoryInterface: RepositoryInterface
    ) : ViewModel() {


    val title: MutableState<String> = mutableStateOf("Please Verify Your Account")
    val description: MutableState<String> = mutableStateOf("loading...")
    val input: MutableState<String> = mutableStateOf("")
    val button1Text: MutableState<String> = mutableStateOf("")
    val button1Action: MutableState<()-> Unit> = mutableStateOf({ })
    val button2Text: MutableState<String> = mutableStateOf("")

    var authState = 0

    var navToChatList = {}

    init {

        viewModelScope.launch {

            repositoryInterface.authState.collect{
                i -> handleAuthStatChange(i)
            }
        }


    }


    private fun handleAuthStatChange(state: Int)
    {
        authState = state

        when(state){

            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR ->{
                description.value = "Enter account phone number"
                button1Text.value = "Submit"
                button1Action.value = {submitPhoneNumber(input.value)}
                // TODO: after submitting phone code it didnt auto delete
            }
            TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                description.value = "waiting..."
            }
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                description.value = "Enter Code"
                button1Action.value = {submitCode(input.value)}
            }
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                navToChatList()
            }

        }
    }


    fun setNavActions(chatlist: () -> Unit )
    {
        navToChatList = chatlist
    }


   fun submitPhoneNumber( num: String)
   {
       //1 (999) 999-9999
       var phoneNumber = num
       phoneNumber = phoneNumber.replace(" ", "")
       phoneNumber = phoneNumber.replace("(", "")
       phoneNumber = phoneNumber.replace(")", "")
       phoneNumber = phoneNumber.replace("-", "")
       phoneNumber = "+$phoneNumber"
       if (phoneNumber.length != 12 || !PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
           description.value =  "Please enter 1 digit country code, 3 digit area code and 7 digit phone number"
           return
       }
       repositoryInterface.submitPhoneNumber(num)
   }


    fun submitCode(code: String)
    {

        repositoryInterface.submitVerificationCode(code)

        val t = Timer()
        t.schedule(object : TimerTask() {
            override fun run() {
                if (authState == TdApi.AuthorizationStateWaitCode.CONSTRUCTOR) {
                    description.value = "incorrect code??"
                }
            }
        }, 1500)

    }

    fun updateText(txt: String)
    {
        input.value = txt
    }

}