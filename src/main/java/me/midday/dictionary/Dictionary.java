package me.midday.dictionary;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Trie trie = new Trie();
    // 存储权重
    private Map<String, Integer> weightMap = new HashMap<>();

    public Dictionary(){}

    //
    public void addUserDict(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        addUserDict(fileInputStream);
    }
    public void addUserDict(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        while ((line=reader.readLine())!=null){
            String[] wordsInfo = line.split(" ");
            String word = "";
            int count = 0;
            String pos = "";
            if (wordsInfo.length==2){
                word = wordsInfo[0];
                count = Integer.valueOf(wordsInfo[1]);
            }
            else if(wordsInfo.length == 3){
                word = wordsInfo[0];
                count = Integer.valueOf(wordsInfo[1]);
                pos = wordsInfo[2];
            }
            else{
                continue;
            }

        }
    }

}
