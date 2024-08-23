package org.manuel.games.tarkovtournament.controllers

import org.axonframework.commandhandling.gateway.CommandGateway
import org.manuel.games.tarkovtournament.api.CreateTournamentCommand
import org.springframework.context.annotation.Profile
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
    fun createTournament(@PathVariable id: UUID): Mono<TournamentCreatedResponseDto> {
        val command = CreateTournamentCommand(id)
        return Mono.fromFuture<TournamentCreatedResponseDto>(commandGateway.send(command))
            .then(Mono.just<TournamentCreatedResponseDto>(TournamentCreatedSuccessfulResponse(id)))
            .onErrorResume { t -> Mono.just(TournamentCreatedException(t)) }
            .timeout(5.seconds.toJavaDuration())
    }
}