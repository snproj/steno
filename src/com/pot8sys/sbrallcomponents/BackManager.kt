package com.pot8sys.sbrallcomponents

import com.pot8sys.sbrallcomponents.dataparts.*

var backManager = BackManager();

class BackManager () {

    var ActorCount: Int = 0;
    var MaterialCount: Int = 0;
    var RecordingCount: Int = 0;

    fun generateDesignation (datapart: MasterDatapart): String? = when (datapart) {
        is Actor -> {ActorCount++; "A" + ActorCount.toString();}
        is Material -> {MaterialCount++; "M" + MaterialCount.toString();}
        is Recording -> {RecordingCount++; "R" + RecordingCount.toString();}
        else -> null;
    }

}