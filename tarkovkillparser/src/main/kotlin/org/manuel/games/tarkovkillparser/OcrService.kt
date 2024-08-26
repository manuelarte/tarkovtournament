package org.manuel.games.tarkovkillparser

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import java.awt.image.BufferedImage

interface OcrService {
    fun parseImg(image: BufferedImage): String

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

    override fun parseRaidMetadata(image: BufferedImage): RaidMetadata = RaidMetadata.from(this.doOCR(image))

    private fun doOCR(image: BufferedImage): String = this.instance.doOCR(image)
}

sealed class ParseKillException(
    message: String,
) : Exception(message)

class ParseRaidMetadataException(
    ocrOutput: String,
    reason: String,
) : ParseKillException(
        "Can't parse the raid info (ocrOutput: '$ocrOutput', reason: '$reason')",
    )
