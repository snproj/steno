package com.pot8sys.sbrallcomponents

import com.pot8sys.sbrallcomponents.dataparts.*

var storageReader = StorageReader();

class StorageReader () {

    fun traceRootCause(initialRecording: Recording): MutableList<MutableList<Recording>> {
        var recordingsTreeList: MutableList<Recording> = mutableListOf(initialRecording);
        //var treeBackFlagList: MutableList<Int> = mutableListOf();

        var currentPathList: MutableList<Recording> = mutableListOf();

        var listOfIndividualPaths: MutableList<MutableList<Recording>> = mutableListOf();

        fun recursiveTrace(currentRecording: Recording): Unit {
            //if (currentRecording != initialRecording) treeBackFlagList.add(1);
            var localCurrentPathList: MutableList<Recording> = mutableListOf();

            currentPathList.add(currentRecording);
            println(currentPathList);
            var relatedRecordingsList: List<Recording> = currentRecording.relatedRecordingsList;
            var deadEnd: Boolean = (relatedRecordingsList.isEmpty());
            println(deadEnd);
            if (deadEnd) {
                localCurrentPathList.addAll(currentPathList.reversed());
                listOfIndividualPaths.add(localCurrentPathList);
            }

            println("LIST OF INDIVIDUAL PATHS: $listOfIndividualPaths");
            for (recording in relatedRecordingsList) if (recording.recordingDateTime <= currentRecording.recordingDateTime) {
                recordingsTreeList.add(recording);
                recursiveTrace(recording);
            }

            currentPathList.remove(currentRecording);

            println();

            //if (currentRecording != initialRecording) treeBackFlagList.add(-1);
        }

        recursiveTrace(initialRecording);

        //return listOf(recordingsTreeList, treeBackFlagList);

        return listOfIndividualPaths;

    }

    fun findLeaves(): MutableList<Recording> {
        var endList: MutableList<Recording> = mutableListOf<Recording>().let{
            for(recording in storage.recordingStorageList) it.add(recording);
            it;
        };

        for (currentRecording in storage.recordingStorageList) {
            if (currentRecording.relatedRecordingsList.isEmpty()) endList.remove(currentRecording);
            else {
                for (otherRecording in storage.recordingStorageList) {
                    if (otherRecording.relatedRecordingsList.contains(currentRecording)) endList.remove(currentRecording);
                }
            }
        }

        return endList;
    }
}