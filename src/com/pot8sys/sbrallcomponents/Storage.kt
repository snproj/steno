package com.pot8sys.sbrallcomponents

import com.pot8sys.sbrallcomponents.dataparts.*

var storage: Storage = Storage();

class Storage () {
    var actorStorageList: MutableList<Actor> = mutableListOf();
    var materialStorageList: MutableList<Material> = mutableListOf();
    var recordingStorageList: MutableList<Recording> = mutableListOf();

    fun getActorByRealName (realName: String): Actor? {
        for (actor in actorStorageList) {
            if (actor.realName == realName) return actor;
        }

        return null;
    }

    fun getMaterialByUserDefinition (userDefinition: String): Material? {
        for (material in materialStorageList) {
            if (material.userDefinition == userDefinition) return material;
        }

        return null;
    }

    fun getRecordingByUserDefinition (userDefinition: String): Recording? {
        for (recording in recordingStorageList) {
            if (recording.userDefinition == userDefinition) return recording;
        }

        return null;
    }
}