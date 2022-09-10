package com.cagneymoreau.opengram.ui.structure

import androidx.compose.runtime.MutableState

interface Searchable {

    var searchTarget: MutableState<String>
    fun query(query: String): Unit


}

