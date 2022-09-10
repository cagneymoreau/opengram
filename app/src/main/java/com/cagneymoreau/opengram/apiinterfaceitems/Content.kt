package com.cagneymoreau.opengram.apiinterfaceitems

sealed class Content {

    object Unknown: Content()
    object Text: Content()
    object Photo: Content()
    object Audio: Content()
    object AudioNote: Content()
    object Video: Content()
    object VideoNote: Content()
    object Document: Content()
    object Sticker: Content()
    object Animation: Content()
    object WebContent:Content()

}