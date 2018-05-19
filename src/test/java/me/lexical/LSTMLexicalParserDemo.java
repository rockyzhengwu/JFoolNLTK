package me.lexical;

import me.midday.FoolNLTK;
import me.midday.lexical.AnalysisResult;
import me.midday.lexical.Entity;
import me.midday.lexical.LexicalAnalyzer;
import me.midday.lexical.Word;

import java.util.List;

public class LSTMLexicalParserDemo {
    public static void main(String[] args){
        LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
        System.out.println(lexicalAnalyzer.hashCode());
        System.out.println(FoolNLTK.getLSTMLexicalAnalyzer().hashCode());
        String text = "北京欢迎你";
        List<List<Word>> words =  lexicalAnalyzer.cut(text);
        List<List<Entity>> entitys = lexicalAnalyzer.ner(text);
        List<AnalysisResult> res = lexicalAnalyzer.analysis(text);
        for(List<Word> sent: words){
            sent.forEach(System.out::println);
        }
    }

}
