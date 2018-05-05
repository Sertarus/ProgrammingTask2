package transpose;

        import static org.junit.jupiter.api.Assertions.*;

        import org.junit.jupiter.api.Test;

        import java.io.*;

        import org.apache.commons.io.FileUtils;


public class TransposeLauncherTest {
    @Test
    void main() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] args = {"src/test/java/filesForTests/1.txt"};
        TransposeLauncher.main(args);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args1 = {"-a", "5", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args1);
        assertEquals("123456 4     7    \r\n2     5     8    \r\n3     6    ", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args2 = {"-a", "5", "-t", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args2);
        assertEquals("12345 4     7    \r\n2     5     8    \r\n3     6    ", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args3 = {"-a", "5", "-r", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args3);
        assertEquals("123456     4     7\r\n    2     5     8\r\n    3     6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args4 = {"-r", "-t", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args4);
        assertEquals("    123456          4          7\r\n         2          5          8" +
                "\r\n         3          6", outContent.toString());
        outContent.reset();

        System.setOut(new PrintStream(outContent));
        String[] args5 = {"-a", "5", "-t", "-r", "src/test/java/filesForTests/2.txt"};
        TransposeLauncher.main(args5);
        assertEquals("12345     4     7\r\n    2     5     8\r\n    3     6", outContent.toString());
        outContent.reset();

        System.setErr(new PrintStream(outContent, false, "Windows-1251"));
        String[] args7 = {"-123"};
        TransposeLauncher.main(args7);
        assertEquals("\"-123\" is not a valid option\r\n" +
                "transpose [-a num] [-t] [-r] [-o ofile] [file]\r\n" +
                " inputName     : Input file name\r\n" +
                " -a num        : Sets space for every word\r\n" +
                " -o outputName : Output file name\r\n" +
                " -r            : Sets the alignment of the word to the right in the space\r\n" +
                "                 allocated to it (default: false)\r\n" +
                " -t            : Cuts the word if it goes beyond the space allocated to it\r\n" +
                "                 (default: false)\r\n", outContent.toString());
        outContent.reset();

        File test = new File("src/test/java/filesForTests/3.txt");
        String[] args10 = {"-o", "src/test/java/filesForTests/3.txt", "src/test/java/filesForTests/1.txt"};
        TransposeLauncher.main(args10);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", FileUtils.readFileToString(test));
        test.delete();

        ByteArrayInputStream inContent = new ByteArrayInputStream("1 2 3\r\n4 5 6\r\n7 8".getBytes());
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
        String[] args11 = {};
        TransposeLauncher.main(args11);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", outContent.toString());
        outContent.reset();
        inContent.reset();

        System.setIn(inContent);
        String[] args12 = {"-o", "src/test/java/filesForTests/3.txt"};
        TransposeLauncher.main(args12);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6", FileUtils.readFileToString(test));
        test.delete();

        System.setOut(new PrintStream(outContent, false, "Windows-1251"));
        String[] args13 = {"src/test"};
        TransposeLauncher.main(args13);
        assertEquals("Wrong input file name", outContent.toString());
        outContent.reset();

        BufferedInputStream in = new BufferedInputStream(System.in);
        InputStream old = System.in;
        System.setIn(in);
        in.close();
        String[] args14 = {};
        TransposeLauncher.main(args14);
        assertEquals("Reading error: Stream closed", outContent.toString());
        System.setIn(old);
        outContent.reset();

        test.createNewFile();
        RandomAccessFile raFile = new RandomAccessFile(test, "rw");
        raFile.getChannel().lock();
        String[] args15 = {"src/test/java/filesForTests/3.txt"};
        TransposeLauncher.main(args15);
        assertEquals("Reading from the file error: " +
                "Процесс не может получить доступ к файлу," +
                " так как часть этого файла заблокирована другим процессом", outContent.toString());
        raFile.close();
        test.delete();
        outContent.reset();

        String[] args16 = {"-a", "-5"};
        TransposeLauncher.main(args16);
        assertEquals("Num can not be less than 1", outContent.toString());
        outContent.reset();
    }
}