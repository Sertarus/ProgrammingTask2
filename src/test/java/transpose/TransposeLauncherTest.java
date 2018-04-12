package transpose;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.*;

import org.apache.commons.io.FileUtils;


public class TransposeLauncherTest {
    @Test
    void main() throws IOException {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        String[] args = {"transpose", "src/test/java/filesForTests/1.txt"};
        TransposeLauncher.main(args);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args1 = {"transpose", "-a", "5", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args1);
        assertEquals("123456 4     7    \r\n2     5     8    \r\n3     6    ", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args2 = {"transpose", "-a", "5", "-t", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args2);
        assertEquals("12345 4     7    \r\n2     5     8    \r\n3     6    ", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args3 = {"transpose", "-a", "5", "-r", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args3);
        assertEquals("123456     4     7\r\n    2     5     8\r\n    3     6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args4 = {"transpose", "-r", "-t", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args4);
        assertEquals("    123456          4          7\r\n         2          5          8" +
                "\r\n         3          6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args5 = {"transpose", "-a", "5", "-t", "-r", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args5);
        assertEquals("12345     4     7\r\n    2     5     8\r\n    3     6", outContent.toString());
        outContent.reset();

        String[] args6 = {"123"};
        Assertions.assertThrows(IllegalArgumentException.class, () -> TransposeLauncher.main(args6));

        System.setErr(new PrintStream(outContent));
        String[] args7 = {"transpose", "-123"};
        TransposeLauncher.main(args7);
        assertEquals("\"-123\" is not a valid option\r\n" +
                "transpose [-a num] [-t] [-r] [-o ofile] [file]\r\n" +
                " InputName     : Input file name\r\n" +
                " -a num        : Sets space for every word\r\n" +
                " -o OutputName : Output file name\r\n" +
                " -r            : Sets the alignment of the word to the right in the space\r\n" +
                "                 allocated to it (default: false)\r\n" +
                " -t            : Cuts the word if it goes beyond the space allocated to it\r\n" +
                "                 (default: false)\r\n", outContent.toString());
        outContent.reset();

        String[] args8 = {"transpose", "123"};
        Assertions.assertThrows(IOException.class, () -> TransposeLauncher.main(args8));

        String[] args9 = {"transpose", "-o", "123"};
        Assertions.assertThrows(IOException.class, () -> TransposeLauncher.main(args9));

        File test = new File("src/test/java/filesForTests/3.txt");
        test.createNewFile();
        String[] args10 = {"transpose", "-o", "src/test/java/filesForTests/3.txt", "src/test/java/filesForTests/1.txt"};
        TransposeLauncher.main(args10);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", FileUtils.readFileToString(test));
        test.delete();

        ByteArrayInputStream inContent = new ByteArrayInputStream("1 2 3\r\n4 5 6\r\n7 8".getBytes());
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
        String[] args11 = {"transpose"};
        TransposeLauncher.main(args11);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", outContent.toString());
        outContent.reset();
        inContent.reset();

        test.createNewFile();
        System.setIn(inContent);
        String[] args12 = {"transpose", "-o", "src/test/java/filesForTests/3.txt"};
        TransposeLauncher.main(args12);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", FileUtils.readFileToString(test));
        test.delete();
    }
}
