package com.tsypk.coreclient.service.presenting.file

import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneModel

interface IphoneTablePresenter {
    fun majorHeader(): String
    fun minorHeader(model: IphoneModel): String
    fun row(iphone: Iphone): String
}
