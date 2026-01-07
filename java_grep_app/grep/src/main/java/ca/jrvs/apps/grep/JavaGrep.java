package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {
    void process() throws IOException;

    List<File> listFiles(String rootDir);

    List<String> readLines(File inputFile);

    boolean containsPattern(String line);

    void writeToFiles(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String path);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);
}
