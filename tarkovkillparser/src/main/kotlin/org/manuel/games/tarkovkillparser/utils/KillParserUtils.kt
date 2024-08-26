package org.manuel.games.tarkovkillparser.utils

import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY

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
            val (_, descriptorsBaseImage) =
                Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                    orb.detectAndCompute(baseImage, Mat(), keyPoints, descriptors)
                }
            val (
                _, descriptors) =
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