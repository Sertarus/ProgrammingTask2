package transpose;

import java.io.*;
import java.util.*;

public class Transpose {

    public ArrayList<ArrayList<String>> toTranspose(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            List<String[]> ReceivedStrings = new ArrayList<>();
            String currentString = reader.readLine();
            while (currentString != null && !currentString.contentEquals("")) {
                ReceivedStrings.add(currentString.trim().split(" +"));
                currentString = reader.readLine();
            }
            int resultHeight = 0;
            for (String[] element : ReceivedStrings) {
                if (element.length > resultHeight) {
                    resultHeight = element.length;
                }
            }
            ArrayList<ArrayList<String>> result = new ArrayList<>();
            for (int i = 0; i < resultHeight; i++) {
                result.add(new ArrayList<>());
            }
            for (ArrayList<String> list : result) {
                for (int i = 0; i < ReceivedStrings.size(); i++) {
                    list.add("");
                }
            }
            for (int i = 0; i < ReceivedStrings.size(); i++) {
                for (int j = 0; j < ReceivedStrings.get(i).length; j++) {
                    result.get(j).set(i, ReceivedStrings.get(i)[j]);
                }
            }
            for (ArrayList<String> list : result) {
                for (int i = list.size() - 1; list.get(i).contentEquals(""); i--) {
                    list.remove(list.size() - 1);
                }
            }
            return result;
        }
    }

    public ArrayList<ArrayList<String>> toTranspose(String inputName) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(inputName)) {
            return toTranspose(inputStream);
        }
    }
}