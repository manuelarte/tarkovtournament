package org.manuel.games.tarkovkillparser.utils

import org.opencv.core.Core
import org.opencv.core.DMatch
import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgcodecs.Imgcodecs

fun isKillImage(
    baseImage: Mat = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!,
    img: Mat,
): Boolean = areImgSimilar(baseImage, img, 50.0, 0.1)

fun areImgSimilar(
    baseImage: Mat,
    img: Mat,
    minimumDistance: Double = 50.0,
    threshold: Double = 0.1,
): Boolean {
    require(!baseImage.empty()) { "Base Image can't be empty" }
    require(!img.empty()) { "img can't be empty" }
    check(img.channels() == 1) { "img needs to be in grayscale" }
    ORB.create().also { orb ->
        // Detect keyPoints and compute descriptors
        val (_, descriptorsBaseImage) =
            Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                orb.detectAndCompute(baseImage, Mat(), keyPoints, descriptors)
            }
        val (
            _, descriptors,
        ) =
            Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                orb.detectAndCompute(img, Mat(), keyPoints, descriptors)
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
        val goodMatches = matches.filter { it.distance > minimumDistance }
        val similarity = goodMatches.size.toDouble() / matches.size.toDouble()
        return similarity > threshold
    }
}

fun Mat.cropNextBack(): Mat {
    val top = .85
    val bottom = 1.0
    val left = .45
    val right = .55
    return this.cropImage(top, bottom, left, right)
}

fun Mat.cropRaidMetadata(): Mat {
    val top = .95
    val bottom = 1.0
    val left = .0
    val right = .25
    return this.cropImage(top, bottom, left, right)
}

fun Mat.cropPlayerKillsTable(): Mat {
    val top = .18
    val bottom = .73
    val left = .17
    val right = .83
    return this.cropImage(top, bottom, left, right)
}

class ParseKillList(
    /** Cropped image with the table kills */
    private val croppedImage: Mat,
) {
    /** Normalized Height of a kill entry */
    private val killHeight: Double = 0.1

    private val numberWidth: Double = 0.05
    private val locationWidth: Double = 0.17
    private val timeWidth: Double = 0.12
    private val playerWidth: Double = 0.23
    private val levelWidth: Double = 0.04
    private val factionWidth: Double = 0.115

    /**
     * Get the kill number n of the player kill list
     */
    fun imgKillEntry(n: Int): Mat {
        require(n in 1..9) { "1 <= n <= 9" }
        return this.croppedImage.cropImage(n * this.killHeight, (n + 1) * this.killHeight, 0.0, 1.0)
    }

    fun imgKillEntryLocation(killEntry: Mat): Mat {
        val left = numberWidth
        return killEntry.cropImage(0.0, 1.0, left, left + locationWidth)
    }

    fun imgKillEntryTime(killEntry: Mat): Mat {
        val left = numberWidth + locationWidth
        return killEntry.cropImage(0.0, 1.0, left, left + timeWidth)
    }

    fun imgKillEntryPlayer(killEntry: Mat): Mat {
        val left = numberWidth + locationWidth + timeWidth
        return killEntry.cropImage(0.0, 1.0, left, left + playerWidth)
    }

    fun imgKillEntryLevel(killEntry: Mat): Mat {
        val left = numberWidth + locationWidth + timeWidth + playerWidth
        return killEntry.cropImage(0.0, 1.0, left, left + levelWidth)
    }

    fun imgKillEntryFaction(killEntry: Mat): Mat {
        val left = numberWidth + locationWidth + timeWidth + playerWidth + levelWidth
        return killEntry.cropImage(0.0, 1.0, left, left + factionWidth)
    }

    fun imgKillEntryStatus(killEntry: Mat): Mat {
        val left = numberWidth + locationWidth + timeWidth + playerWidth + levelWidth + factionWidth
        return killEntry.cropImage(0.0, 1.0, left, 1.0)
    }
}
