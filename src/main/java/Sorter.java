import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


public class Sorter<T extends Comparable<T>> {
    private Configuration config;
    private final FileWorker fileWorker;

    public Sorter(Configuration conf) throws IOException {
        config = conf;
        fileWorker = new FileWorker(config.getOutFile(), config.getInputFiles());
    }

    public void merge() throws IOException {
        try (fileWorker) {
            ArrayList<T> arrayList = castArrayList(fileWorker.readFirstElems());

            while (fileWorker.getScannersSize() > 0) {
                int index = findMinOrMaxIndex(arrayList);
                fileWorker.write(arrayList.get(index).toString() + "\n");
                T newElem = findNextElem(index, arrayList.get(index));
                if (newElem != null) {
                    arrayList.set(index, newElem);
                } else {
                    arrayList.remove(index);
                }
            }
        }
    }
    private ArrayList<T> castArrayList(ArrayList<String> strings){
        ArrayList<T> res = new ArrayList<>();
        Loop:
        for (int i = 0; i < strings.size(); ++i){
            try{
               res.add((T) config.getCastFunction().apply(strings.get(i)));
            }
            catch (NumberFormatException exception){
                String elem;
                while ((elem = fileWorker.getNextElem(i))!= null){
                    try{
                        res.add((T) config.getCastFunction().apply(elem));
                        continue Loop;
                    }
                    catch (NumberFormatException ignored){}
                }
                strings.remove(i);
                --i;
            }
        }
        return res;
    }


    private T findNextElem(int index, T elem) {
        T newElem = elem;
        int cmp = -config.signAscending();
        while (newElem != null && Math.signum(cmp) != config.signAscending()) {
            try {
                newElem = (T) config.getCastFunction().apply(fileWorker.getNextElem(index));
            } catch (NumberFormatException exception) {
                if (exception.getLocalizedMessage().equals("null"))
                    return null;
                else
                    continue;
            }
            try {
                cmp = elem.compareTo(newElem);
            }
            catch (NullPointerException exception){
                break;
            }

        }
        return newElem;
    }

    private int findMinOrMaxIndex(ArrayList<T> arrayList) {
        return arrayList.
                indexOf(config.isAscending() ? Collections.min(arrayList) : Collections.max(arrayList));
    }
}
