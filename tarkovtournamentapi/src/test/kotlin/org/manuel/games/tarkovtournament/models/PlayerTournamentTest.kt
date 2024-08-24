package org.manuel.games.tarkovtournament.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

class PlayerTournamentTest {
    @Test
    fun addRaidDifferentRaids() {
        val playerTournament =
            PlayerTournament().also {
                it.addRaid(Raid("12345", listOf()))
                it.addRaid(Raid("54321", listOf()))
            }
        assertEquals(2, playerTournament.raids.size)
    }

    @Test
    fun addRaidSameRaidNoKills() {
        val raidId = "12345"
        val playerTournament =
            PlayerTournament().also {
                it.addRaid(Raid(raidId, listOf()))
                it.addRaid(Raid(raidId, listOf()))
            }
        assertEquals(1, playerTournament.raids.size)
    }

    @Test
    fun addRaidSameRaidDifferentKills() {
        val raidId = "12345"
        val playerTournament =
            PlayerTournament().also {
                it.addRaid(Raid(raidId, listOf(createEmptyKill(1))))
                it.addRaid(Raid(raidId, listOf(createEmptyKill(2))))
            }
        assertEquals(1, playerTournament.raids.size)
        assertEquals(2, playerTournament.raids.iterator().next().kills.size)
    }

    @Test
    fun addRaidSameRaidSameKill() {
        val raidId = "12345"
        val killNumber = 1
        val playerTournament =
            PlayerTournament().also {
                val raid = Raid(raidId, listOf(createEmptyKill(killNumber)))
                it.addRaid(raid)
            }
        val updatedKill = Kill(killNumber, null, "Scav Name", null, "SCAV", null, null, null)
        val raid2 = Raid(raidId, listOf(updatedKill))
        playerTournament.addRaid(raid2)
        assertEquals(1, playerTournament.raids.size)
        assertEquals(1, playerTournament.raids.iterator().next().kills.size)
        assertContains(playerTournament.raids.iterator().next().kills, updatedKill)
    }

    private fun createEmptyKill(number: Int): Kill {
        return Kill(number, null, null, null, null, null, null, null)
    }
}
