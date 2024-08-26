@file:Suppress("ktlint:standard:filename")

package org.manuel.games.tarkovkillparser

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import org.manuel.games.tarkovkillparser.utils.CropKillListByField
import org.manuel.games.tarkovkillparser.utils.KillEntryField
import org.manuel.games.tarkovkillparser.utils.bitwiseNot
import org.manuel.games.tarkovkillparser.utils.cropNextBack
import org.manuel.games.tarkovkillparser.utils.cropPlayerKillsTable
import org.manuel.games.tarkovkillparser.utils.cropRaidMetadata
import org.manuel.games.tarkovkillparser.utils.toBufferedImage
import org.opencv.core.Mat

class KillListScreenParser(
    /** Kill List Screenshot in Grayscale */
    private val img: Mat,
    private val ocrService: OcrService,
) {
    private val detector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.SPANISH).build()
    private val lang: String

    init {
        require(!img.empty()) { "Img must not be empty!" }
        require(img.channels() == 1) { "Img must be in grayscale" }
        this.lang = this.parseLang()
    }

    fun parse(): PlayerKillRaidInfo {
        val raidMetadata = this.parseRaidMetadata()
        val cropKillListByField = CropKillListByField(this.img.cropPlayerKillsTable())
        for (n in 1..9) {
            val killEntryImg = cropKillListByField.imgKillEntry(n)
            KillEntryField.entries.forEach { field ->
                val img = cropKillListByField.imgKillEntryForField(killEntryImg, field)
                val output = this.ocrService.parseImg(img.toBufferedImage())
                println("$n - $field: $output")
            }
        }
        // parse raid id
        // parse kills
        return PlayerKillRaidInfo(listOf(), raidMetadata)
    }

    private fun parseLang(): String {
        val nextBack = this.img.cropNextBack().also { it.bitwiseNot() }
        val output = this.ocrService.parseImg(nextBack.toBufferedImage())
        val detectedLanguage =
            this.detector
                .detectLanguageOf(text = output)
        return detectedLanguage.isoCode639_1.name
    }

    private fun parseRaidMetadata(): RaidMetadata {
        val raidMetadata =
            this.img.cropRaidMetadata()
        return this.ocrService.parseRaidMetadata(raidMetadata.toBufferedImage())
    }
}
