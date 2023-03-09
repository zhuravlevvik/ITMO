package FileWalker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Walk {
    public static void main(String[] args) {
        walk(args, false);
    }

    public static void walk(String[] args, boolean isRecursive) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("2 notnull arguments expected");
            return;
        }

        Path inputFile = Walker.stringToPath(args[0]);
        Path outputFile = Walker.stringToPath(args[1]);
        if (inputFile == null || outputFile == null) {
            return;
        }

        if (!Walker.createDirectories(outputFile)) {
            return;
        }


        Walker walker;
        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(inputFile))) {
            try (BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(outputFile))) {
                walker = new Walker(writer, isRecursive);
                String line = reader.readLine();
                while (line != null) {
                    Path p = Walker.stringToPath(line);
                    if (p == null) {
                        walker.printNullHash(line);
                    } else {
                        try {
                            Files.walkFileTree(p, walker);
                        } catch (IOException e) {
                            System.err.println("Some problems with walking through files occurred: " + e.getMessage());
                        } catch (SecurityException e) {
                            System.err.println("FileWalker has no rights to access the file: " + e.getMessage());
                        }
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.err.println("Cannot open output file: " + outputFile + " " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Error with digest algorithm occurred: " + e.getMessage());
            } catch (SecurityException e) {
                System.err.println("Cannot access output file " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Cannot read file: " + inputFile + " " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Cannot access input file " + e.getMessage());
        }
    }
}

