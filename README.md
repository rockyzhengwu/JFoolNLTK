# FoolNLTK-java
[FoolNLTK](https://github.com/rockyzhengwu/FoolNLTK) java 版本


## 使用方法
```java
import me.midday.FoolNLTK;
import me.midday.lexical.AnalysisResult;
import me.midday.lexical.Entity;
import me.midday.lexical.LexicalAnalyzer;
import me.midday.lexical.Word;

import java.util.List;

public class LSTMLexicalParserDemo {
    public static void main(String[] args){
        LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
        String text = "北京欢迎你";
        // 分词
        List<List<Word>> words =  lexicalAnalyzer.cut(text);
        // 实体识别
        List<List<Entity>> entitys = lexicalAnalyzer.ner(text);
        // all
        List<AnalysisResult> res = lexicalAnalyzer.analysis(text);
        for(List<Word> sent: words){
            sent.forEach(System.out::println);
        }

    }
}
```