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

    public static void main(String[] args) {
        new TransposeLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("transpose [-a num] [-t] [-r] [-o ofile] [file]");
            parser.printUsage(System.err);
            return;
        }
        List<List<String>> result;
        if (inputFileName == null) {
            try {
                result = Transpose.toTranspose(System.in);
            } catch (IOException e) {
                System.err.print("Reading error: " + e.getMessage());
                return;
            }
        } else {
            try (InputStream in = new FileInputStream(inputFileName)) {
                result = Transpose.toTranspose(in);
            } catch (FileNotFoundException e) {
                System.err.print("Wrong input file name");
                return;
            } catch (IOException e) {
                System.err.print("Reading from the file error: " + e.getMessage());
                return;
            }
        }
        if (setOptions(result) == null) return;
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
            System.err.print("Num can not be less than 1");
            return null;
        }
        for (List<String> listOfStrings : matrixOfStrings) {
            for (int j = 0; j < listOfStrings.size(); j++) {
                if (t) {
                    if (listOfStrings.get(j).length() > space) {
                        listOfStrings.set(j, listOfStrings.get(j).substring(0, space));
                    }
                }
                String resultString;
                if (r) {
                    resultString = align(listOfStrings.get(j), space, true);
                } else {
                    resultString = align(listOfStrings.get(j), space, false);
                }
                if (!resultString.contentEquals("")) {
                    listOfStrings.set(j, resultString);
                }
            }
        }
        return matrixOfStrings;
    }

    private String align(String string, int space, boolean right) {
        if (right) {
            return String.format("%" + space + "s", string);
        } else {
            return String.format("%-" + space + "s", string);
        }
    }

    private void writeToConsole(List<List<String>> result, OutputStream out) {
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
        } catch (IOException e) {
            System.err.println("Output error: " + e.getMessage());
        }
    }

    private void writeToFile(List<List<String>> result) {
        try (FileOutputStream writer = new FileOutputStream(outputFileName)) {
            writeToConsole(result, writer);
        } catch (IOException e) {
            System.err.println("Outputting to file error: " + e.getMessage());
        }
    }
}