package org.manuel.games.tarkovtournament.config

import org.axonframework.config.Configurer
import org.axonframework.config.ConfigurerModule
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.lifecycle.Phase
import org.axonframework.messaging.Message
import org.axonframework.messaging.interceptors.LoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {
    @Bean
    fun loggingInterceptor(): LoggingInterceptor<Message<*>> {
        return LoggingInterceptor()
    }

    @Bean
    fun loggingInterceptorConfigurerModule(loggingInterceptor: LoggingInterceptor<Message<*>?>): ConfigurerModule {
        return LoggingInterceptorConfigurerModule(loggingInterceptor)
    }

    /**
     * An example [ConfigurerModule] implementation to attach configuration to Axon's configuration life cycle.
     */
    private class LoggingInterceptorConfigurerModule(private val loggingInterceptor: LoggingInterceptor<Message<*>?>) :
        ConfigurerModule {
        override fun configureModule(configurer: Configurer) {
            configurer.eventProcessing { processingConfigurer: EventProcessingConfigurer ->
                processingConfigurer.registerDefaultHandlerInterceptor {
                        _: org.axonframework.config.Configuration?,
                        _: String?,
                    ->
                    loggingInterceptor
                }
            }
                .onInitialize { config: org.axonframework.config.Configuration ->
                    this.registerInterceptorForBusses(
                        config,
                    )
                }
        }

        /**
         * Registers the [LoggingInterceptor] on the [org.axonframework.commandhandling.CommandBus],
         * [com.google.common.eventbus.EventBus], [org.axonframework.queryhandling.QueryBus], and
         * [org.axonframework.queryhandling.QueryUpdateEmitter].
         *
         *
         * It does so right after the [Phase.INSTRUCTION_COMPONENTS], to ensure all these infrastructure
         * components are constructed.
         *
         * @param config The [org.axonframework.config.Configuration] to retrieve the infrastructure components
         * from.
         */
        private fun registerInterceptorForBusses(config: org.axonframework.config.Configuration) {
            config.onStart(
                Phase.INSTRUCTION_COMPONENTS + 1,
                Runnable {
                    config.commandBus().registerHandlerInterceptor(loggingInterceptor)
                    config.commandBus().registerDispatchInterceptor(loggingInterceptor)
                    config.eventBus().registerDispatchInterceptor(loggingInterceptor)
                    config.queryBus().registerHandlerInterceptor(loggingInterceptor)
                    config.queryBus().registerDispatchInterceptor(loggingInterceptor)
                    config.queryUpdateEmitter().registerDispatchInterceptor(loggingInterceptor)
                },
            )
        }
    }
}
