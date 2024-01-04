package com.tsypk.coreclient.service.presenting.file.csv

import com.tsypk.coreclient.service.presenting.file.IphoneTablePresenter
import com.tsypk.coreclient.service.presenting.file.IphoneWriter
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneModel
import java.io.FileOutputStream

@Service
class IphoneCsvWriter : IphoneWriter {
    override fun writeIphonesToCsv(
        iphones: Map<IphoneModel, List<Iphone>>,
        filename: String,
        presenter: IphoneTablePresenter,
    ) {
        FileOutputStream(filename).apply {
            val writer = bufferedWriter()
            writer.write(presenter.majorHeader())
            writer.newLine()
            iphones.forEach {
                writer.write(presenter.minorHeader(it.key))
                writer.newLine()
                it.value.forEach { iphone ->
                    writer.write(presenter.row(iphone))
                    writer.newLine()
                }
            }
            writer.flush()
        }
    }
}
