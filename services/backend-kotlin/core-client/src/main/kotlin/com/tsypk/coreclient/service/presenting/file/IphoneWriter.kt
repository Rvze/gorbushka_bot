package com.tsypk.coreclient.service.presenting.file

import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneModel

interface IphoneWriter {
    fun writeIphonesToCsv(
        iphones: Map<IphoneModel, List<Iphone>>,
        filename: String,
        presenter: IphoneTablePresenter,
    )
}
