package org.casumo.task.refactor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * This class is thread safe.
 */
public class ParserFacade {

    private final File file;

    public ParserFacade(File file) {
        this.file = file;
    }

    public synchronized Optional<File> getFile() {
        return Optional.ofNullable(file);
    }

    public String getContent() {
        synchronized (file) {
            StringBuilder output = new StringBuilder();
            try (InputStream i = new FileInputStream(file)) {
                int data;
                while ((data = i.read()) > 0) {
                    output.append((char) data);
                }
            } catch (Exception e) {
                System.err.println("Error on reading file " + file.getAbsolutePath());
                throw new RuntimeException(e);
            }
            return output.toString();
        }
    }

    /**
     * Excluding unicode symbols should be for range 0–127 (x00–x7F)
     */
    public String getContentWithoutUnicode() {
        return getContent().replaceAll("[^\\x00-\\x7F]", "");
    }

    public void saveContent(String content) {
        synchronized (file) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            } catch (IOException e) {
                System.err.println("Error on saving content to file " + file.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }
    }

}