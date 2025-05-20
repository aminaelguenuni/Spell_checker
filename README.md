# 📝 Spell Checker

This repository contains a Java program that performs **offline spell checking** by comparing words from an input text file against a dictionary. The program is designed for educational purposes, focusing on data structures and algorithm efficiency.

## Features

- **Dictionary Loading**: Reads a dictionary text file (alphabetically sorted) and builds efficient data structures for fast look-up.
- **Spell Checking**: Scans a given text file, detects unrecognized words, and reports them with their corresponding line numbers.
- **Two Implementations**:
  - `ArrayList` + Binary Search: Simple structure with fast look-up in a sorted list.
  - Ternary Search Trie (TST) + Radix Search: Efficient for prefix-based lookups and space optimization.
  
## Input Files

- `dictionary.txt`: Contains valid words, one per line, sorted alphabetically.
- `input.txt`: Contains the content to be spell-checked.

## Output

A list of unrecognized words along with the line numbers where they appear.

