package org.manuel.games.tarkovkillparser

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import java.awt.image.BufferedImage

interface OcrService {
    fun parseNextBack(image: BufferedImage): String
}

class TesseractService : OcrService {
    private val instance: ITesseract =
        Tesseract().also {
            it.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata")
        }

    override fun parseNextBack(image: BufferedImage): String = this.instance.doOCR(image)
}
