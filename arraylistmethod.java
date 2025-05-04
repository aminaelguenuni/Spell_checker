//Basic def
//Binary search is an efficient algorithm for finding an item from a sorted list of items.
// It works by repeatedly dividing in half the portion of the list that could contain the item,
// until you've narrowed down the possible locations to just one. 
import java.io.*;//import file I/O
import java.util.*;

public class arraylistmethod {
    private ArrayList<String> dictionary= new ArrayList<>(); //declaring arraylist to store the dictionary 
    //final linking to the files that need to be read 
    private static final String DICTIONARY_FILE = "dictionary.txt"; 
    private static final String TEXT_FILE = "testing.txt";

    public arraylistmethod() throws IOException { //Constructor method with error handling 
        // reading dictionary file 
        Scanner s = new Scanner(new File(DICTIONARY_FILE));
        while (s.hasNext()){
            dictionary.add(s.next().toLowerCase().trim()); //make the word lower case and remove any spaces
        }  
    }

    public Map<String, List<Integer>> checkSpelling() throws IOException { //returns a map where mispelled word and line number is returned 
        Map<String, List<Integer>> misspelledWords = new TreeMap<>(); //store the key values of mispelled words with their line number 
        
        // reading text file line by line 
        try (BufferedReader br = new BufferedReader(//reads line by line 
            new InputStreamReader(new FileInputStream(TEXT_FILE)))) {//reads file and decodes its bytes into characters using UTF-8 encoding (avoiding any wierd char)
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {//reading line by line 
                lineNumber++;
                // splitting line into words 
                String[] words = line.split(" "); // split on whitespace
        
                for (String basicWord : words) {
                    String word = basicWord.replace("'", "'") //replace the swigly one with straight one
                                           .replaceAll("^[^a-zA-Z'–-]+", "")
                                           .replaceAll("[^a-zA-Z'–-]+$", "")
                                           .toLowerCase(); //make everything lower case 
                    
                        if (word.isEmpty()) continue;
        
                    if (word.endsWith("'s")) {
                        String base = word.substring(0, word.length() - 2);//gets everythign before the 2 char, cut the end 
                        if (Collections.binarySearch(dictionary, base) < 0 && Collections.binarySearch(dictionary, word) < 0) {//if the word does not exist + collection.binarysearch is used to search for a specific element within a sorted list.
                            if (!misspelledWords.containsKey(word)) {
                                misspelledWords.put(word, new ArrayList<>());
                            }
                            misspelledWords.get(word).add(lineNumber);
                                                       
                        }
                    } 
                        else if (word.contains("'")) {
                            // Handle contractions like "don't"
                            if (Collections.binarySearch(dictionary, word) < 0) {// if the word does not exist in dictionary 
                                String noApostrophe = word.replace("'", "");
                                if (Collections.binarySearch(dictionary, noApostrophe) < 0) {
                                    if (!misspelledWords.containsKey(word)) {
                                        misspelledWords.put(word, new ArrayList<>());
                                    }
                                    misspelledWords.get(word).add(lineNumber);
                                }
                            }
                        }
                    else if (word.contains("-")|| word.contains("–")) {
                        boolean allPartsValid = true;

                        String[] parts = word.split("[-–]"); // Split on both hyphen and en-dash
                        for (int i = 0; i < parts.length; i++) {
                            String part = parts[i];
                            if (!part.isEmpty() && Collections.binarySearch(dictionary, part) < 0) {//part is not found 
                                allPartsValid = false; // if only one part is not found, mark everything as wrong
                                break;
                            }
                        }
                        
                        if (!allPartsValid && Collections.binarySearch(dictionary, word) < 0) {
                            // Only flag if neither the compound sentence nor individual parts exist
                            if (!misspelledWords.containsKey(word)) {
                                misspelledWords.put(word, new ArrayList<>());
                            }
                            misspelledWords.get(word).add(lineNumber);                        }
                    } else {
                        //binary search for all normal words after clearing special cases 
                        if (Collections.binarySearch(dictionary, word) < 0) {

                            if (!misspelledWords.containsKey(word)) {
                                misspelledWords.put(word, new ArrayList<>());
                            }
                            misspelledWords.get(word).add(lineNumber);                        }
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
    
