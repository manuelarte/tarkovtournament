package org.manuel.games.tarkovtournament.api

data class TournamentsQuery(val active: Boolean, val limit: Int)

data class TournamentsResponse(val tournaments: Collection<TournamentDto>)
