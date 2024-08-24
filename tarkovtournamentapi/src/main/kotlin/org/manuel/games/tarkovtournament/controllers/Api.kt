package org.manuel.games.tarkovtournament.controllers

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.manuel.games.tarkovtournament.api.CreateTournamentCommand
import org.manuel.games.tarkovtournament.api.FinishTournamentCommand
import org.manuel.games.tarkovtournament.api.TournamentsQuery
import org.manuel.games.tarkovtournament.api.TournamentsResponse
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("tournaments/")
@Profile("api")
class TournamentCommandController(private val commandGateway: CommandGateway) {
    @PostMapping("/{id}")
    fun createTournament(
        @PathVariable id: UUID,
    ): Mono<ResponseEntity<TournamentCreatedResponseDto>> {
        val command = CreateTournamentCommand(id, "Manuelarte")
        return Mono.fromFuture<TournamentCreatedResponseDto>(commandGateway.send(command))
            .then(
                Mono.just<ResponseEntity<TournamentCreatedResponseDto>>(
                    ResponseEntity.ok(
                        TournamentCreatedSuccessfulResponse(id),
                    ),
                ),
            )
            .onErrorResume { t -> Mono.just(ResponseEntity.badRequest().body(TournamentCreatedException(t.message!!))) }
            .timeout(5.seconds.toJavaDuration())
    }

    @DeleteMapping("/{id}")
    fun finishTournament(
        @PathVariable id: UUID,
    ): Mono<ResponseEntity<TournamentFinishedResponseDto>> {
        val command = FinishTournamentCommand(id)
        return Mono.fromFuture<TournamentCreatedResponseDto>(commandGateway.send(command))
            .then(
                Mono.just<ResponseEntity<TournamentFinishedResponseDto>>(
                    ResponseEntity.ok(
                        TournamentFinishedSuccessfulResponse(id),
                    ),
                ),
            )
            .onErrorResume { t ->
                Mono.just(
                    ResponseEntity.badRequest().body(TournamentFinishedException(t.message!!)),
                )
            }
            .timeout(5.seconds.toJavaDuration())
    }
}

@RestController
@RequestMapping("tournaments/")
@Profile("api")
class TournamentQueryController(private val queryGateway: QueryGateway) {
    @GetMapping
    fun getActiveTournaments(
        @RequestParam(defaultValue = "true") active: Boolean,
    ): ResponseEntity<TournamentsResponse> {
        val response = queryGateway.query(TournamentsQuery(active, 5), TournamentsResponse::class.java).get()
        return ResponseEntity.ok(response)
    }
}
