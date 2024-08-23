package org.manuel.games.tarkovtournament.controllers

import java.util.UUID

interface TournamentCreatedResponseDto

data class TournamentCreatedSuccessfulResponse(val id: UUID): TournamentCreatedResponseDto

class TournamentCreatedException : TournamentCreatedResponseDto, Exception {
    constructor ()
    constructor (message: String?)
    constructor (message: String?, cause: Throwable?)
    constructor (cause: Throwable?)
}