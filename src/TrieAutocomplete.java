import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 *
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument is null
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 * 
	 */
	private void add(String word, double weight) {
		//Add terms to the trie. Accounts for duplicate adds with lower weights than previous adds.
		
		if(word == null){
			throw new NullPointerException("I don't like null words!");
		}
		if(weight < 0){
			throw new IllegalArgumentException("What do you mean negative weights!?");
		}
		

		
		Node rootCopy = myRoot;
		int chars = word.length();
		int i = 0;
		
		while(i < chars){
			double temp = rootCopy.mySubtreeMaxWeight;
			
			if (temp < weight){
				rootCopy.mySubtreeMaxWeight = weight;
			}
			
			if (!rootCopy.children.containsKey(word.charAt(i))){
				Node daughter = new Node(word.charAt(i), rootCopy, weight);
				rootCopy.children.put(word.charAt(i), daughter);
			}
			
			
			rootCopy = rootCopy.children.get(word.charAt(i));
			
			i++;
		}
		
		
		rootCopy.isWord = true;
		rootCopy.setWeight(weight);
		rootCopy.setWord(word);
		
		if (weight <= rootCopy.mySubtreeMaxWeight) {
			
			while (rootCopy.parent != null) {
				
				double tempMax = rootCopy.getWeight();
				
				for (char x: rootCopy.children.keySet()) {
					Node temp = rootCopy.children.get(x);
					
					if (tempMax < temp.mySubtreeMaxWeight) {
						tempMax = temp.mySubtreeMaxWeight;
					}
				}
				rootCopy.mySubtreeMaxWeight = tempMax;
				rootCopy = rootCopy.parent;
			}			
		}
		else {
			rootCopy.mySubtreeMaxWeight = weight;
		}
	}


	@Override
	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
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
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		if(prefix==null){
			throw new NullPointerException("prefix is null");
		}
		if(k==0){
			String[] xy = {};
			return xy;
		}
		
		PriorityQueue<Node> allNodes = new PriorityQueue<Node>(new Node.ReverseSubtreeMaxWeightComparator());
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Node rootCopy = myRoot;
		
		int l = prefix.length();
		int i = 0;
		while(i < l) {
			if (rootCopy.getChild(prefix.charAt(i)) != null) {
				
				rootCopy = rootCopy.getChild(prefix.charAt(i));
			}
			else {
				String[] x = {};
				return x;
			}
			i++;
		}
		
		
		allNodes.add(rootCopy);
		
		
		//
		while(!allNodes.isEmpty()){
			if(k <= nodeList.size()){
				
				
				Node peekValue = allNodes.peek();
				double weight1 = peekValue.mySubtreeMaxWeight;
				Node last = nodeList.get(0);
				double weight2 = last.getWeight();
				boolean x = weight1 < weight2;
				
				if(x){
					break;
				}	
				
				
			}
			// Only break when the weight of the first element in the arraylist
			// has a weight that is greater than what is left in the priority queue
			rootCopy =allNodes.remove();
			
			if(rootCopy.isWord){
				nodeList.add(rootCopy);
			}
			
			for(char x: rootCopy.children.keySet()){
				allNodes.add(rootCopy.children.get(x));
			}
			
		}
		//

		Collections.sort(nodeList, Collections.reverseOrder());
		int size = Math.min(k, nodeList.size());
		String[] output = new String[size];
		
		int p = 0;
		while(p < size){
			output[p] = nodeList.get(p).getWord();
			p++;
		}
		
		for (int r = 0; r < output.length; r ++){
			System.out.println(output[r]);
		}
		
		return output;
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		if(prefix==null){
			throw new NullPointerException("what can I do with a null prefix?");
		}
		
		Node rootCopy = myRoot;
		
		int l = prefix.length();
		int i = 0;
		while(i < l) {
			if (rootCopy.getChild(prefix.charAt(i)) != null) {
				
				rootCopy = rootCopy.getChild(prefix.charAt(i));
			}
			else {
				
				return "";
			}
			i++;
		}
	
		double rootWeight = rootCopy.mySubtreeMaxWeight;
		
		while(!rootCopy.isWord){
		for(char x: rootCopy.children.keySet()){
			Node temp = rootCopy.children.get(x);
			if(rootWeight==temp.mySubtreeMaxWeight){
				rootCopy=temp;
				break;
			}
		}
		}
		
		return rootCopy.getWord();
	}	
}