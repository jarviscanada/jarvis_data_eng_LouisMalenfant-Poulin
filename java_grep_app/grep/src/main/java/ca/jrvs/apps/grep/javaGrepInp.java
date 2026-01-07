package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class javaGrepInp implements JavaGrep {
    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
    private String rootpath;
    private String regex;
    private String outfile;

    @Override
    public void process() throws IOException {
        List<String> matchedLines = Collections.<String>emptyList();
        String rootDir=getRootPath();
        for(File fichier:listFiles(rootDir)) {
            for (String ligne : readLines(fichier)) {
                if (containsPattern(ligne)) {
                    matchedLines.add(ligne);
                }
            }
        }
        writeToFiles(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {

        List<File> files= Collections.<File>emptyList();
        File dir= new File(rootDir);
        Stack<File> stack=new Stack<>();
        stack.push(dir);
        while (!stack.empty()){
            File tmp=stack.pop();
            if(tmp.isDirectory()){
                File[] files1=tmp.listFiles();
                for(File file:files1){
                    stack.push(file);
                }
            }
            else{
                files.add(tmp);
            }
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> ligne=Collections.<String>emptyList();
        try(Scanner reader=new Scanner(inputFile)){
            while(reader.hasNextLine()) {
                ligne.add(reader.nextLine());
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return ligne;
    }

    @Override
    public boolean containsPattern(String line) {
        String regex=getRegex();

        return line.matches(regex);
    }

    @Override
    public void writeToFiles(List<String> lines) throws IOException {
        String file=getOutFile();
        BufferedWriter writer= new BufferedWriter(new FileWriter(file, true));
        for(String line : lines){
            writer.write(line);
            writer.newLine();
        }

    }

    @Override
    public String getRootPath() {
        return rootpath;
    }

    @Override
    public void setRootPath(String path) {
        rootpath=path;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String str) {
        regex=str;
    }

    @Override
    public String getOutFile() {
        return outfile;
    }

    @Override
    public void setOutFile(String outFile) {
        outfile=outFile;
    }

    public static void main(String[] args) {
        if (args.length!=3) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }
        javaGrepInp javaGrepInp = new javaGrepInp();
        javaGrepInp.setRegex(args[0]);
        javaGrepInp.setRootPath(args[1]);
        javaGrepInp.setOutFile(args[2]);
        try{
            javaGrepInp.process();
        }
        catch(Exception ex){
            javaGrepInp.logger.error("Error, unable to process ",ex);
        }
    }
}
