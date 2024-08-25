package org.manuel.games.tarkovtournament.models

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONObject
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import kotlin.test.Test
import kotlin.test.assertEquals

class KillPointCalculatorTest {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

    @Test
    fun deserializingTypeDefault() {
        val factionsObject =
            JSONObject().also {
                it.put("scav", 1)
                it.put("pmc", 2)
            }
        val rootObject =
            JSONObject().also {
                it.put("type", "byFaction")
                it.put("factions", factionsObject)
                it.put("defaultPoints", 1)
            }
        val pointCalculator: KillPointCalculator = this.objectMapper.readValue(rootObject.toString())
        val expected = FactionKillRule(factions = mapOf(Pair("scav", 1.0), Pair("pmc", 2.0)), defaultPoints = 1.0)
        assertEquals(pointCalculator, expected)
    }

    @Test
    fun serializingTypeDefault() {
        val pointCalculator: KillPointCalculator =
            FactionKillRule(factions = mapOf(Pair("scav", 1.0), Pair("pmc", 2.0)), defaultPoints = 1.0)
        val factionsObject =
            JSONObject().also {
                it.put("scav", 1)
                it.put("pmc", 2)
            }
        val expected =
            JSONObject().also {
                it.put("type", "byFaction")
                it.put("factions", factionsObject)
                it.put("defaultPoints", 1)
            }
        val actual = JSONObject(this.objectMapper.writeValueAsString(pointCalculator))
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT)
    }

    @Test
    fun roundTrip() {
        val pointCalculator: KillPointCalculator =
            FactionKillRule(factions = mapOf(Pair("scav", 1.0), Pair("pmc", 2.0)), defaultPoints = 1.0)
        val json = objectMapper.writeValueAsString(pointCalculator)
        val myClassRoundTrip = objectMapper.readValue(json, KillPointCalculator::class.java)
        assertEquals(pointCalculator, myClassRoundTrip)
    }
}
