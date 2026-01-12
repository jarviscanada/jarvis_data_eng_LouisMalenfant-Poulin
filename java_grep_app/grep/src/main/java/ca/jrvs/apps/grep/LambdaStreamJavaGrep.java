package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaStreamJavaGrep implements JavaGrep {

    private String rootPath;
    private String regex;
    private String outFile;
    final Logger logger = LoggerFactory.getLogger(LambdaStreamJavaGrep.class);

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();

        // Use Stream to process files and lines
        listFiles(rootPath).stream()              // Stream<File>
                .flatMap(file -> {                 // Flatten to Stream<String>
                    try {
                        return readLines(file).stream()
                                .filter(this::containsPattern);
                    } catch (Exception e) {
                        return Stream.empty();
                    }
                })
                .forEach(matchedLines::add);       // Collect matched lines

        writeToFiles(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(rootDir))) {
            return pathStream
                    .filter(Files::isRegularFile)     // Keep only regular files
                    .map(Path::toFile)               // Convert Path to File
                    .collect(Collectors.toList());   // Collect to List
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean containsPattern(String line) {
        return line.matches(regex);  // Using regex pattern matching
    }

    @Override
    public void writeToFiles(List<String> lines) throws IOException {
        // Using lambda with try-with-resources
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(outFile),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String path) {
        this.rootPath = path;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args) {
        if (args.length!=3) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }
        LambdaStreamJavaGrep LambdaStream = new LambdaStreamJavaGrep();
        LambdaStream.setRegex(args[0]);
        LambdaStream.setRootPath(args[1]);
        LambdaStream.setOutFile(args[2]);
        try{
            LambdaStream.process();
        }
        catch(Exception ex){
            LambdaStream.logger.error("Error, unable to process ",ex);
        }
    }
}