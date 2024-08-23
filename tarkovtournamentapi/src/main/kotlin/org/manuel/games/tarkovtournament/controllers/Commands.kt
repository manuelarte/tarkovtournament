package org.manuel.games.tarkovtournament.controllers

import org.axonframework.commandhandling.gateway.CommandGateway
import org.manuel.games.tarkovtournament.api.CreateTournamentCommand
import org.manuel.games.tarkovtournament.api.FinishTournamentCommand
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("tournaments")
@Profile("api")
class TournamentCommandController(val commandGateway: CommandGateway) {

    @PostMapping("/{id}")
    fun createTournament(@PathVariable id: UUID): Mono<ResponseEntity<TournamentCreatedResponseDto>> {
        val command = CreateTournamentCommand(id)
        return Mono.fromFuture<TournamentCreatedResponseDto>(commandGateway.send(command))
            .then(Mono.just<ResponseEntity<TournamentCreatedResponseDto>>(ResponseEntity.ok(TournamentCreatedSuccessfulResponse(id))))
            .onErrorResume { t -> Mono.just(ResponseEntity.badRequest().body(TournamentCreatedException(t.message!!))) }
            .timeout(5.seconds.toJavaDuration())
    }

    @DeleteMapping("/{id}")
    fun finishTournament(@PathVariable id: UUID): Mono<ResponseEntity<TournamentFinishedResponseDto>> {
        val command = FinishTournamentCommand(id)
        return Mono.fromFuture<TournamentCreatedResponseDto>(commandGateway.send(command))
            .then(Mono.just<ResponseEntity<TournamentFinishedResponseDto>>(ResponseEntity.ok(TournamentFinishedSuccessfulResponse(id))))
            .onErrorResume { t -> Mono.just(ResponseEntity.badRequest().body(TournamentFinishedException(t.message!!))) }
            .timeout(5.seconds.toJavaDuration())
    }
}
