package com.cagneymoreau.opengram.apiinterfaceitems

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow

interface UserInterface {


    val name: String
    val nameTwo: String
    val nameAlternate: String
    val description: String
    val activeStatus: String
    val image: StateFlow<ImageBitmap>
    val id: Long



}