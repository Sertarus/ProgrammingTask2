package transpose;

import java.io.*;
import java.util.*;

public class Transpose {

    public List<List<String>> toTranspose(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            List<String[]> receivedStrings = new ArrayList<>();
            String currentString = reader.readLine();
            while (currentString != null && !currentString.contentEquals("")) {
                receivedStrings.add(currentString.trim().split(" +"));
                currentString = reader.readLine();
            }
            int resultHeight = 0;
            for (String[] element : receivedStrings) {
                if (element.length > resultHeight) {
                    resultHeight = element.length;
                }
            }
            List<List<String>> result = new ArrayList<>();
            for (int i = 0; i < resultHeight; i++) {
                result.add(new ArrayList<>());
            }
            for (List<String> list : result) {
                for (int i = 0; i < receivedStrings.size(); i++) {
                    list.add("");
                }
            }
            for (int i = 0; i < receivedStrings.size(); i++) {
                for (int j = 0; j < receivedStrings.get(i).length; j++) {
                    result.get(j).set(i, receivedStrings.get(i)[j]);
                }
            }
            for (List<String> list : result) {
                for (int i = list.size() - 1; list.get(i).contentEquals(""); i--) {
                    list.remove(list.size() - 1);
                }
            }
            return result;
        }
    }
}