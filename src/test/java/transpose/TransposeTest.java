package transpose;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransposeTest {
    @Test
    public void toTranspose() throws IOException {
        ArrayList<ArrayList<String>> expectedResult = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>() {{
                add("1");
                add("4");
                add("7");
            }});
            add(new ArrayList<String>() {{
                add("2");
                add("5");
                add("8");
            }});
            add(new ArrayList<String>() {{
                add("3");
                add("6");
            }});
        }};
        assertEquals(expectedResult,
                Transpose.toTranspose(new FileInputStream("src/test/java/filesForTests/1.txt")));
    }
}