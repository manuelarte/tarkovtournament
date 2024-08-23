package org.manuel.games.tarkovtournament.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.manuel.games.tarkovtournament.api.CreateTournamentCommand
import org.manuel.games.tarkovtournament.api.RaidCompleteCommand
import org.manuel.games.tarkovtournament.api.RaidCompletedEvent
import org.manuel.games.tarkovtournament.api.TournamentCreatedEvent
import org.manuel.games.tarkovtournament.models.PlayerTournament
import java.util.*

@Aggregate
class Tournament {

    @AggregateIdentifier
    private lateinit var id: UUID
    private var playersTournament: MutableMap<String, PlayerTournament> = mutableMapOf()

    // Tag this handler to use it as code sample in the documentation
    // tag::CreateTournamentCommand[]
    @CommandHandler
    constructor(command: CreateTournamentCommand) {
        AggregateLifecycle.apply(TournamentCreatedEvent(command.id))
    }
    // end::CreateTournamentCommand[]

    // Tag this handler to use it as code sample in the documentation
    // tag::RaidCompleteCommand[]
    @CommandHandler
    fun handle(command: RaidCompleteCommand) {
        require(command.player.isNotEmpty()) { "no player name given" }
        // TODO validate that the raid is not already present
        AggregateLifecycle.apply(RaidCompletedEvent(command.tournamentId, command.player, command.raid))
    }
    // tag::RaidCompleteCommand[]

    @EventSourcingHandler
    fun on(event: TournamentCreatedEvent) {
        this.id = event.id
        // rules
    }

    @EventSourcingHandler
    fun on(event: RaidCompletedEvent) {
        val playerTournament = this.playersTournament.getOrPut(event.player){PlayerTournament()}
        playerTournament.addRaid(event.raid)
    }

    constructor()
}