package org.manuel.games.tarkovtournament.api

import java.time.Instant
import java.util.UUID

data class TournamentDto(val id: UUID, val createdAt: Instant, val active: Boolean)
