import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class FileWorker implements AutoCloseable {
    private ArrayList<Scanner> scanners = new ArrayList<>();
    private Writer writer;

    public FileWorker(Path outputFile, ArrayList<Path> inputFiles) throws IOException {
        for (int i = 0; i < inputFiles.size(); ++i) {
            try {
                if (Files.isReadable(inputFiles.get(i)))
                    scanners.add(new Scanner(inputFiles.get(i)));
            } catch (IOException ignored) {
            }

        }
        try {
            Files.createFile(outputFile);
        }
        catch (FileAlreadyExistsException ignored) {};
        writer = Files.newBufferedWriter(outputFile);
    }

    public ArrayList<String> readFirstElems(){
        ArrayList<String> res = new ArrayList<>();
        for (Scanner scanner : scanners){
            if (scanner.hasNextLine()){
                res.add(scanner.nextLine());
            }
            else scanners.remove(scanner);
        }
        return res;
    }

    public String getNextElem(int index){
        if (scanners.get(index).hasNextLine()){
            return scanners.get(index).nextLine();
        }
        else scanners.remove(index);
        return null;
    }

    public void write(String elem) throws IOException {
        writer.write(elem);
    }

    public int getScannersSize(){
        return scanners.size();
    }

    @Override
    public void close() throws IOException {
        writer.close();
        scanners.forEach(Scanner::close);
    }
}
