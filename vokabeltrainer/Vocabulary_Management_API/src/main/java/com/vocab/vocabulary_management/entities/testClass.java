package com.vocab.vocabulary_management.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class testClass {
        public static void main(String[] args) {
            try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\mseet\\IdeaProjects\\KBAckend\\vokabeltrainer\\Vocabulary_Management_API\\src\\main\\java\\com\\vocab\\vocabulary_management\\entities\\vocab.txt"))) {
                String line;
                line = reader.readLine();
                System.out.println(line);
                Pattern metadataPattern = Pattern.compile("\\{\\{\\{(.*?)\\}\\}\\}");
                Matcher metadataMatcher = metadataPattern.matcher(line);
                if(metadataMatcher.find()) {
                    System.out.println(metadataMatcher.group().replace("{" , "").replace("}" , ""));
                }
                if(metadataMatcher.find()) {
                    System.out.println(metadataMatcher.group().replace("{" , "").replace("}" , ""));
                }
                while((line = reader.readLine()) != null) {
                    var list = separateString(line);
                    list.get(0); // original text kommt in Flashcard
                    for (int i = 1; i < list.size(); i++) {
                        list.get(i); // translation jeweils eigenes Translation Objekt
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        public static List<String> separateString(String input) {
            List<String> arrays = new ArrayList<>();

            // Define the regular expression pattern to match words inside curly braces
            Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher matcher = pattern.matcher(input);

            // Find and add all matching substrings to the arrays list
            while (matcher.find()) {
                arrays.add(matcher.group(1));
            }

            return arrays;
        }
    }

}
