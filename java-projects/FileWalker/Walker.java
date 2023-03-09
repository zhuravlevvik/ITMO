package FileWalker;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Walker implements FileVisitor<Path>{
    private final MessageDigest algorithm;
    private final BufferedWriter writer;
    private final boolean isRecursive;

    private final String NULL_SHA = "0".repeat(64);

    public Walker(BufferedWriter writer, boolean isRecursive) throws NoSuchAlgorithmException {
        this.writer = writer;
        this.isRecursive = isRecursive;
        algorithm = MessageDigest.getInstance("SHA-256");
    }

    private String fileToSHA(Path path) {
        algorithm.reset();
        try (
            DigestInputStream stream = new DigestInputStream(new BufferedInputStream(Files.newInputStream(path)), algorithm)
        ) {
            while (stream.read() != -1) {
                // Do nothing
            }
        } catch (IOException e) {
            System.err.println("Error with digestInputStream occurred: " + e.getMessage());
        }

        return HexFormat.of().formatHex(algorithm.digest());
    }
    private void print(String sha, String path) {
        try {
            writer.write(sha + " " + path + System.getProperty("line.separator"));
        } catch (IOException e) {
            System.err.println("Can't write to output file: " + e.getMessage());
        }
    }

    public void printNullHash(String path) {
        print(NULL_SHA, path);
    }

    public static boolean createDirectories(Path path) {
        try {
            Path parentPath = path.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
        } catch (FileAlreadyExistsException e) {
            System.err.println("Cannot create output file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("An error with output file occurred: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static Path stringToPath(String str) {
        Path p = null;
        try {
            p = Path.of(str);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: " + e.getMessage());
        }
        return p;
    }
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if (!isRecursive) {
            printNullHash(String.valueOf(dir));
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        print(fileToSHA(file), String.valueOf(file));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) {
        if (e == null) {
            printNullHash(String.valueOf(file));
        } else {
            System.err.println("Cannot iterate through file: " + file + " " + e.getMessage());
            printNullHash(String.valueOf(file));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) {
        if (e == null) {
            return FileVisitResult.CONTINUE;
        }
        System.err.println("Directory iteration failed: " + dir + e.getMessage());
        return FileVisitResult.CONTINUE;
    }
}
