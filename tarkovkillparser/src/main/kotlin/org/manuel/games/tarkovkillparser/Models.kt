package org.manuel.games.tarkovkillparser

import java.util.SortedSet
import kotlin.time.Duration

enum class Version(val n: String) {
    BETA("Beta Version"),
}

data class RaidMetadata(val versionNumber: String, val version: Version, val raidId: String) {

    companion object {
        fun from(ocrOutput: String): RaidMetadata {
            val splitted = ocrOutput.split(" | ")
            if (splitted.size != 2) {
                throw ParseRaidMetadataException(ocrOutput = ocrOutput, reason = "Can't find left and right part of |")
            }
            val versionAndBeta = splitted[0].trim()
            val index = versionAndBeta.indexOf(" Beta version", ignoreCase = true)
            if (index == -1) {
                throw ParseRaidMetadataException(ocrOutput = ocrOutput, reason = "Can't find Beta Version string")
            }
            return RaidMetadata(versionNumber = versionAndBeta.substring(0, index).replace("@", "0"), version = Version.BETA, raidId = splitted[1].trim())
        }
    }


}

data class PlayerKill(val number: Int, val location: String, val time: Duration?, val player: String?, val level: Int?, val faction: String?, val distance: Double?)

data class PlayerKillRaidInfo(private val kills: Collection<PlayerKill>, val metadata: RaidMetadata)