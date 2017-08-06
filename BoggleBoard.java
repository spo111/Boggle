import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class BoggleBoard {
	// the 16 Boggle dice (1992 version)
	private static final String[] BOGGLE_1992 = {
			"LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS",
			"ANAEEG", "IDSYTT", "OATTOW", "MTOICU",
			"AFPKFS", "XLDERI", "HCPOAS", "ENSIEU",
			"YLDEVR", "ZNRNHL", "NMIQHU", "OBBAOJ"
	};
	
	// the 16 Boggle dice (1983 version)
	private static final String[] BOGGLE_1983 = {
			"AACIOT", "ABILTY", "ABJMOQ", "ACDEMP",
			"ACELRS", "ADENVZ", "AHMORS", "BIFORX",
			"DENOSW", "DKNOTU", "EEFHIY", "EGINTV",
			"EGKLUY", "EHINPS", "ELPSTU", "GILRUW",
	};

	// the 25 Boggle Master / Boggle Deluxe dice
	private static final String[] BOGGLE_MASTER = {
			"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
			"AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
			"CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR",
			"DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
			"FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
	};
	
	// the 25 Big Boggle dice
	private static final String[] BOGGLE_BIG = {
			"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
			"AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCENST",
			"CEIILT", "CEILPT", "CEIPST", "DDHNOT", "DHHLOR",
			"DHLNOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
			"FIPRSY", "GORRVW", "IPRRRY", "NOOTUW", "OOOTTU"
	};
	
	// letters and frequencies of letters in the English alphabet
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final double[] FREQUENCIES = {
			0.08167, 0.01492, 0.02782, 0.04253, 0.12703, 0.02228,
			0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025,
			0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987,
			0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150,
			0.01974, 0.00074
	};
	
	private final int M;			// number of rows
	private final int N;			// number of columns
	private char[][] board;		// the M-by-N array of characters
	
	// Initializes a random 4-by-4 board, by rolling the Hasbro dice.
	public BoggleBoard() {
		M = 4;
		N = 4;
		int diceLen = BOGGLE_1992.length;
		Random random = new Random();
		for (int i = 0; i < diceLen; i++) {
			int r = i + random.nextInt(diceLen - i);
			String temp = BOGGLE_1992[r];
			BOGGLE_1992[i] = BOGGLE_1992[r];
			BOGGLE_1992[r] = temp;
		}
		board = new char[M][N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				String letters = BOGGLE_1992[N * i + j];
				int r = random.nextInt(letters.length());
				board[i][j] = letters.charAt(r);
			}
		}
	}
	
	// Initializes a board from the given filename.
	public BoggleBoard(String filename) {
		  Scanner sc = null;
		  try {
		   sc = new Scanner(new File(filename));
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		  }
		  M = sc.nextInt();
		  N = sc.nextInt();
		  board = new char[M][N];
		  for (int i = 0; i < M; i++) {
			  for (int j = 0; j < N; j++) {
				  String letter = sc.next();
				  if (letter.equals("QU"))
					  board[i][j] = 'Q';
				  else if (letter.length() != 1)
					  throw new IllegalArgumentException("invalid character: " + letter);
				  else if (ALPHABET.indexOf(letter) == -1)
					  throw new IllegalArgumentException("invalid character: " + letter);
				  else
					  board[i][j] = letter.charAt(0);
			  }
		  }
	}
	
	// Initialize a random M-by-N board, according to the frequency.
	public BoggleBoard(int M, int N) {
		this.M = M;
		this.N = N;
		board = new char[M][N];
		Random random = new Random();
		int freLen = FREQUENCIES.length;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				double r = random.nextDouble();
				double sum = 0;
				int k;
				for (k = 0; k < freLen; k++) {
					sum = sum + FREQUENCIES[k];
					if (sum > r) break;
				}
				board[i][j] = ALPHABET.charAt(k);
			}
		}
	}
	
	// Initialize a board from the given 2d character array; 'Q' represents "Qu".
	public BoggleBoard(char[][] a) {
		this.M = a.length;
		this.N = a[0].length;
		board = new char[M][N];
		for (int i = 0; i < M; i++) {
			if (a[i].length != N)
				throw new IllegalArgumentException("char[][] array is ragged");
			for (int j = 0; j < N; j++) {
				if (ALPHABET.indexOf(a[i][j]) == -1)
					throw new IllegalArgumentException("invalid character: " + a[i][j]);
				board[i][j] = a[i][j];
			}
		}
	}
	
	// Return the number of rows and columns.
	public int rows() { return M; }
	
	public int cols() { return N; }
	
	// Return the letter in row i and column j.
	public char getLetter (int i, int j) {
		return board[i][j];
	}
	
	// Return a string representation of the board, replacing 'Q' with "Qu"
	public String toString() {
		StringBuilder sb = new StringBuilder(M + " " + N + "\n");
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				sb.append(board[i][j]);
				if (board[i][j] == 'Q') sb.append("u ");
				else sb.append("  ");
			}
			sb.append("\n");
		}
		return sb.toString().trim();
	}
	
	// Unit tests the BoggleBoard data type.
	public static void main(String[] args) {
		// initialize a 4-by-4 board using Hasbro dice
		System.out.println("Hasbro board:");
		BoggleBoard board1 = new BoggleBoard();
		System.out.println(board1);
		System.out.println();
		
		// initialize a 4-by-4 board using letter frequencies in English language
		System.out.println("Random 4-by-4 board:");
		BoggleBoard board2 = new BoggleBoard(4, 4);
		System.out.println(board2);
		System.out.println();
		
		// initialize a 4-by-4 board from a 2d char array
		System.out.println("4-by-4 board from 2D character array:");
		char[][] a = {
				{ 'D', 'O', 'T', 'Y' },
				{ 'T', 'R', 'S', 'F' },
				{ 'M', 'X', 'M', 'O' },
				{ 'Z', 'A', 'B', 'W' }
		};
		BoggleBoard board3 = new BoggleBoard(a);
		System.out.println(board3);
		System.out.println();
		
		// initialize a 4-by-4 board from a file
		String filename = "board-quinquevalencies.txt";
		System.out.println("4-by-4 board from file " + filename + ":");
		BoggleBoard board4 = new BoggleBoard(filename);
		System.out.println(board4);
		System.out.println();
	}
}
