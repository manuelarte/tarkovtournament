package org.manuel.games.tarkovtournament.api

data class TournamentsQuery(val active: Boolean, val limit: Int) {
    override fun toString(): String {
        return "TournamentsQuery(active=$active, limit=$limit)"
    }
}

data class TournamentsResponse(val tournaments: Collection<TournamentDto>)
