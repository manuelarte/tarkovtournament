package org.manuel.games.tarkovtournament.controllers

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.manuel.games.tarkovtournament.api.*
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("tournaments/")
@Profile("api")
class TournamentCommandController(private val commandGateway: CommandGateway, private val queryGateway: QueryGateway) {
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

    @GetMapping
    fun getActiveTournaments(
        @RequestParam(defaultValue = "true") active: Boolean,
    ): ResponseEntity<TournamentsResponse> {
        val response = queryGateway.query(TournamentsQuery(active, 5), TournamentsResponse::class.java).get()
        return ResponseEntity.ok(response)
    }
}
