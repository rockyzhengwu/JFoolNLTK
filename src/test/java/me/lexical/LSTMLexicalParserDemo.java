package me.lexical;

import me.midday.FoolNLTK;
import me.midday.lexical.AnalysisResult;
import me.midday.lexical.Entity;
import me.midday.lexical.LexicalAnalyzer;
import me.midday.lexical.Word;

import java.io.IOException;
import java.util.List;

public class LSTMLexicalParserDemo {
    public static void main(String[] args) throws IOException {
        LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
        System.out.println(lexicalAnalyzer.hashCode());
        System.out.println(FoolNLTK.getLSTMLexicalAnalyzer().hashCode());
        String text = "北京欢迎你北京天安门";
        lexicalAnalyzer.addUserDict("/Users/zhengwu/workspace/Github/JFoolNLTK/src/test/java/me/lexical/user_dict.txt");
        List<List<Word>> words =  lexicalAnalyzer.cut(text);
//        List<List<Entity>> entitys = lexicalAnalyzer.ner(text);
//        List<AnalysisResult> res = lexicalAnalyzer.analysis(text);
        System.out.println("last result ");
        for(List<Word> sent: words){
            sent.forEach(System.out::println);
        }

    }

}
