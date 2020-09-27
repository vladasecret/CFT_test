import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration(args);
        Sorter sorter = new Sorter(config);

        sorter.merge();

    }
}
