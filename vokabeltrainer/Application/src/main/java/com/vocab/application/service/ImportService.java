package com.vocab.application.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImportService {

    /**
     * read a content of a file. It is required to give the full absolute path of the file.
     *
     * @param path full absolute path of the file to be read.
     * @return content of file
     * @throws FileNotFoundException  if the file does not exist or no files in default path exists
     * @throws InvalidFormatException if the file(s) have not the expected format
     */
    boolean importFile(String path) throws IOException;

    /**
     * reads initial flashcardlist files and save it to database. Default path is relative path to current location and it is required to have the folder 'vocabFiles'.
     * @return true if succeeded, false otherwise
     */
    boolean importInitialFiles() throws IOException;
}
