@file:Suppress("ktlint:standard:filename")

package org.manuel.games.tarkovkillparser

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import org.opencv.core.Core
import org.opencv.core.DMatch
import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY

class KillScreenParser(
    private val img: Mat,
    private val ocrService: OcrService,
) {
    private val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.SPANISH).build()
    private val lang: String

    init {
        require(!img.empty()) { "Img must not be empty!" }
        require(img.channels() == 1) { "Img must be in grayscale" }
        this.lang = this.parseLang()
    }

    fun parse(): String {
        val lang = this.parseLang()
        // parse raid id
        // parse kills
        return "MANUEL"
    }

    private fun parseLang(): String {
        val top = .85
        val bottom = 1.0
        val left = .45
        val right = .55
        val nextBack =
            this.img.also {
                val cropped = it.cropImage(top, bottom, left, right)
                cropped.bitwiseNot()
            }
        val output = this.ocrService.parseNextBack(nextBack.toBufferedImage())
        val detectedLanguage =
            this.detector
                .detectLanguageOf(text = output)
        return detectedLanguage.isoCode639_1.name
    }
}

fun isKillImage(
    baseImage: Mat = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!,
    img: Mat,
): Boolean {
    require(!baseImage.empty()) { "Base Image can't be empty" }
    require(!img.empty()) { "img can't be empty" }
    val bwImg = Mat().apply { Imgproc.cvtColor(img, this, COLOR_BGR2GRAY) }
    val orb =
        ORB.create().also { orb ->
            // Detect keyPoints and compute descriptors
            val (keyPointsBaseImage, descriptorsBaseImage) =
                Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                    orb.detectAndCompute(baseImage, Mat(), keyPoints, descriptors)
                }
            val (keyPoints, descriptors) =
                Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                    orb.detectAndCompute(bwImg, Mat(), keyPoints, descriptors)
                }

            // Create a BFMatcher object with Hamming distance as measurement
            val bfMatcher = BFMatcher.create(Core.NORM_HAMMING, false)

            // Match descriptors
            val matches: MutableList<DMatch> =
                MatOfDMatch()
                    .also {
                        bfMatcher.match(descriptorsBaseImage, descriptors, it)
                    }.toList()
                    .also {
                        it.sortWith { m1: DMatch, m2: DMatch ->
                            m1.distance.toDouble().compareTo(m2.distance.toDouble())
                        }
                    }
            val goodMatches = matches.filter { it.distance > 50 }
            val similarity = goodMatches.size.toDouble() / matches.size.toDouble()
            return similarity > 0.1
        }
}
