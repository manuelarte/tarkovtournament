package org.manuel.games.tarkovkillparser

sealed class ParseKillException(
    message: String,
) : Exception(message)

class ParseRaidMetadataException(
    ocrOutput: String,
    reason: String,
) : ParseKillException(
        "Can't parse the raid info (ocrOutput: '$ocrOutput', reason: '$reason')",
    )

class ParseKillEntryException(
    ocrOutput: String,
    reason: String,
) : ParseKillException(
        "Can't parse the kill entry (ocrOutput: '$ocrOutput', reason: '$reason')",
    )
