import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration config = new Configuration(args);
            if (config.getType() == 'i')
                new Sorter<Integer>(config).merge();
            else
                new Sorter<String>(config).merge();
        }
        catch (Exception exc){
            System.out.println(exc.getLocalizedMessage());
        }
    }
}
