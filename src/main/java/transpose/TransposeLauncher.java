package transpose;

import org.kohsuke.args4j.*;

import java.io.*;
import java.util.List;

public class TransposeLauncher {

    @Option(name = "-o", metaVar = "outputName", usage = "Output file name")
    private String outputFileName;

    @Option(name = "-a", metaVar = "num", usage = "Sets space for every word")
    private Integer space;

    @Option(name = "-t", usage = "Cuts the word if it goes beyond the space allocated to it")
    private boolean t;

    @Option(name = "-r", usage = "Sets the alignment of the word to the right in the space allocated to it")
    private boolean r;

    @Argument(usage = "Input file name", metaVar = "inputName")
    private String inputFileName;

    public static void main(String[] args) throws IOException {
        new TransposeLauncher().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("transpose [-a num] [-t] [-r] [-o ofile] [file]");
            parser.printUsage(System.err);
            return;
        }
        if (inputFileName != null && !new File(inputFileName).exists()) {
            throw new IOException("Wrong input file name");
        }
        Transpose transpose = new Transpose();
        InputStream in = new BufferedInputStream(System.in);
        if (inputFileName != null) {
            in = new FileInputStream(inputFileName);
        }
        List<List<String>> result = transpose.toTranspose(in);
        if (outputFileName != null) {
            writeToFile(setOptions(result));
        } else {
            writeToConsole(setOptions(result), System.out);
        }
    }

    private List<List<String>> setOptions(List<List<String>> matrixOfStrings) {
        if (space == null && (t || r)) {
            space = 10;
        } else if (space == null) {
            space = 1;
        }
        if (space <= 0) {
            throw new IllegalArgumentException("Num can not be less than 1");
        }
        for (List<String> listOfStrings : matrixOfStrings) {
            for (int j = 0; j < listOfStrings.size(); j++) {
                if (t) {
                    if (listOfStrings.get(j).length() > space) {
                        listOfStrings.set(j, listOfStrings.get(j).substring(0, space));
                    }
                }
                StringBuilder currentElement = new StringBuilder();
                if (r) {
                    align(currentElement, space - listOfStrings.get(j).length());
                    currentElement.append(listOfStrings.get(j));
                } else {
                    currentElement.append(listOfStrings.get(j));
                    align(currentElement, space);
                }
                String resultString = currentElement.toString();
                if (!resultString.contentEquals("")) {
                    listOfStrings.set(j, resultString);
                }
            }
        }
        return matrixOfStrings;
    }

    private void align(StringBuilder string, int space) {
        while (string.length() < space) {
            string.append(" ");
        }
    }

    private void writeToConsole(List<List<String>> result, OutputStream out) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).size(); j++) {
                    if (!result.get(i).get(j).contentEquals("")) {
                        writer.write(result.get(i).get(j));
                        if (j < result.get(i).size() - 1) {
                            writer.write(" ");
                        }
                    }
                }
                if (i != result.size() - 1) writer.newLine();
            }
        }
    }

    private void writeToFile(List<List<String>> result) throws IOException {
        FileOutputStream writer = new FileOutputStream(outputFileName);
        writeToConsole(result, writer);
    }
}