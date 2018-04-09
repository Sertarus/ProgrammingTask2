package transpose;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TransposeLauncherTest {
    @Test
    void main() throws IOException{
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] args = {"transpose", "src/test/java/filesForTests/1.txt"};
        new TransposeLauncher();
        TransposeLauncher.main(args);
        assertEquals("1 4 7\r\n2 5 8\r\n3 6",outContent.toString());
        outContent.reset();

        String[] args1 = {"transpose","-a","5","-t","-r", "src/test/java/filesForTests/2.txt"};
        System.setOut(new PrintStream(outContent));
        new TransposeLauncher();
        TransposeLauncher.main(args1);
        assertEquals("12345     4     7\r\n    2     5     8\r\n    3     6",outContent.toString());





    }
}
