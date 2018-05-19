package me.lexical;


import me.midday.dictionary.Match;
import me.midday.dictionary.Trie;

import java.util.List;

public class DictionaryDemo {
    public static void main(String[] args){
        Trie trie = new Trie();
        trie.addWord("北京");
        trie.addWord("北京天安门");
        String text = "我在北京天安门等你";
        trie.createFail();
        List<Match> mathList = trie.parseText(text);
        for(Match m: mathList){
            System.out.println(m);
        }
        "sentence".contains("word");
    }

}
