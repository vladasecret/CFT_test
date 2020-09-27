import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Function;

public class Configuration {
    private Class elemType;
    private final Function<String,Comparable<?>> castFunction;
    private boolean ascending;
    private Path outFile;
    private ArrayList<Path> inputFiles = new ArrayList<>();

    public Configuration(String[] args) {
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();

        for (int i = 0; i < args.length; ++i){
            if (args[i].startsWith("-"))
                keys.add(args[i]);
            else fileNames.add(args[i]);
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
        if (keys.size() == 2){
            switch (key){
                case "-a"->{
                    ascending = true;
                    break;
                }
                case  "-d" ->{
                    ascending = false;
                    break;
                }
                default -> {
                    throw new IllegalArgumentException("When you enter 2 keys, the first key have to be -a or -d");
                }
            }
            key = keys.get(1);
        }
        if (keys.size() == 1){
            ascending = true;
        }
        switch (key){
            case "-i"->{
                elemType = Integer.class;
                castFunction = Integer::parseInt;
                break;
            }
            case  "-s"->{
                elemType = String.class;
                castFunction = x -> x;
                break;
            }
            default ->{
                throw new IllegalArgumentException("When you enter just 1 key it have to be -s or -i");
            }
        }
        outFile = Paths.get(fileNames.get(0));
        fileNames.remove(0);

        for (String fileName : fileNames){
            Path tmp = Paths.get(fileName);
            if (Files.exists(tmp) && Files.isRegularFile(tmp)){
                inputFiles.add(tmp);
            }
            else System.err.format("File \"%s\" does not exist. This file will be ignored.\n", fileName);
        }
        if (inputFiles.size() < 1)
            throw new IllegalArgumentException("No existing input files");

    }

    public ArrayList<Path> getInputFiles() {
        return inputFiles;
    }

    public Class getElemType() {
        return elemType;
    }

    public Path getOutFile() {
        return outFile;
    }

    public boolean isAscending() {
        return ascending;
    }
    public int signAscending(){
        return  (ascending) ? -1 : 1;

    }
    public Function<String,Comparable <?>> getCastFunction(){
        return castFunction;
    }
}
