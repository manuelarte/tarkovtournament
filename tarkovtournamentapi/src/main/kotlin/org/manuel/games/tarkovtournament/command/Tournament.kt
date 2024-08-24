package org.manuel.games.tarkovtournament.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.manuel.games.tarkovtournament.api.CreateTournamentCommand
import org.manuel.games.tarkovtournament.api.FinishTournamentCommand
import org.manuel.games.tarkovtournament.api.RaidCompleteCommand
import org.manuel.games.tarkovtournament.api.RaidCompletedEvent
import org.manuel.games.tarkovtournament.api.TournamentCreatedEvent
import org.manuel.games.tarkovtournament.api.TournamentFinishedEvent
import org.manuel.games.tarkovtournament.models.PlayerTournament
import org.springframework.context.annotation.Profile
import java.util.UUID

@Profile("command")
@Aggregate
class Tournament {
    @AggregateIdentifier
    private lateinit var id: UUID
    private lateinit var createdBy: String
    private var playersTournament: MutableMap<String, PlayerTournament> = mutableMapOf()
    private var isFinished = false

    // Tag this handler to use it as code sample in the documentation
    // tag::CreateTournamentCommand[]
    @CommandHandler
    constructor(command: CreateTournamentCommand) {
        AggregateLifecycle.apply(TournamentCreatedEvent(command.id, command.createdBy))
    }
    // end::CreateTournamentCommand[]

    // Tag this handler to use it as code sample in the documentation
    // tag::RaidCompleteCommand[]
    @CommandHandler
    fun handle(command: RaidCompleteCommand) {
        require(command.player.isNotEmpty()) { "no player name given" }
        AggregateLifecycle.apply(RaidCompletedEvent(command.tournamentId, command.player, command.raid))
    }
    // tag::RaidCompleteCommand[]

    @CommandHandler
    fun handle(command: FinishTournamentCommand) {
        check(!this.isFinished)
        AggregateLifecycle.apply(TournamentFinishedEvent(command.id))
    }

    @EventSourcingHandler
    fun on(event: TournamentCreatedEvent) {
        this.id = event.id
        this.createdBy = event.createdBy
        // rules
    }

    @EventSourcingHandler
    fun on(event: RaidCompletedEvent) {
        val playerTournament = this.playersTournament.getOrPut(event.player) { PlayerTournament() }
        playerTournament.addRaid(event.raid)
    }

    @EventSourcingHandler
    fun on(event: TournamentFinishedEvent) {
        this.isFinished = true
    }

    constructor()
}
