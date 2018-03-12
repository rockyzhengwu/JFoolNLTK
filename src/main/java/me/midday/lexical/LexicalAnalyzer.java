package me.midday.lexical;

import java.util.List;

public interface LexicalAnalyzer {
    List<List<Word>> cut(String text);
    List<List<Word>> cut(List<String> docs);
    List<List<Word>> pos(String text);
    List<List<Word>> pos(List<String> docs);
    List<List<Entity>> ner(String text);
    List<List<Entity>> ner(List<String> docs);

    List<AnalysisResult> analysis(String text);
    List<AnalysisResult> analysis(List<String> text);

}
