package com.vocab.application.serviceImpl;

import com.vocab.application.service.ImportService;
import com.vocab.vocabulary_management.services.FlashcardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@ComponentScan(basePackages = {"com.vocab"})
public class ImportServiceImpl implements ImportService {

    @Autowired
    FlashcardListService flashcardListService;

    private final String DEFAULT_PATH = Paths.get("").toAbsolutePath().toString() + File.separator + "vocabFiles" + File.separator;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importFile(String path) throws IOException {
        String content = readFile(path);
        if (content.isEmpty() || content.trim().isBlank()) {
            return false;
        }
        return flashcardListService.createFlashcardList(content);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importInitialFiles() throws IOException {
        Set<String> files = retrieveFilenamesFromFolder();

        for (String filename : files) {
            String content = readFile(filename);
            if (content.isEmpty() || content.trim().isBlank() || !flashcardListService.createFlashcardList(content)) {
                return false;
            }
        }
        return true;
    }

    private Set<String> retrieveFilenamesFromFolder() throws IOException {
        // read all filenames in folder
        Set<String> filenames;
        Path default_folder = Paths.get(DEFAULT_PATH);
        try (Stream<Path> stream = Files.list(default_folder)) {
            filenames = stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(filename -> DEFAULT_PATH + filename)
                    .collect(Collectors.toSet());
        }

        // check if a zip-File exists and if so extract the files
        filenames.forEach(filename -> {
            if (filename.endsWith(".zip")) {
                try {
                    extractFilesFromZip(filename);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //collect filenames including absolute path
        try (Stream<Path> stream = Files.walk(default_folder)) {
            return stream.filter(file -> !Files.isDirectory(file) && !file.getFileName().toString().endsWith(".zip"))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    private void extractFilesFromZip(String zipFile) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), StandardCharsets.ISO_8859_1);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(new File(DEFAULT_PATH), zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();


        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private String readFile(String filename) throws IOException {
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException ioex) {
            throw ioex;
        }
        return content;
    }

}
