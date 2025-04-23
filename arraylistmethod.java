import java.io.*;//import file I/O
import java.nio.charset.StandardCharsets;//importing UTF-8 charset
import java.util.*;

public class arraylistmethod {
    private ArrayList<String> dictionary= new ArrayList<>(); //declaring arraylist to store the dictionary 
    //final linking to the files that need to be read 
    private static final String DICTIONARY_FILE = "dictionary.txt"; 
    private static final String TEXT_FILE = "testing.txt";

    public arraylistmethod() throws IOException { //Constructor method with error handling 

        BufferedReader dictReader = new BufferedReader(new FileReader(DICTIONARY_FILE));//prepares to read the file 
        String word; //declares a string that will hold each word
        
        while ((word = dictReader.readLine()) != null) { //reads each line by line 
            dictionary.add(word.toLowerCase().trim()); //make the word lower case and remove any spaces and add to dictionary array list 
        }
        
        if (!isSorted(dictionary)) { //checks if the words are sorted and sort them using helper function.
            Collections.sort(dictionary); //sorting helps with binary search 
        }
    }

    private boolean isSorted(ArrayList<String> list) { //helper function declaration in the form of an arraylist 
        for (int i = 0; i < list.size() - 1; i++) {//iterate through the list 
            if (list.get(i).compareTo(list.get(i + 1)) > 0) { //if it is not sorted then return false 
                return false;
            }
        }
        return true;
    }

    public Map<String, List<Integer>> checkSpelling() throws IOException { //returns a map where mispelled word and line number is returned 
        Map<String, List<Integer>> misspelledWords = new TreeMap<>(); //store the key values of mispelled words with their line number 
        BufferedReader textReader = new BufferedReader(//for reading the testing file 
        new InputStreamReader(new FileInputStream(TEXT_FILE), StandardCharsets.UTF_8));//Reads the text file using UTF-8 encoding mainly for special char such as (-)
        String line;
        int lineNumber = 0;
        
        while ((line = textReader.readLine()) != null) {//reading line by line 
            lineNumber++;
            String[] words = line.split("\\s+");// split on whitespace
    
            for (String rawWord : words) {
                // Step 1: Clean the word (keep internal hyphens, en-dashes, and apostrophes)
                String word = rawWord.replace("’", "'") //replace the swigly one with straight one 
                                     .replaceAll("^[^a-zA-Z'–-]+", "")
                                     .replaceAll("[^a-zA-Z'–-]+$", "")
                                     .toLowerCase(); //make everything lower case 
                
                     if (word.isEmpty()) continue;
    
                if (word.endsWith("'s")) {
                    String base = word.substring(0, word.length() - 2);//gets everythign before the 2 char, cut the end 
                    if (Collections.binarySearch(dictionary, base) < 0 && Collections.binarySearch(dictionary, word) < 0) {//check in the dictionary 
                        misspelledWords.computeIfAbsent(word, k -> new ArrayList<>()).add(lineNumber);// mark the word as misspelled and add the line to it, if not found create a new array list and add line number 
                    }
                } else if (word.contains("'")) {
                    // Handle contractions like "don't"
                    if (Collections.binarySearch(dictionary, word) < 0) {
                        String noApostrophe = word.replace("'", "");
                        if (Collections.binarySearch(dictionary, noApostrophe) < 0) {
                            misspelledWords.computeIfAbsent(word, k -> new ArrayList<>()).add(lineNumber);
                        }
                    }
                }
                else if (word.contains("-")|| word.contains("–")) {
                    boolean allPartsValid = true;

                    String[] parts = word.split("[-–]"); // Split on both hyphen and en-dash
                    for (int i = 0; i < parts.length; i++) {
                        String part = parts[i];
                        if (!part.isEmpty() && Collections.binarySearch(dictionary, part) < 0) {
                            allPartsValid = false; // if only one part is not found, mark everything as wrong
                            break;
                        }
                    }
                    
                    if (!allPartsValid && Collections.binarySearch(dictionary, word) < 0) {
                        // Only flag if neither the compound sentence nor individual parts exist
                        misspelledWords.computeIfAbsent(word, k -> new ArrayList<>()).add(lineNumber);
                    }
                } else {
                    //binary search for all normal words after clearing special cases 
                    if (Collections.binarySearch(dictionary, word) < 0) {
                        misspelledWords.computeIfAbsent(word, k -> new ArrayList<>()).add(lineNumber);
                    }
                }
            }
        }
        //return the misspelled words
        return misspelledWords;
    }
        
    public static void main(String[] args) throws IOException {
        arraylistmethod checker = new arraylistmethod();// creates an object 
        Map<String, List<Integer>> errors = checker.checkSpelling(); // adds mispelled words to the errors map, 
        
        if (errors.isEmpty()) { //if there is no erros 
            System.out.println("No spelling errors found!");
        } else {
            System.out.println("Misspelled words:");
            for (Map.Entry<String, List<Integer>> entry : errors.entrySet()) {//lopping through the errors map and getting the mislepped word and value which is the line number 
                System.out.printf("%s - Lines: %s%n", entry.getKey(), entry.getValue());//print the misspelled words and their lines 
            }
        }
    }
}
    