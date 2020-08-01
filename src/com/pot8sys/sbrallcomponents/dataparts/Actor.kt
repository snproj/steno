package com.pot8sys.sbrallcomponents.dataparts

import com.pot8sys.sbrallcomponents.backManager

val nullActor: Actor = Actor("NOTYPE", "NOREALNAME");

data class Actor (override val type: String, val realName: String, var members: MutableList<Actor> = mutableListOf()) : MasterDatapart() {
    override val designation: String? = backManager.generateDesignation(this);

    fun printingDescription(): String {
        var membersString: String = "";

        if (members.size > 0) {
            membersString += " HAS MEMBERS: ";
            for (index in 0 until (members.size - 1)) {
                membersString += "${members[index].realName}, "
            }
            membersString += "${members.last().realName}."
        }

        val actorString: String = "[$designation] $realName$membersString";

        return actorString;
    }

    fun toBriefDescription(): String {
        var membersString: String = "";

        if (members.size > 0) {
            membersString += " [";
            for (index in 0 until (members.size - 1)) {
                membersString += "${members[index].realName}, "
            }
            membersString += "${members.last().realName}]"
        }

        val actorString: String = "$realName$membersString";

        return actorString;
    }

    override fun toString(): String {
        var membersString: String = "";

        if (members.size > 0) {
            membersString += " with MEMBERS: ";
            for (index in 0 until (members.size - 1)) {
                membersString += "${members[index].realName}, "
            }
            membersString += "${members.last().realName}"
        }

        val actorString: String = "<$designation: $realName$membersString>";

        return actorString;
    }
}

