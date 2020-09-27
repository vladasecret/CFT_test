import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Function;

public class Configuration {
    private final char type;
    private final Function<String, Comparable<?>> castFunction;
    private boolean ascending;
    private Path outFile;
    private final ArrayList<Path> inputFiles = new ArrayList<>();

    public Configuration(String[] args) {
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();

        for (String arg : args) {
            if (arg.startsWith("-"))
                keys.add(arg);
            else fileNames.add(arg);
        }

        if (keys.contains("-a") && keys.contains("-d"))
            throw new IllegalArgumentException("Too much keys. You can't use -a & -d together");
        if (keys.contains("-i") && keys.contains("-s"))
            throw new IllegalArgumentException("Too much keys. You can't use -i & -s together");
        if (keys.size() > 2)
            throw new IllegalArgumentException("Too much keys");
        if (fileNames.size() < 2)
            throw new IllegalArgumentException("Not enough file names");


        String key = keys.get(0);
        if (keys.size() == 2) {
            switch (key) {
                case "-a":
                    ascending = true;
                    break;
                case "-d":
                    ascending = false;
                    break;
                default:
                    throw new IllegalArgumentException("When you enter 2 keys, the first key have to be -a or -d, the second -i or -s");
            }
            key = keys.get(1);
        }
        if (keys.size() == 1) {
            ascending = true;
        }
        switch (key) {
            case "-i":
                type = 'i';
                castFunction = Integer::parseInt;
                break;
            case "-s":
                type = 's';
                castFunction = x -> x;
                break;
            default:
                if (keys.size() == 1)
                    throw new IllegalArgumentException("When you enter just 1 key it have to be -s or -i");
                else
                    throw new IllegalArgumentException("When you enter 2 keys, the first key have to be -a or -d, the second -i or -s");
        }
        outFile = Paths.get(fileNames.get(0));
        fileNames.remove(0);

        for (String fileName : fileNames) {
            Path tmp = Paths.get(fileName);
            if (Files.exists(tmp) && Files.isReadable(tmp) && Files.isRegularFile(tmp)) {
                inputFiles.add(tmp);
            } else System.out.format("File \"%s\" does not exist or is not readable. It will be ignored.\n", fileName);
        }
        if (inputFiles.size() < 1)
            throw new IllegalArgumentException("No existing or readable input files");

    }

    public ArrayList<Path> getInputFiles() {
        return inputFiles;
    }

    public char getType() {
        return type;
    }

    public Path getOutFile() {
        return outFile;
    }

    public boolean isAscending() {
        return ascending;
    }

    public int signAscending() {
        return (ascending) ? -1 : 1;

    }

    public Function<String, Comparable<?>> getCastFunction() {
        return castFunction;
    }
}
