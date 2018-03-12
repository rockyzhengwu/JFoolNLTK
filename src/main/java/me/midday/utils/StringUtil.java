package me.midday.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    public static List<String> docToSents(String text){
        StringTokenizer tokenizer = new StringTokenizer(text, "。");
        StringBuffer buffer = new StringBuffer();
        List<String> sents = new ArrayList<>();
        char[] chars = text.toCharArray();
        for (int p = 0; p < chars.length; p++){
            char tmp = chars[p];
            buffer.append(tmp);
            if(tmp =='。' || tmp=='？' || tmp=='!' || tmp=='!' || tmp=='?'){
                sents.add(buffer.toString());
                buffer = new StringBuffer();
            }
        }
        if(buffer.length()!=0){
            sents.add(buffer.toString());
        }
        return sents;
    }
}
