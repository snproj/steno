import com.pot8sys.sbrallcomponents.*

fun main (args: Array<String>) {

    if (args.isNotEmpty()) {
        val inputFile: String = args[0];
        inputFileReader.pathName = inputFile;
    }

    val stringList = inputFileReader.extractInput();

    outputFileWriter.clearOutputFile();

    inputFileReader.parseDirectory(stringList);

    //println(storage.actorStorageList)
    //println(storage.materialStorageList)
    //println(storage.recordingStorageList)

    //outputFileWriter.writeAllThreads();
}