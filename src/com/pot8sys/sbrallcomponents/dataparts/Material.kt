package com.pot8sys.sbrallcomponents.dataparts

import com.pot8sys.sbrallcomponents.backManager

val nullMaterial: Material = Material("NOTYPE", "NOMOC", nullActor,"NORPT", "NORPD", "NOFS", "NOUD");

data class Material (override val type: String, val mannerOfCommunication: String, val author: Actor, val reportersPersonalTruncation: String, val reportersPersonalDescription: String, val fullSource: String, val userDefinition: String) : MasterDatapart() {
    override val designation: String? = backManager.generateDesignation(this);

    override fun toString(): String {
        val materialString: String = """ 
            |[$designation] $reportersPersonalDescription: A $type on/through $mannerOfCommunication by ${author.toBriefDescription()}. SOURCE: $fullSource
            |REPORTER'S PERSONAL TRUNCATION: $reportersPersonalTruncation
        """.trimMargin();

        return materialString;
    }

    fun toBriefDescription(): String {
        val materialString: String = "<$designation: $type through $mannerOfCommunication by ${author.realName}: $reportersPersonalDescription>";

        return materialString;
    }
}