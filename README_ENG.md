# FoolNLTK-java
[FoolNLTK](https://github.com/rockyzhengwu/FoolNLTK) Java Version

## maven

```
  <dependency>
       <groupId>me.midday</groupId>
       <artifactId>JFoolNLTK</artifactId>
       <version>1.0</version>
  </dependency>
```


## Usage Instructions
```java
import me.midday.FoolNLTK;
import me.midday.lexical.AnalysisResult;
import me.midday.lexical.Entity;
import me.midday.lexical.LexicalAnalyzer;
import me.midday.lexical.Word;

import java.util.List;

public class LSTMLexicalParserDemo {
    public static void main(String[] args){
       String text = "北京欢迎你";
       LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
       // Participle
       List<List<Word>> words = lexicalAnalyzer.cut(text);
       for(List<Word> ws: words){
           ws.forEach(System.out::println);
       }

       // POS Tagging
       List<List<Word>> posWords = lexicalAnalyzer.pos(text);
       for(List<Word> ws: posWords){
           ws.forEach(System.out::println);
       }
       // Named Entity Recognition
       List<List<Entity>>  entities = lexicalAnalyzer.ner(text);

       for(List<Entity> ents :entities){
           ents.forEach(System.out::println);
       }


       // Participle, POS Tagging, Named Entity Recognition
       List<AnalysisResult>  results = lexicalAnalyzer.analysis(text);
       results.forEach(System.out::println);


       // Multi-lined Input Version

       System.out.println();
       System.out.println("多文本：");
       List<String> docs = new ArrayList<>();
       docs.add(text);
       docs.add(text);
       // Participle
       List<List<Word>> dWords = lexicalAnalyzer.cut(docs);
       for(List<Word> ws: dWords){
           ws.forEach(System.out::println);
       }
       // POS Tagging
       List<List<Word>> dPosWords = lexicalAnalyzer.pos(docs);
       for(List<Word> ws: dPosWords){
           ws.forEach(System.out::println);
       }
       // Named Entity Recognition
       List<List<Entity>>  dEntities = lexicalAnalyzer.ner(docs);

       for(List<Entity> ents :dEntities){
           ents.forEach(System.out::println);
       }

       // Participle, POS Tagging, Named Entity Recognition
       List<AnalysisResult>  dResults = lexicalAnalyzer.analysis(docs);
       dResults.forEach(System.out::println);

    }
}
```
