
/*************************************************************************
 * @author Kevin Wayne
 *
 * Description: A term and its weight.
 * 
 *************************************************************************/

import java.util.Comparator;

public class Term implements Comparable<Term> {

	private final String myWord;
	private final double myWeight;

	/**
	 * The constructor for the Term class. Should set the values of word and
	 * weight to the inputs, and throw the exceptions listed below
	 * 
	 * @param word
	 *            The word this term consists of
	 * @param weight
	 *            The weight of this word in the Autocomplete algorithm
	 * @throws NullPointerException
	 *             if word is null
	 * @throws IllegalArgumentException
	 *             if weight is negative
	 */
	public Term(String word, double weight) {

		if (word == null) {
			throw new NullPointerException("Null, nada, zilch");
		}
		if (weight < 0) {
			throw new IllegalArgumentException("Weights aren't negative silly");
		}
		myWord = word;
		myWeight = weight;
	}

	/**
	 * A Comparator for comparing Terms using a set number of the letters they
	 * start with. This Comparator may be useful in writing your implementations
	 * of Autocompletors.
	 *
	 */
	public static class PrefixOrder implements Comparator<Term> {
		private final int r;

		public PrefixOrder(int r) {
			this.r = r;
		}
		// Comparator with a parameter
		
		public int compare(Term first, Term second){
			String word1 = first.getWord();
			String word2 = second.getWord();
			
			
			if (word1.length() < r || word2.length() < r){
				return word1.compareTo(word2);
			}
			
			if(word1.substring(0, r).equals(word2.substring(0,r))){
				return 0;
			}
			else{
				return word1.substring(0, r).compareTo(word2.substring(0,r));
			}
		}
		
		
	}
	
	public static class ReverseWeightOrder implements Comparator<Term> {
		public int compare(Term first, Term second) {
			double weight1 = first.getWeight();
			double weight2 = second.getWeight();
			double difference = weight2 - weight1;
			
			if (difference < 0){
				return -1;
			}
			else if (difference == 0){
				return 0;
			}
			else {
				return 1;
			}
			
		
		}
	}
	
	
	public static class WeightOrder implements Comparator<Term> {
		public int compare(Term first, Term second) {
			double weight1 = first.getWeight();
			double weight2 = second.getWeight();
			double difference = weight2 - weight1;

			if (difference < 0){
				return 1;
			}
			else if (difference == 0){
				return 0;
			}
			else {
				return -1;
			}
			
		}
	}

	/**
	 * The default sorting of Terms is lexicographical ordering.
	 */
	public int compareTo(Term that) {
		return myWord.compareTo(that.myWord);
	}

	/**
	 * Getter methods, use these in other classes which use Term
	 */
	public String getWord() {
		return myWord;
	}

	public double getWeight() {
		return myWeight;
	}

	public String toString() {
		return String.format("%14.1f\t%s", myWeight, myWord);
	}
}
