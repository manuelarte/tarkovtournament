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
import org.opencv.imgproc.Imgproc

class KillListScreenParser(
    /** Kill list screenshot in Grayscale */
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
        val cropKillListByField = CropKillListByField(this.img.cropPlayerKillsTable().bitwiseNot())
        val killList: List<PlayerKill> = mutableListOf()
        for (n in 1..9) {
            val killEntryImg = cropKillListByField.imgKillEntry(n)
            val time =
                cropKillListByField
                    .imgKillEntryForField(killEntryImg, KillEntryField.TIME)
                    .let {
                        val resizeImage = Mat(it.height() * 5, it.width() * 5, it.type())
                        val interpolation = Imgproc.INTER_CUBIC
                        Imgproc.resize(it, resizeImage, resizeImage.size(), 0.0, 0.0, interpolation)

                        this.ocrService.parseNumberImg(resizeImage.toBufferedImage())
                    }
            if (time.isEmpty()) break
            val fieldMap =
                mutableMapOf<KillEntryField, String>().also {
                    KillEntryField.entries.forEach { field ->
                        val output =
                            if (field == KillEntryField.NUMBER) {
                                n.toString()
                            } else {
                                val croppedFieldImg = cropKillListByField.imgKillEntryForField(killEntryImg, field)
                                this.ocrService.parseImg(croppedFieldImg.toBufferedImage())
                            }
                        it[field] = output
                    }
                }
            PlayerKill.from(fieldMap)
        }

        return PlayerKillRaidInfo(killList, raidMetadata)
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
