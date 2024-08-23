package org.manuel.games.tarkovtournament.controllers

import java.util.UUID

interface TournamentCreatedResponseDto

data class TournamentCreatedSuccessfulResponse(val id: UUID): TournamentCreatedResponseDto

data class TournamentCreatedException(val error: String) : TournamentCreatedResponseDto

interface TournamentFinishedResponseDto

data class TournamentFinishedSuccessfulResponse(val id: UUID): TournamentFinishedResponseDto

data class TournamentFinishedException(val error: String) : TournamentFinishedResponseDto
