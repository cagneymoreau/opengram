package com.cagneymoreau.opengram.apiinterfaceitems

import androidx.compose.runtime.MutableState

interface ContentInterface {

    var textContent:String
    val mediaPath: MutableState<String>
    val content: Content
    var url: String
    var description: String

}