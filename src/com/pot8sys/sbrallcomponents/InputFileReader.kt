package com.pot8sys.sbrallcomponents

import com.pot8sys.sbrallcomponents.dataparts.*
import java.io.File

class ParsingComponentException(message: String) : Exception(message)
class ActorNotFoundException(message: String) : Exception(message)
class MaterialNotFoundException(message: String) : Exception(message)
class RecordingNotFoundException(message: String) : Exception(message)

val inputFileReader = InputFileReader();

class InputFileReader () {
    var pathName: String = "input.txt";
    val inputFile = File(pathName);

    fun extractInput(): List<String> {
        val wholeFileString = inputFile.readText(Charsets.UTF_8);
        val stringList: List<String> = wholeFileString.split(";").map {it -> it.trim()};
        return stringList;
    }

    fun parseDirectory(stringList: List<String>): Unit {
        println(stringList);
        for (string in stringList) {
            println(string);
            if (string != "") {
                val commandComponents: List<String> = string.split("\\").map {it -> it.trim()};
                when (commandComponents[0]) {
                    "A" -> parseActor(string);
                    "AA" ->outputFileWriter.writeAllActors();
                    "M" -> parseMaterial(string);
                    "AM" -> outputFileWriter.writeAllMaterials();
                    "R" -> parseRecording(string);
                    "AR" -> outputFileWriter.writeAllRecordings();
                    "P" -> {
                        val commandStringList: MutableList<String> = mutableListOf<String>().let{
                            for (x in 1 until commandComponents.size) {
                                it.add(commandComponents[x]);
                            }
                            it;
                        }
                        outputFileWriter.printCommand(commandStringList);
                    }
                    "C" -> {}; // Do nothing, this is a comment marker
                }
            } else {} // Do nothing
        }
    }

    fun parseActor(string: String): Actor{
        val actorComponents: List<String> = string.split("\\").map {it -> it.trim()};

        if (actorComponents[0] != "A") throw ParsingComponentException("FIRST COMPONENT IS NOT \"A\"");

        val type = actorComponents[1];
        val realName = actorComponents[2];
        var members: MutableList<Actor> = mutableListOf();
        if (actorComponents.size > 3) {
            for (i in 3 until actorComponents.size) {
                storage.getActorByRealName(actorComponents[i])?.let { members.add(it) } ?: throw ActorNotFoundException("ACTOR ${actorComponents[i]} NOT FOUND");
            }
        }
        val newActor = Actor(type, realName, members);
        storage.actorStorageList.add(newActor);
        return newActor;
    }

    fun parseMaterial(string: String): Material {
        val materialComponents: List<String> = string.split("\\").map {it -> it.trim()};
        if (materialComponents[0] != "M") throw ParsingComponentException("FIRST COMPONENT IS NOT \"M\"");

        val type = materialComponents[1];
        val mannerOfCommunication = materialComponents[2];
        val author = storage?.getActorByRealName(materialComponents[3]) ?: throw ActorNotFoundException("ACTOR ${materialComponents[3]} NOT FOUND");
        val reportersPersonalTruncation = materialComponents[4];
        val reportersPersonalDescription = materialComponents[5];
        val fullSource = materialComponents[6];
        val userDefinition = materialComponents[7];
        val newMaterial = Material(type, mannerOfCommunication, author, reportersPersonalTruncation, reportersPersonalDescription, fullSource, userDefinition);
        storage.materialStorageList.add(newMaterial);
        return newMaterial;
    }

    fun parseRecording(string: String): Recording {
        val recordingComponents: List<String> = string.split("\\").map {it -> it.trim()};
        if (recordingComponents[0] != "R") throw ParsingComponentException("FIRST COMPONENT IS NOT \"R\"");

        val parsingRecordingsList: List<String> = recordingComponents[5].split(",").map {it -> it.trim()};

        val rawDateTime: RecordingDateTime = RecordingDateTime(recordingComponents[1]);
        val actor1: Actor = storage.getActorByRealName(recordingComponents[2]) ?: throw ActorNotFoundException("ACTOR ${recordingComponents[2]} NOT FOUND");
        val material: Material = storage.getMaterialByUserDefinition(recordingComponents[3]) ?: throw MaterialNotFoundException("MATERIAL ${recordingComponents[3]} NOT FOUND");
        val actor2: Actor = storage.getActorByRealName(recordingComponents[4]) ?: throw ActorNotFoundException("ACTOR ${recordingComponents[4]} NOT FOUND");
        var relatedRecordingsList: MutableList<Recording> = mutableListOf();

        if (recordingComponents[5] != "NONE") {
            for (recording in parsingRecordingsList) {
                relatedRecordingsList.add(storage.getRecordingByUserDefinition(recording) ?: throw RecordingNotFoundException("RECORDING $recording NOT FOUND"));
            }
        } else {} // Do nothing

        val reportersPersonalTruncation = recordingComponents[6];
        val userDefinition = recordingComponents[7];
        val newRecording = Recording(rawDateTime, actor1, material, actor2, relatedRecordingsList, reportersPersonalTruncation, userDefinition);
        storage.recordingStorageList.add(newRecording);
        return newRecording;
    }
}