package org.manuel.games.tarkovkillparser

import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.Test

class TesseractServiceTest {
    @Test
    fun nextBack() {
        val instance = TesseractService("C:\\Program Files\\Tesseract-OCR\\tessdata")
        val file =
            File(
                this::class.java.classLoader
                    .getResource("ocr/next-back-original.png")!!
                    .toURI(),
            )
        assertEquals("NEXT\nBACK\n", instance.parseNextBack(ImageIO.read(file)))
    }
}
