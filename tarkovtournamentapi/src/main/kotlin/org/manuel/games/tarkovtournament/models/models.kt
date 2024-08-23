package org.manuel.games.tarkovtournament.models

import java.util.*
import kotlin.time.Duration

data class Raid(
    val id: String?,
    val kills: List<Kill>
)

data class Kill(
    val number: Int,
    val time: Duration?,
    val player: String?,
    val faction: String?,
    val weapon: String?,
    val bodyPart: String?,
    val distance: Double?,
)

class PlayerTournament {

    private val raids: MutableList<Raid> = mutableListOf()

    fun addRaid(raid: Raid) {
        this.raids.add(raid)
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