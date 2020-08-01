package com.pot8sys.sbrallcomponents.dataparts

import com.pot8sys.sbrallcomponents.backManager
import java.time.LocalDateTime

class timeFormatExtentException(message: String) : Exception(message)

val nullRecordingDateTime: RecordingDateTime = RecordingDateTime("0, 0, 0, 0, 0, 0");

class RecordingDateTime(rawDateTime: String) {
    val rawDateTimeList: List<String> = rawDateTime.split(",").map {it -> it.trim()};
    var localDateTime: LocalDateTime? = null;

    val timeFormatExtent: Int = rawDateTimeList.size;

    val year: Int = rawDateTimeList[0].toInt();
    val month: Int? = if (timeFormatExtent >= 2) { try { rawDateTimeList[1].toInt() } catch(e: Exception) { null } } else null;
    val day: Int? = if (timeFormatExtent >= 3) { try { rawDateTimeList[2].toInt() } catch(e: Exception) { null } } else null;
    val hours: Int? = if (timeFormatExtent >= 4) { try { rawDateTimeList[3].toInt() } catch(e: Exception) { null } } else null;
    val minutes: Int? = if (timeFormatExtent >= 5) { try { rawDateTimeList[4].toInt() } catch(e: Exception) { null } } else null;
    val seconds: Int? = if (timeFormatExtent >= 6) { try { rawDateTimeList[5].toInt() } catch(e: Exception) { null } } else null;

    init{
        localDateTime = when(timeFormatExtent) {
            1 -> LocalDateTime.of(year, 1, 1, 1, 1, 1);
            2 -> LocalDateTime.of(year, month as Int, 1, 1, 1, 1);
            3 -> LocalDateTime.of(year, month as Int, day as Int, 1, 1, 1);
            4 -> LocalDateTime.of(year, month as Int, day as Int, hours as Int, 1, 1);
            5 -> LocalDateTime.of(year, month as Int, day as Int, hours as Int, minutes as Int, 1);
            6 -> LocalDateTime.of(year, month as Int, day as Int, hours as Int, minutes as Int, seconds as Int);
            else -> throw timeFormatExtentException("TIME FORMAT EXTENT EXCEPTION: IS $timeFormatExtent");
        }
    }

    override fun toString(): String {
        val dateTimeString: String = when (timeFormatExtent) {
            1 -> "${localDateTime?.year}"
            2 -> "${localDateTime?.year}/${localDateTime?.month}"
            3 -> "${localDateTime?.year}/${localDateTime?.month}/${localDateTime?.dayOfMonth}"
            4 -> "${localDateTime?.year}/${localDateTime?.month}/${localDateTime?.dayOfMonth} at ${localDateTime?.hour}h"
            5 -> "${localDateTime?.year}/${localDateTime?.month}/${localDateTime?.dayOfMonth} at ${localDateTime?.hour}h:${localDateTime?.minute}m"
            6 -> "${localDateTime?.year}/${localDateTime?.month}/${localDateTime?.dayOfMonth} at ${localDateTime?.hour}h:${localDateTime?.minute}m:${localDateTime?.second}s"
            else -> throw timeFormatExtentException("TIME FORMAT EXTENT EXCEPTION: IS $timeFormatExtent");
        }

        return dateTimeString;
    }

    operator fun compareTo(otherRecordingDateTime: RecordingDateTime): Int {
        return (localDateTime?.compareTo(otherRecordingDateTime.localDateTime) ?: -999)
    }
}

val nullRecording: Recording = Recording(nullRecordingDateTime, nullActor, nullMaterial, nullActor, mutableListOf(), "NORPT", "NOUD");

data class Recording(val recordingDateTime: RecordingDateTime, val actor1: Actor, val material: Material, val actor2: Actor, val relatedRecordingsList: List<Recording>, val reportersPersonalTruncation: String, val userDefinition: String) : MasterDatapart() {
    override val designation: String? = backManager.generateDesignation(this);

    override fun toString(): String {

        val recordingString: String = """
            |[$designation] $reportersPersonalTruncation:
            |On $recordingDateTime, [${actor1.designation}: ${actor1.toBriefDescription()}] passed [${material.designation}: ${material.reportersPersonalDescription}] to [${actor2.designation}: ${actor2.toBriefDescription()}].
            |
            |MATERIAL IN QUESTION:
            |$material
        """.trimMargin();

        return recordingString;
    }

    fun toBriefDescription(): String {
        val recordingString: String = "<$designation: $recordingDateTime, $reportersPersonalTruncation>";

        return recordingString;
    }
}