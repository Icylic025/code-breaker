
/*************************************************************************
* Name: CodeBreaker
* Author: Kylie Wang
* Date: 2021/10/6
* Purpose: A program that outputs in console the top 3 decoded possibilities 
* of a Caesar Cipher. Decoded solutions are calculated with letter frequency 
* and checking random words in a dictionary.
***************************************************************************/
import java.io.*;

public class CodeBreaker {

	static String[] choices = new String[26]; // possible choices message can be
	static int[] rank = new int[26]; // keeps points given to each message if it might be english
	static int rank1 = -1;
	static int rank2 = -1;
	static int rank3 = -1;

	public static void main(String[] args) {

		String[] oneLetter = { "e", "t", "a", "o", "i", "n", "s", "h", "r", "d", "l", "u" };
		String[] twoLetter = { "th", "er", "on", "an", "re", "he", "in", "ed", "nd", "ha", "at", "en", "es", "of", "or", "nt", "ea", "ti", "to", "it", "st", "io", "le", "is", "ou", "ar", "as", "de", "rt", "ve" };
		String[] threeLetter = { "the", "and", "tha", "ent", "ion", "tio", "for", "nde", "has", "nce", "edt", "tis", "oft", "sth", "men" };
		String[] doubleLetter = { "ss", "ee", "tt", "ff", "ll", "mm", "oo" };
		String[] dictionary = getFileContents("dictionary.txt");
		String randomWord = "";
		int counter = 0;

		// generate all possibilities of the message
		get25();

		// goes through messages to see if it has English letter combinations
		for (int i = 0; i < choices.length; i++) {

			// check for combinations and gives points
			check(oneLetter, i);
			check(twoLetter, i);
			check(threeLetter, i);
			check(doubleLetter, i);
		} // for

		// checks 7 random words in each message
		do {

			// checks random words in all 26 messages
			// gives higher amount of points if a random word is English
			for (int i = 0; i < choices.length; i++) {
				randomWord = getRandomWord(i);
				if (isWord(randomWord, dictionary)) {
					rank[i] = rank[i] + 5;
				} // if
			} // for
			counter++;
		} while (counter <= 7);

		// calculate and display final ranking
		ranking();

	}

	// goes through dictionary.txt to see if a word is English
	public static boolean isWord(String word, String[] dictionary) {
		for (int i = 0; i < dictionary.length; i++) {
			if (word.equalsIgnoreCase(dictionary[i])) {
				return true;
			} // if

		} // for
		return false;
	}

	// checks if a string includes letters in certain orders
	public static void check(String[] a, int i) {
		for (int j = 0; j < a.length; j++) {

			// counts how many times a combination of letter appears
			rank[i] += countMatches(choices[i], a[j]);

		} // for

	}

	// calculate the top three possibilities and print to console
	public static int ranking() {
		int rank1Num = 0;
		int rank2Num = 0;
		int rank3Num = 0;

		rank1Num = 0;
		rank2Num = 0;
		rank3Num = 0;

		// find message with highest points
		for (int i = 0; i < rank.length; i++) {

			if (rank[i] > rank1Num) {
				rank1Num = rank[i];
				rank1 = i;
			}
		} // for

		// find second highest points
		for (int i = 0; i < rank.length; i++) {

			if (i != rank1 && rank[i] > rank2Num) {
				rank2Num = rank[i];
				rank2 = i;
			}
		} // for

		// find third highest points
		for (int i = 0; i < rank.length; i++) {
			if (i != rank1 && i != rank2 && rank[i] > rank3Num) {
				rank3Num = rank[i];
				rank3 = i;
			}
		} // for

		System.out.println("Rank 1: " + choices[rank1]);
		System.out.println("Rank 2: " + choices[rank2]);
		System.out.println("Rank 3: " + choices[rank3]);
		return rank1;

	} // ranking

	// Counts how many times the substring appears in the larger string.
	public static int countMatches(String text, String str) {

		int index = 0;
		int count = 0;
		while (true) {
			index = text.indexOf(str, index);
			if (index != -1) {
				count++;
				index += str.length();
			} else {
				break;
			} // else
		} // while

		return count;
	} // countMatches

	// finds a random group of letters between spaces
	public static String getRandomWord(int rank) {
		int length = 0;
		int random = 0;
		int firstLetter = 0;
		int lastLetter = 0;
		int number = 0;
		char a;
		String word = "";
		String str = "";
		length = choices[1].length();

		// adds space so program doesn't crash if it picked first or last word
		str = " " + choices[rank] + " ";

		// repeat until the random index of a message is a letter
		do {
			random = (int) (Math.random() * (length));

			if (str.charAt(random) < 97 || str.charAt(random) > 122) {
				continue;
			} // do

			break;

		} while (true);

		// index moves forward until space to find the starting letter
		for (int i = 0;; i++) {

			if (str.charAt(random + i) == 32) {
				lastLetter = random + i - 1;
				break;
			} // if

		} // for

		// index moves back to find ending letter
		do {
			if (str.charAt(lastLetter) < 97 || str.charAt(lastLetter) > 122) {
				lastLetter--;

			} else {
				break;

			} // else
		} while (true);

		// removes symbols at the end of a word
		for (int i = 0;; i++) {
			if (str.charAt(random - i) < 97 || str.charAt(random - i) > 122) {
				firstLetter = random - i + 1;
				break;
			}
		}

		// converts the chars between starting letter and ending letter to string
		do {
			a = str.charAt(firstLetter + number);
			word = word + String.valueOf(a);
			number++;
		} while (firstLetter + number <= lastLetter);

		return word;

	} // getRandomWord

	// reads fileName and returns the contents as String array
	// with each line of the file as an element of the array
	public static String[] getFileContents(String fileName) {

		String[] contents = null;
		int length = 0;
		try {

			// input
			String folderName = ""; // if the file is contained in the same folder as the .class file, make
									// this equal to the empty string
			String resource = fileName;

			// this is the path within the jar file
			InputStream input = CodeBreaker.class.getResourceAsStream(folderName + resource);
			if (input == null) {
				// this is how we load file within editor (eg eclipse)
				input = CodeBreaker.class.getClassLoader().getResourceAsStream(resource);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(input));

			in.mark(1000000); // see api

			// count number of lines in file
			while (in.readLine() != null) {
				length++;
			}

			in.reset(); // rewind the reader to the start of file
			contents = new String[length]; // give size to contents array

			// read in contents of file and print to screen
			for (int i = 0; i < length; i++) {
				contents[i] = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			System.out.println("File Input Error");
		}

		return contents;

	} // getFileContents

	// generates other 25 possible messages
	public static void get25() {
		String fileContent;
		String[] fileContents = getFileContents("message.txt");
		fileContent = fileContents[0].toString();
		int number = 0;
		String str = fileContent.toLowerCase();
		String a = "";

		// Creating array of string length
		char[] charArray = new char[str.length()];

		// populates the array with messages
		do {

			// Copy character by character into array
			for (int i = 0; i < str.length(); i++) {
				charArray[i] = str.charAt(i);
			}

			// shift
			for (int i = 0; i < charArray.length; i++) {

				if (charArray[i] <= 122 && charArray[i] >= 97) {

					charArray[i] = (char) (charArray[i] + number);

					if (charArray[i] > 122) {
						charArray[i] = (char) (charArray[i] - 122 + 96);
					} // if

				} // if

			} // for

			a = new String(charArray);

			choices[number] = a.toLowerCase();
			number++;
		} while (number < 26);

	} // get25

} // codeBreaker
