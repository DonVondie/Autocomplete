import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 
 * Using a sorted array of Term objects, this implementation uses binary search
 * to find the top term(s).
 * 
 * @author Austin Lu, adapted from Kevin Wayne
 *
 */
public class BinarySearchAutocomplete implements Autocompletor {

	Term[] myTerms;

	/**
	 * Given arrays of words and weights, initialize myTerms to a corresponding
	 * array of Terms sorted lexicographically.
	 * 
	 * This constructor is written for you, but you may make modifications to
	 * it.
	 * 
	 * @param terms
	 *            - A list of words to form terms from
	 * @param weights
	 *            - A corresponding list of weights, such that terms[i] has
	 *            weight[i].
	 * @return a BinarySearchAutocomplete whose myTerms object has myTerms[i] =
	 *         a Term with word terms[i] and weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument passed in is null
	 */
	public BinarySearchAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		myTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			myTerms[i] = new Term(terms[i], weights[i]);
		}
		Arrays.sort(myTerms);
	}

	/**
	 * Uses binary search to find the index of the first Term in the passed in
	 * array which is considered equivalent by a comparator to the given key.
	 * This method should not call comparator.compare() more than 1+log n times,
	 * where n is the size of a.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The first index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	public static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {

		if (a.length == 0)  {
			return -1;
		}
		if (a == null){
			return -1;
		}
		if (key== null){
			return -1;
		}
		
		int low = -1;
		int high = a.length;
		int avg = 0;
		
		while (high - low > 1) {
			avg = (low + high) / 2;
			
			if (comparator.compare(a[avg], key) == 0) {
				high = avg;
			} else if (comparator.compare(a[avg], key) > 0) {
				high = avg;
			} else {
				low = avg;
			}
		}
		if(high !=-1 && high < a.length && comparator.compare(a[high], key)==0){
			return high;
		}

		else {
			return  -1;
		}
		
	}

	/**
	 * The same as firstIndexOf, but instead finding the index of the last Term.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The last index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	public static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {

		if (a.length == 0)  {
			return -1;
		}
		if (a == null){
			return -1;
		}
		if (key== null){
			return -1;
		}

		int low = -1;
		int high = a.length;
		int avg;
		
		while (high - low > 1) {
			avg = (low + high) / 2;
			if (comparator.compare(a[avg], key) == 0) {
				low = avg;
			} else if (comparator.compare(a[avg], key) < 0) {
				low = avg;
			} else {
				high = avg;
			}
		}
		if(low !=-1 && low < a.length && comparator.compare(a[low], key)==0){
			return low;
		}
		else {
			return  -1;
		}
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in myTerms with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, reutrn an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {

		if (prefix == null) {
			throw new NullPointerException("I don't like null prefixes!");
		}
		Term search = new Term(prefix, 0);
		
		int first = firstIndexOf(myTerms, search, new Term.PrefixOrder(prefix.length()));
		int last = lastIndexOf(myTerms, search, new Term.PrefixOrder(prefix.length()));

		if (first == -1 || last == -1) {
			String[] nothing = {};
			return nothing;
		}
		
		PriorityQueue<Term> termPQ = new PriorityQueue<Term>(k, new Term.ReverseWeightOrder());
		//easier with reverseweight order
		int r = first;
		while(r <= last){
			termPQ.add(myTerms[r]);
			r++;
		}
		int numMatches = Math.min(k, termPQ.size());
		String[] output = new String[numMatches];
		
		int jj = 0;
		while(jj < numMatches){
			output[jj] = termPQ.remove().getWord();
			jj++;
		}
				 
		for(int rr = 0; rr < numMatches; rr++){
			System.out.println(output[rr]);
		}
		return output;
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in myTerms starting with
	 * that prefix. e.g. for {air:3, bat:2, bell:4, boy:1}, topMatch("b") would
	 * return "bell". If no such word exists, return an empty String.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from myTerms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {

		if (prefix == null) {
			throw new NullPointerException("I don't like null prefixes!");
		}
		Term search = new Term(prefix, 0);
		int first = firstIndexOf(myTerms, search, new Term.PrefixOrder(prefix.length()));
		int last = lastIndexOf(myTerms, search, new Term.PrefixOrder(prefix.length()));
		if (first == -1 || last == -1) {
			return "";
		}

		String emptyString = "";
		double rootWeight = -1;
		
		int i = first;
		while(i <= last){
			double tempWeight = myTerms[i].getWeight();
			String tempWord = myTerms[i].getWord();
			boolean tempStart = tempWord.startsWith(prefix);
			boolean tempBool = tempWeight > rootWeight;
			
			if (tempBool && tempStart) {
				emptyString = myTerms[i].getWord();
				rootWeight = myTerms[i].getWeight();
			}
			i++;
		}
		
		return emptyString;
	}


}