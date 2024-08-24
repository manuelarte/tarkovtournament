package org.manuel.games.tarkovtournament.models

import java.util.Objects
import kotlin.time.Duration

/**
 * A Raid in Escape From Tarkov.
 * Contains the information parsed by the raid ended screenshot image.
 */
data class Raid(
    /** The id of the Raid, normally in a format like YCUQK8 */
    val id: String,
    /** The kills done in that raid */
    val kills: Collection<Kill>,
) {
    fun indexKills(): Map<Int, Kill> {
        return this.kills.associateBy { it.number }
    }
}

data class Kill(
    /** The index of the kill */
    val number: Int,
    val time: Duration?,
    val player: String?,
    val level: Int?,
    val faction: String?,
    val weapon: String?,
    val bodyPart: String?,
    /** Distance in metres of the kill */
    val distance: Double?,
) {
    init {
        require(number > 0) {
            "number > 0"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kill

        if (number != other.number) return false
        if (time != other.time) return false
        if (player != other.player) return false
        if (level != other.level) return false
        if (faction != other.faction) return false
        if (weapon != other.weapon) return false
        if (bodyPart != other.bodyPart) return false
        if (distance != other.distance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (player?.hashCode() ?: 0)
        result = 31 * result + (level ?: 0)
        result = 31 * result + (faction?.hashCode() ?: 0)
        result = 31 * result + (weapon?.hashCode() ?: 0)
        result = 31 * result + (bodyPart?.hashCode() ?: 0)
        result = 31 * result + (distance?.hashCode() ?: 0)
        return result
    }
}

class PlayerTournament {
    val raids: MutableCollection<Raid> = mutableListOf()

    fun addRaid(raid: Raid) {
        val existingRaid = this.raids.find { it.id == raid.id }
        if (existingRaid != null) {
            val originalKills = existingRaid.indexKills()
            val newKills = raid.indexKills()
            val totalKills = originalKills + newKills
            val newRaid = Raid(raid.id, totalKills.values)
            raids.remove(existingRaid)
            raids.add(newRaid)
        } else {
            raids.add(raid)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerTournament

        return raids == other.raids
    }

    override fun hashCode(): Int {
        return Objects.hash(raids)
    }
}
