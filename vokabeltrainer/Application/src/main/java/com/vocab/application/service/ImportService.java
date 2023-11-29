package com.vocab.application.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImportService {

    /**
     * read a content of a file.
     *
     * @param path
     * @return content of file
     * @throws FileNotFoundException  if the file does not exist or no files in default path exists
     * @throws InvalidFormatException if the file(s) have not the expected format
     */
    boolean importFile(String path) throws IOException;

    /**
     * reads initial flashcardlist files and save it to database
     * @return true if succeeded, false otherwise
     */
    boolean importInitialFiles() throws IOException;
}
