package org.manuel.games.tarkovtournament.command

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.manuel.games.tarkovtournament.api.RaidCompleteCommand
import org.manuel.games.tarkovtournament.api.RaidCompletedEvent
import org.manuel.games.tarkovtournament.api.TournamentCreatedEvent
import org.manuel.games.tarkovtournament.models.Raid
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test


class TournamentTest {

    private lateinit var fixture: FixtureConfiguration<Tournament>

    @BeforeTest
    fun setUp() {
        fixture = AggregateTestFixture(Tournament::class.java)
    }

    @Test
    fun testRaidCompleteCommand() {
        val tournamentId = UUID.randomUUID()
        val player = "manuelarte"
        val raid = Raid("12345", listOf())
        fixture.given(TournamentCreatedEvent(tournamentId))
            .`when`(RaidCompleteCommand(tournamentId, player, raid))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RaidCompletedEvent(tournamentId, player, raid))

    }
}
