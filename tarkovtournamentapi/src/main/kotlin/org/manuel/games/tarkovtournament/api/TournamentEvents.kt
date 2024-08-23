package org.manuel.games.tarkovtournament.api

import org.manuel.games.tarkovtournament.models.Raid
import java.util.UUID

class TournamentCreatedEvent(val id: UUID)

class RaidCompletedEvent(
    val tournamentId: UUID,
    val player: String,
    val raid: Raid
)