package org.manuel.games.tarkovtournament.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/** Interface to define how the points are calculated for each kill */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = FactionKillRule::class,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = FactionKillRule::class, name = "byFaction"),
)
interface KillPointCalculator {
    fun points(kill: Kill): Double
}

/** Implementation for KillPointCalculator in which each faction receives different points */
data class FactionKillRule(private val factions: Map<String, Double>, val defaultPoints: Double) : KillPointCalculator {
    override fun points(kill: Kill): Double {
        val key = kill.faction?.lowercase()
        return factions.getOrDefault(key, defaultPoints)
    }
}

/**
 * Interface to define the Rules of the tournament
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = KillingPointsRule::class,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = KillingPointsRule::class, name = "default"),
)
interface Rule {
    /** Applies the rule to the raids and returns the points after applying the rule */
    fun apply(
        player: String,
        raids: Collection<Raid>,
    ): Double
}

/** Rule to be applied to the kills */
interface KillRule : Rule

/** Default implementation for the KillRule */
class KillingPointsRule(private val calculator: KillPointCalculator) : KillRule {
    override fun apply(
        player: String,
        raids: Collection<Raid>,
    ): Double {
        var points = 0.0
        raids.forEach { raid ->
            raid.kills.forEach { kill -> points += calculator.points(kill) }
        }
        return 0.0
    }
}
