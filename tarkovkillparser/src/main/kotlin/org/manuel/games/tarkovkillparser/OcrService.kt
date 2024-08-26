package org.manuel.games.tarkovkillparser

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import java.awt.image.BufferedImage

interface OcrService {
    fun parseImg(image: BufferedImage): String

    /**
     * Parse number image
     */
    fun parseNumberImg(image: BufferedImage): String

    /**
     * Parse the Raid metadata, the expecting results is something similar to @.15.0.2.32197 Beta version | QWRETY
     */
    fun parseRaidMetadata(image: BufferedImage): RaidMetadata
}

class TesseractService(
    val tessdataLocation: String,
) : OcrService {
    private val instance: ITesseract =
        Tesseract().also {
            it.setDatapath(tessdataLocation)
        }

    override fun parseImg(image: BufferedImage): String = this.doOCR(image)

    override fun parseNumberImg(image: BufferedImage): String {
        val instance =
            Tesseract().also {
                it.setDatapath(tessdataLocation)
            }
        instance.setVariable("tessedit_char_whitelist", "0123456789")
        return instance.doOCR(image)
    }

    override fun parseRaidMetadata(image: BufferedImage): RaidMetadata = RaidMetadata.from(this.doOCR(image))

    private fun doOCR(image: BufferedImage): String = this.instance.doOCR(image)
}
