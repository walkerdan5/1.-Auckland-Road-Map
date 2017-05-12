import java.util.*;

public class Trie {
	TrieNode head = null;
	
	public Trie(){
		this.head = new TrieNode();
		head.value = ""; //head does not require a value
		
	}
	public void addWord(String word){
		if(word == null) return;
		
		word = word.toLowerCase();
		head.addChild(word, 0);
	}
	
	public String[] getWord(String word){
		ArrayList<String> returnedStrings = new ArrayList<String>();
		returnedStrings = head.getChild(returnedStrings, word, 0);
		ArrayList<String> nonDupliStrings = new ArrayList<String>();
		for(String s: returnedStrings){
			if(!nonDupliStrings.contains(s)){
				nonDupliStrings.add(s);
			}
		}
		
		//Started to work on a drop down box for the search, with a limit of 10 options, but didn't finish it
		String[] stringOptions = new String[10]; //Restricts search options to 10 possibilities
				for(int i=0; i < Math.min(10, nonDupliStrings.size()); i++){
					stringOptions[i]= nonDupliStrings.get(i);
				}
				return stringOptions;
	}
	
	public void word(){
		head.printWord();
	}
}
