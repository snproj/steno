package com.pot8sys.sbrallcomponents

import com.pot8sys.sbrallcomponents.dataparts.*
import java.io.File
import java.lang.Exception

data class Indent(val numIndents: Int = 0, val string: String ="") {
    override fun toString(): String {
        var stringOfIndents = "";
        for (x in 1..numIndents) stringOfIndents += "\t";

        val endString = stringOfIndents + string;

        return endString;
    }
}

val outputFileWriter: OutputFileWriter = OutputFileWriter();

class OutputFileWriter () {
    val outputFile = File("output.txt");

    var showDesignations: Boolean = true;

    fun clearOutputFile() {
        outputFile.writeText("");
    }

    fun writeAllActors(): Unit {
        outputFile.appendText("PARTIES INVOLVED:\n");

        for (actor in storage.actorStorageList) {
            outputFile.appendText(Indent(1, "${actor.toString()}\n").toString());
        }

        outputFile.appendText("\n");
    }

    fun writeAllMaterials(): Unit {
        outputFile.appendText("EVIDENCE INVOLVED:\n");

        for (material in storage.materialStorageList) {
            outputFile.appendText(Indent(1, "${material.toBriefDescription()}\n").toString());
        }

        outputFile.appendText("\n");
    }

    fun writeAllRecordings(): Unit {
        outputFile.appendText("EVENTS INVOLVED:\n");

        for (recording in storage.recordingStorageList) {
            outputFile.appendText(Indent(1, "${recording.toBriefDescription()}\n").toString());
        }

        outputFile.appendText("\n");
    }

    fun writeRootCauses(recordingThreadList: MutableList<MutableList<Recording>>) {
        var threadCount: Int = 1;

        for (recordingThread in recordingThreadList) {
            outputFile.appendText("THREAD: $threadCount\n");

            var recordingCount: Int = 1;

            for (recording in recordingThread) {
                outputFile.appendText(Indent(1, "RECORDING: $recordingCount\n").toString());
                outputFile.appendText(Indent(2,("[${recording.designation}] ${recording.reportersPersonalTruncation}" + "\n\n")).toString());
                //outputFile.appendText(Indent(1, "END OF RECORDING: $recordingCount\n").toString());
                recordingCount++;
            }

            //outputFile.appendText("END OF THREAD: $threadCount\n");
            threadCount++;
        }

        var rootCauses: MutableList<String> = mutableListOf<String>().let{
            for (thread in recordingThreadList) {
                thread[0].designation?.let { it1 -> it.add(it1) };
            }
            it;
        }

        outputFile.appendText("ROOT CAUSES: RECORDINGS $rootCauses\n");
    }

    fun writeAllThreads() {
        var leaves = storageReader.findLeaves();
        var leafCount: Int = 1;

        for (index in 0 until leaves.size) {
            outputFile.appendText("LEAF: $leafCount\n");
            var rootCauses =
                    storageReader.traceRootCause(storage.getRecordingByUserDefinition(
                            leaves[index].userDefinition
                    ) ?: nullRecording);

            writeRootCauses(rootCauses);
            leafCount++;
        }
    }

    fun printCommand(commandStringList: List<String>) {
        val subCommandStringList = commandStringList.drop(1);
        when (commandStringList[0]) {
            "RV" -> startReview(subCommandStringList);
            "A" -> writeActor(subCommandStringList);
            "M" -> writeMaterial(subCommandStringList);
            "R" -> writeRecording(subCommandStringList);
            "T" -> writeThread(subCommandStringList);
            "AT" -> writeAllThreads();
            "I" -> writeInference(subCommandStringList);
            "C" -> writeComparison(subCommandStringList);
            "D" -> writeReportersDivision(subCommandStringList);
        }


    }

    fun startReview(subCommandStringList: List<String>) {
        val divList: List<String> = subCommandStringList[0].split(",").map {it -> it.trim()};
        val divLength: Int = divList[0].toInt();

        val endString = "REPORTER REVIEWS: ${divList[1]}";
        var divString = "";
        val charDiff = if (divLength - endString.length > 0) divLength - endString.length else 0;
        for (j in 1..charDiff) {
            divString += "-";
        }

        outputFile.appendText("/////$divString$endString\\\\\\\\\\\n\n");
        if (subCommandStringList.size > 2) {
            val moreCommands: List<String> = subCommandStringList?.slice(2..subCommandStringList.lastIndex);

            when (subCommandStringList[1]) {
                "A" -> writeActor(moreCommands);
                "M" -> writeMaterial(moreCommands);
                "R" -> writeRecording(moreCommands);
                "RV" -> startReview(moreCommands);
                "I" -> writeInference(moreCommands);
            }
        }
        for (k in 1..endString.length) {
            divString += "-";
        }
        outputFile.appendText("\\\\\\\\\\$divString/////\n\n\n\n\n");
    }

    fun writeActor(subCommandStringList: List<String>) {
        val targetActor = storage.getActorByRealName(subCommandStringList[0]) ?: throw ActorNotFoundException("ACTOR ${subCommandStringList[0]} NOT FOUND");

        outputFile.appendText("ACTOR ${targetActor.printingDescription()}\n\n");

        if (subCommandStringList.size > 2) {
            outputFile.appendText("     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
            val moreCommands: List<String> = subCommandStringList?.slice(2..subCommandStringList.lastIndex);

            when (subCommandStringList[1]) {
                "A" -> writeActor(moreCommands);
                "M" -> writeMaterial(moreCommands);
                "R" -> writeRecording(moreCommands);
                "RV" -> startReview(moreCommands);
                "I" -> writeInference(moreCommands);
            }
        }
    }

    fun writeMaterial(subCommandStringList: List<String>) {
        val targetMaterial = storage.getMaterialByUserDefinition(subCommandStringList[0]) ?: throw MaterialNotFoundException("MATERIAL ${subCommandStringList[0]} NOT FOUND");

        outputFile.appendText("MATERIAL $targetMaterial\n\n");

        if (subCommandStringList.size > 2) {
            outputFile.appendText("     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
            val moreCommands: List<String> = subCommandStringList?.slice(2..subCommandStringList.lastIndex);

            when (subCommandStringList[1]) {
                "A" -> writeActor(moreCommands);
                "M" -> writeMaterial(moreCommands);
                "R" -> writeRecording(moreCommands);
                "RV" -> startReview(moreCommands);
                "I" -> writeInference(moreCommands);
            }
        }
    }

    fun writeRecording(subCommandStringList: List<String>) {
        val targetRecording = storage.getRecordingByUserDefinition(subCommandStringList[0]) ?: throw RecordingNotFoundException("RECORDING ${subCommandStringList[0]} NOT FOUND");

        outputFile.appendText("RECORDING $targetRecording\n\n");

        if (subCommandStringList.size > 2) {
            outputFile.appendText("     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
            val moreCommands: List<String> = subCommandStringList?.slice(2..subCommandStringList.lastIndex);

            when (subCommandStringList[1]) {
                "A" -> writeActor(moreCommands);
                "M" -> writeMaterial(moreCommands);
                "R" -> writeRecording(moreCommands);
                "RV" -> startReview(moreCommands);
                "I" -> writeInference(moreCommands);
            }
        }
    }

    fun writeThread(subCommandStringList: List<String>) {
        val targetRecording = storage.getRecordingByUserDefinition(subCommandStringList[0]) ?: throw RecordingNotFoundException("RECORDING ${subCommandStringList[0]} NOT FOUND");

        val listOfIndividualPaths = storageReader.traceRootCause(targetRecording);

        outputFile.appendText("Investigating the root causes of [${targetRecording.designation}] based on the PERSONAL INFERENCES OF THE REPORTER AS TO THESE EVENTS' PLAUSIBLE CONNECTIONS, we find the below listed ${listOfIndividualPaths.size} THREADS of recordings.\n\nEach THREAD lists its constituent RECORDINGS in CHRONOLOGICAL ORDER as given below.\n");
        outputFile.appendText("______________________________________________________\n");
        writeRootCauses(listOfIndividualPaths);
        outputFile.appendText("______________________________________________________\n\n\n\n\n");
    }

    fun writeInference(subCommandStringList: List<String>) {
        var inferenceString: String = "";
        val referenceDesignationList: MutableList<String> = mutableListOf();

        var moreCommands: MutableList<String> = mutableListOf();
        fun inferenceIterate() {
            for (index in subCommandStringList.indices) {
                val target: MasterDatapart? = storage.run {
                    getActorByRealName(subCommandStringList[index]) ?: getMaterialByUserDefinition(subCommandStringList[index]) ?: getRecordingByUserDefinition(subCommandStringList[index])
                }

                target?.designation?.let{referenceDesignationList.add(it);}
                        ?: run {
                            inferenceString += subCommandStringList[index];
                            for (x in (index + 1) until subCommandStringList.size) {
                                moreCommands.add(subCommandStringList[x]);
                            }
                            return;
                        };
            }
        }
        inferenceIterate();

        val endString: String = "REPORTER INFERS FROM $referenceDesignationList: $inferenceString\n\n\n\n\n";
        outputFile.appendText(endString);
        println("SUB: "+subCommandStringList);
        println("MORECOMMANDS: "+moreCommands);

        if (moreCommands.size > 0) {
            var evenMoreCommands = moreCommands.slice(1 until moreCommands.size)
            outputFile.appendText("     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

            when (moreCommands[0]) {
                "A" -> writeActor(evenMoreCommands);
                "M" -> writeMaterial(evenMoreCommands);
                "R" -> writeRecording(evenMoreCommands);
                "RV" -> startReview(evenMoreCommands);
                "I" -> writeInference(evenMoreCommands);
            }
        }

        /*
        val referenceList: MutableList<String> = mutableListOf<String>().let{
            for (x in 0 until (subCommandStringList.size - 1)) {
                it.add(subCommandStringList[x]);
            }
            it;
        }

        val referenceDesignationList: MutableList<String> = mutableListOf();

        for (reference in referenceList) {
            val target: MasterDatapart = storage.run{
               getActorByRealName(reference) ?: getMaterialByUserDefinition(reference) ?: getRecordingByUserDefinition(reference) ?:
                       throw Exception("$reference NOT FOUND");
            }
            target.designation?.let { referenceDesignationList.add(it) };
        }
        val inferenceString: String = subCommandStringList.last();

        var moreCommands: List<String>;

        val endString: String = "REPORTER INFERS FROM $referenceDesignationList: $inferenceString\n\n\n\n\n";

        if (subCommandStringList.size > 2) {
            outputFile.appendText("     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
            val moreCommands: List<String> = subCommandStringList?.slice(2..subCommandStringList.lastIndex);

            when (subCommandStringList[1]) {
                "A" -> writeActor(moreCommands);
                "M" -> writeMaterial(moreCommands);
                "R" -> writeRecording(moreCommands);
                "RV" -> startReview(moreCommands);
                "I" -> writeInference(moreCommands);
            }
        }

         */
    }

    fun writeComparison(subCommandStringList: List<String>) {
        val referenceStringList: MutableList<String> = mutableListOf();
        val referenceDesignationList: MutableList<String> = mutableListOf();

        for (reference in subCommandStringList) {
            val target: MasterDatapart = storage.run{
                getActorByRealName(reference) ?: getMaterialByUserDefinition(reference) ?: getRecordingByUserDefinition(reference) ?:
                throw Exception("$reference NOT FOUND");
            }
            target.toString()?.let { referenceStringList.add(it) };
            target.designation?.let { referenceDesignationList.add(it) };
        }

        outputFile.appendText("______________________________________________________\n");
        val endString: String = """
            |REPORTER FEELS it relevant and pertinent to PURPOSELY JUXTAPOSE the following ${referenceDesignationList.size} elements: $referenceDesignationList:
            |
            |
            |
        """.trimMargin();
        outputFile.appendText(endString);

        for (index in 0 until referenceStringList.size) {
            outputFile.appendText("""
                |COMPARISON ELEMENT ${index + 1}:
                |${referenceStringList[index]}
                |
                |
                |
            """.trimMargin());
        }
        outputFile.appendText("ELEMENTS COMPARED: $referenceDesignationList\n");
        outputFile.appendText("______________________________________________________\n\n\n\n\n");
    }

    fun writeReportersDivision(subCommandStringList: List<String>) {
        val divLength: Int = subCommandStringList[0].toInt();
        var endString: String;
        for (index in 1 until subCommandStringList.size) {
            endString = if(subCommandStringList[index].isEmpty()) {
                "REPORTER'S DIVISION";
            } else {
                "REPORTER'S DIVISION: ${subCommandStringList[index]}";
            }
            var divString = "";
            val charDiff = if (divLength - endString.length > 0) divLength - endString.length else 0;
            for (j in 1..charDiff) {
                divString += "-";
            }
            outputFile.appendText("$divString$endString\n\n\n\n\n");
        }
    }


}