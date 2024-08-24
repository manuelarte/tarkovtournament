package org.manuel.games.tarkovtournament.query

import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.axonframework.queryhandling.QueryHandler
import org.manuel.games.tarkovtournament.api.*
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Profile("query")
@Service
@ProcessingGroup("tournament")
class TournamentQueryProjector {
    private val tournamentReadModel: MutableMap<UUID, TournamentDto> = ConcurrentHashMap()

    @EventHandler
    fun on(
        event: TournamentCreatedEvent,
        @Timestamp instant: Instant,
    ) {
        val tournament = TournamentDto(event.id, instant, true)
        tournamentReadModel[event.id] = tournament
    }

    @QueryHandler
    fun handle(query: TournamentsQuery): TournamentsResponse {
        return TournamentsResponse(tournamentReadModel.values.filter { t -> t.active == query.active })
    }
}
