//Basic def 
// ternary search tree is a type of trie (sometimes called a prefix tree)
// where nodes are arranged in a manner similar to a binary search tree, 
//but with up to three children rather than the binary tree's limit of two AND holds one char 

//Radix Search is a prefix-based searching technique used to efficiently 
//find data based on common prefixes.
//starts with method 

import java.io.*;//import file I/O 
import java.util.*;

public class tst {
    private TSTNode root; // Root of the TST
    private static final String DICTIONARY_FILE = "dictionary.txt";//storing the files
    private static final String TEXT_FILE = "testing.txt";

    private static class TSTNode {//defines a node 
        char character;
        boolean isEndOfWord;// mark the end of the word 
        TSTNode left, middle, right;// for childreen, left, middle and right 

        TSTNode(char character) {//contructor 
            this.character = character;
        }
    }

    public tst() throws IOException {
        BufferedReader dictReader = new BufferedReader(new FileReader(DICTIONARY_FILE));//read the dictionary file 
        String word;
        
        while ((word = dictReader.readLine()) != null) {// read each word from dic and add it to the TST 
            insert(word.toLowerCase().trim());
        }
    }

    // Insert a word into the TST
    private void insert(String word) {// method already mentioned above 
        root = insert(root, word, 0);
    }

    private TSTNode insert(TSTNode node, String word, int index) {//insert a node into the TST tree 
        char c = word.charAt(index);
        
        if (node == null) {// if the node is null, we create a new node with char c 
            node = new TSTNode(c);
        }
        
        if (c < node.character) {//we go left if the char we want to insert is less than the node char 
            node.left = insert(node.left, word, index);
        } else if (c > node.character) {
            node.right = insert(node.right, word, index);
        } else {
            if (index < word.length() - 1) {//insert in the middle if we our char is still not at the end 
                node.middle = insert(node.middle, word, index + 1);
            } else {
                node.isEndOfWord = true;//if done, then we are at the end of the word 
            }
        }
        return node;
    }

    // Search for a word in the TST
    public boolean search(String word) {// to recursively search 
        return search(root, word, 0);
    }

    private boolean search(TSTNode node, String word, int index) {// the helper search method 
        if (node == null) {
            return false;
        }
        
        char c = word.charAt(index);
        
        if (c < node.character) {
            return search(node.left, word, index);
        } else if (c > node.character) {
            return search(node.right, word, index);
        } else {
            if (index == word.length() - 1) {
                return node.isEndOfWord;//marks the end of the word
            } else {
                return search(node.middle, word, index + 1);//otherwise search in the middle 
            }
        }
    }
    // Radix search for prefix search and guide throw the TST 
    public boolean beginWith(String prefix) {
        return beginWith(root, prefix, 0);//return the helper method 
    }

    private boolean beginWith(TSTNode node, String prefix, int index) {//helper method 
        if (node == null) {
            return false;
        }
        
        char c = prefix.charAt(index);//prefix is stuff attached at the beginning 
        
        if (c < node.character) {
            return beginWith(node.left, prefix, index);
        } else if (c > node.character) {
            return beginWith(node.right, prefix, index);
        } else {
            if (index == prefix.length() - 1) {// if we are at the last char of the prefix 
                return true;
            } else {
                return beginWith(node.middle, prefix, index + 1);
            }
        }
    }
     //search for errors 
    public Map<String, List<Integer>> checkSpelling() throws IOException {//store mispelled words with their keys and values 
        Map<String, List<Integer>> misspelledWords = new TreeMap<>();
        BufferedReader textReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(TEXT_FILE)));//opens the file with UTF-8 encoding 
        String line;
        int lineNumber = 0;
        
        while ((line = textReader.readLine()) != null) {
            lineNumber++;
            String[] words = line.split("\\s+");
    
            for (String rawWord : words) {
                String word = rawWord.replace("'", "'")
                                    .replaceAll("^[^a-zA-Z'–-]+", "")
                                    .replaceAll("[^a-zA-Z'–-]+$", "")
                                    .toLowerCase();
                
                if (word.isEmpty()) continue;
    
                if (word.endsWith("'s")) {
                    String base = word.substring(0, word.length() - 2);
                    if (!search(base) && !search(word)) {
                        if (!misspelledWords.containsKey(word)) {
                            misspelledWords.put(word, new ArrayList<>());
                        }
                        misspelledWords.get(word).add(lineNumber); 
                        }
                } else if (word.contains("'")) {
                    if (!search(word)) {//original full word not found 
                        String noApostrophe = word.replace("'", "");
                        if (!search(noApostrophe)) {// if the clean version is not found 
                            if (!misspelledWords.containsKey(word)) {
                                misspelledWords.put(word, new ArrayList<>());
                            }
                            misspelledWords.get(word).add(lineNumber); 
                        }
                    }
                } else if (word.contains("-") || word.contains("–")) {
                    boolean allPartsValid = true;
                    String[] parts = word.split("[-–]");

                    for (int i = 0; i < parts.length; i++) {
                        String part = parts[i];
                        if (!part.isEmpty() && !search(part)) {
                            allPartsValid = false; // if only one part is not found, mark everything as wrong
                            break;
                        }
                    }
                    
                } 
                else {
                    if (!search(word)) {//for all simple words just search in the dict
                        if (!misspelledWords.containsKey(word)) {
                            misspelledWords.put(word, new ArrayList<>());
                        }
                        misspelledWords.get(word).add(lineNumber); 
                    }
                }
            }
        }
        return misspelledWords;//return 
    }  
    public static void main(String[] args) throws IOException {//main methods 
        tst checker = new tst();//create object instance 
        Map<String, List<Integer>> errors = checker.checkSpelling();// map to store the erros 
        
        if (errors.isEmpty()) {
            System.out.println("No spelling errors found!");
        } else {
            System.out.println("Misspelled words:");
            for (Map.Entry<String, List<Integer>> entry : errors.entrySet()) {//loop through the map 
                System.out.printf("%s - Lines: %s%n", entry.getKey(), entry.getValue());
            }
        }
    }
}
