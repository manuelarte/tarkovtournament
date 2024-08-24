package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
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

val BASE_KILL_IMAGE = Imgcodecs.imread("src/main/resources/base-image-kill-parser.png")!!

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    OpenCV.loadLocally()

    val name = "Kotlin"
    // TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, $name!")

    for (i in 1..5) {
        // TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
}

fun isKillImage(img: Mat): Boolean {
    require(!img.empty()) { "img can't be empty" }
    val bwImg = Mat().apply { Imgproc.cvtColor(img, this, COLOR_BGR2GRAY) }
    val orb =
        ORB.create().also { orb ->
            // Detect keyPoints and compute descriptors
            val (keyPointsBaseImage, descriptorsBaseImage) =
                Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                    orb.detectAndCompute(BASE_KILL_IMAGE, Mat(), keyPoints, descriptors)
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
            println("Similarity: $similarity")
            return similarity > 0.1
        }
/*
    // Draw the top 50 matches
    val subListMatches: Array<DMatch> =
        goodMatches
            .subList(
                0,
                min(50.0, goodMatches.size.toDouble()).toInt(),
            ).toTypedArray()
    Mat().also {
        Features2d.drawMatches(
            BASE_KILL_IMAGE,
            keypointsBaseImage,
            bwImg,
            keypoints2,
            MatOfDMatch(
 *subListMatches,
            ),
            it,
        )
        // Display the matched image
        HighGui.imshow("Matches", it)
        HighGui.waitKey(0)
    }
*/
}
