package org.manuel.games.tarkovtournament.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.manuel.games.tarkovtournament.models.Raid
import java.util.UUID


// CreateTournamentCommand Creates a tournament
// tag::CreateTournamentCommand[]
data class CreateTournamentCommand(
    @TargetAggregateIdentifier val id: UUID,
)  // end::CreateTournamentCommand[]

// RaidCompleteCommand Raid complete for a player in a tournament
// tag::RaidCompleteCommand[]
data class RaidCompleteCommand(
    @TargetAggregateIdentifier val tournamentId: UUID,
    val player: String,
    val raid: Raid,
)  // end::RaidCompleteCommand[]

// CreateTournamentCommand Creates a tournament
// tag::FinishTournamentCommand[]
data class FinishTournamentCommand(
    @TargetAggregateIdentifier val id: UUID,
)
// end::FinishTournamentCommand
