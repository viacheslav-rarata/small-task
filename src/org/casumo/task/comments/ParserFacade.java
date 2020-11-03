package org.casumo.task.comments;

// TODO use named import for better performance instead of *
import java.io.*;
import java.util.Optional;

/**
 * This class is thread safe.
 */
// TODO wrong implementation and using of facade pattern
// TODO broken SOLID principles - reading, parsing and writing in one class
// TODO in case of using singleton the #setFile(File f) must be the first call before any
//  logic. Better to use one instance per file with initialization via constructor

public class ParserFacade {

    private static ParserFacade instance;

    // TODO add synchronized key word for thread safe singleton implementation
    public static ParserFacade getInstance() {
        if (instance == null) {
            instance = new ParserFacade();
        }
        return instance;
    }

    private File file;

    // TODO Setting file should be changed to constructor initialization
    public synchronized void setFile(File f) {
        file = f;
    }

    // TODO bad practice to return null. Should be changed to Optional.ofNullable()
    public synchronized Optional<File> getFile() {
        if (file != null) {
            return Optional.of(file);
        } else {
            return null;
        }
    }

    // TODO String concatenation should be changed to builder
    // TODO use try-catch with resources of try-finally to close stream
    // TODO synchronize reading content by creating monitor for #file
    // TODO handle exception
    public String getContent() throws IOException {
        FileInputStream i = new FileInputStream(file);
        String output = "";
        int data;
        while ((data = i.read()) > 0) output += (char) data;
        return output;
    }

    // TODO use #getContent() method instead of reading file again
    // TODO wrong reading of not Unicode characters - use #replaceAll("[^\\x00-\\x7F]", "") on #getContent()
    public String getContentWithoutUnicode() throws IOException {
        FileInputStream i = new FileInputStream(file);
        String output = "";
        int data;
        while ((data = i.read()) > 0) if (data < 0x80) {
            output += (char) data;
        }
        return output;
    }

    // TODO use try-catch with resources of try-finally to close stream
    // TODO synchronize saving content by creating monitor for #file
    // TODO handle exception
    public void saveContent(String content) throws IOException {
        FileOutputStream o = new FileOutputStream(file);
        for (int i = 0; i < content.length(); i += 1) {
            o.write(content.charAt(i));
        }
    }
}