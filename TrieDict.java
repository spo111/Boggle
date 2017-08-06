import java.util.HashSet;

import java.util.LinkedList;
/*************************************************************************
 * Name: Zheng Kuang
 * Email: kuangzheng04@gmail.com
 *
 * Compilation:  javac TrieDic.java
 *
 * Description:  A word dictionary for words with letters A-Z, implemented
 *               using a 26-way trie (Adapted from TrieST.java).
 * http://coursera.cs.princeton.edu/algs4/assignments/boggle.html
 *
 *************************************************************************/

public class TrieDict {
	private static final int R = 26; 	// number of total characters from A to Z
	private static final int A = 65;		// ASCII code of A
	private Node root;		// root of trie
	private int N;			// number of words in trie
	
	// R-way trie node
	private static class Node {
		private boolean isWord = false;
		private Node[] next = new Node[R];
	}
	
	// Initializes an empty word dictionary.
	public TrieDict() {
		
	}
	
	// Does this dictionary contain the given word?
	public boolean contains(String word) {
		if (word == null) throw new IllegalArgumentException("argument to contains() is null");
		Node x = get(root, word, 0);
		return x.isWord;
	}
	
	private Node get(Node x, String word, int d) {
		if (x == null) return null;
		if (d == word.length()) return x;
		char c = word.charAt(d);
		return get(x.next[c - A], word, d + 1);
	}
	
	// Inserts a word into the dictionary
	public void put(String word) {
		if (word == null) throw new IllegalArgumentException("first argument to put() is null");
		root = put(root, word, 0);
	}
	
	private Node put(Node x, String word, int d) {
		if (x == null) x = new Node();
		if (d == word.length()) {
			if (!x.isWord) N++;
			x.isWord = true;
			return x;
		}
		char c = word.charAt(d);
		x.next[c - A] = put(x.next[c - A], word, d + 1);
		return x;
	}
	
	// Returns the number of words in this dictionary.
	public int size() {
		return N;
	}
	
	// Is this dictionary empty?
	public boolean isEmpty() {
		return size() == 0;
	}
	
	// Returns words in the dictionary as an Iterable.
	public Iterable<String> words() {
		return keysWithPrefix("");
	}
	
	// Returns all of the words in the set that start with prefix.
	public Iterable<String> keysWithPrefix(String prefix) {
		LinkedList<String> results = new LinkedList<String>();
		Node x = get(root, prefix, 0);
		collect(x, new StringBuilder(prefix), results);
		return results;
	}
	
	private void collect(Node x, StringBuilder prefix, LinkedList<String> results) {
		if (x == null) return;
		if (x.isWord) results.add(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c + A);
			collect(x.next[c], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}
	
	// Search a board from dice at site k, and add word to wordList
	public void searchBoard(BoggleBoard board, LinkedList<Integer>[] adj, int k, HashSet<String> wordList) {
		StringBuilder prefix = new StringBuilder();
		boolean[] used = new boolean[board.rows() * board.cols()];
		// start a new search
		searchBoard(board, adj, k, wordList, root, prefix, used);
	}
	
	private void searchBoard(BoggleBoard board, LinkedList<Integer>[] adj, int k, HashSet<String> wordList, 
							Node x, StringBuilder prefix, boolean[] used) {
		if (used[k]) return;		// stop search when current letter has been used
		
		char c = board.getLetter(k / board.cols(), k % board.cols());
		Node y = x.next[c - A];
		if (y == null) return; 	// stop search when current prefix is not in any word
		prefix.append(c); 		// update current prefix
		
		// letter "Q" in a board represent "Qu" in a word
		if (c == 'Q') {
			y = y.next['U' - A];
			if (y == null) {
				prefix.deleteCharAt(prefix.length() - 1);
				return;
			}
			prefix.append('U');
		}
		
		// add the word to wordList if the prefix forms a word
		if (y.isWord && prefix.length() > 2) {
			wordList.add(prefix.toString());
		}
		
		// mark the site k in board as used and search adjacent dice
		used[k] = true;
		for (int l : adj[k]) {
			searchBoard(board, adj, l, wordList, y, prefix, used);
		}
		
		// mark the site k as unuased and update prefix
		used[k] = false;
		if (c == 'Q') prefix.deleteCharAt(prefix.length() - 1);
		prefix.deleteCharAt(prefix.length() - 1);
	}
}
