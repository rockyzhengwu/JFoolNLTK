package me.midday.lexical;



import me.midday.ruleparser.RuleEntityParserFactory;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LSTMLexicalAnalyzer implements LexicalAnalyzer {

    private TFPredictor segModel = new TFPredictor();
    private TFPredictor posModel = new TFPredictor();
    private TFPredictor nerPredictor = new TFPredictor();
    private Vocab vocab;

    public LSTMLexicalAnalyzer(Vocab vocab, TFPredictor segModel, TFPredictor posModel, TFPredictor nerPredictor){
        this.vocab = vocab;
        this.segModel = segModel;
        this.posModel = posModel;
        this.nerPredictor = nerPredictor;
    }

    public LSTMLexicalAnalyzer(String segModelPath, String posModelPath, String nerModelPath, String vocabPath) {
        loadModel(segModelPath, posModelPath, nerModelPath, vocabPath);
    }

    public LSTMLexicalAnalyzer(InputStream zipInputStream, InputStream segModelStream, InputStream posModelStream, InputStream nerModelStream) {

    }


    //
    public LSTMLexicalAnalyzer(String modelDir) {
        loadModel(modelDir);
    }

    public void loadModel(String segModelPath, String posModelPath, String nerModelPath, String vocabPath) {
        vocab = new Vocab(vocabPath);
        segModel.initModel(segModelPath, vocab.getSegLabelNum());
        posModel.initModel(posModelPath, vocab.getPosLabelNum());
        nerPredictor.initModel(nerModelPath, vocab.getNerLabelNum());
    }

    public void loadModel(String modelDir) {
        String segModelPath = Paths.get(modelDir, "seg.pb").toString();
        String posModelPath = Paths.get(modelDir, "pos.pb").toString();
        String nerModelPath = Paths.get(modelDir, "ner.pb").toString();
        String vocabPath = Paths.get(modelDir, "map.zip").toString();
        loadModel(segModelPath, posModelPath, nerModelPath, vocabPath);
    }

    private LSTMLexicalAnalyzer() {
    }

    ;

    private static List<String> strToList(String text) {
        char[] chars = text.toCharArray();
        List<String> charList = new ArrayList<>();
        if (chars.length == 0) {
            return charList;
        }
        for (char c : chars) {
            charList.add(Character.toString(c));
        }
        return charList;
    }

    private List<Word> cutMap(List<Integer> path, List<String> charList) {

        List<Word> words = new ArrayList<>();
        List<String> labels = vocab.tranSegLabel(path);
        StringBuffer word = new StringBuffer();
        String label = "";
        for (int i = 0; i < labels.size(); i++) {
            label = labels.get(i);
            String w = charList.get(i);
            if (label.equals("B")) {
                word.append(w);
            } else if (label.equals("M")) {
                word.append(w);
            } else if (label.equals("S")) {
                words.add(new Word(w));
            } else if (label.equals("E")) {
                word.append(w);
                words.add(new Word(word.toString()));
                word = new StringBuffer();
            }
        }
        if (word.length() != 0) {
            words.add(new Word(word.toString()));
        }
        return words;
    }

    @Override
    public List<List<Word>> cut(List<String> docs) {
        List<List<Integer>> sentList = new ArrayList<>();
        List<List<Word>> res = new ArrayList<>();

        for (String d : docs) {
            List<String> charList = strToList(d);
            List<Integer> charVec = vocab.charToVec(charList);
            if (charVec.size() == 0) {
                continue;
            }
            sentList.add(charVec);
        }

        // if no doc
        if (sentList.size() == 0) {
            return res;
        }
        List<List<Integer>> paths = segModel.predict(sentList);

        int sentSize = docs.size();
        for (int i = 0; i < sentSize; i++) {
            List<Word> wds = cutMap(paths.get(i), strToList(docs.get(i)));
            res.add(wds);
        }
        return res;
    }

    @Override
    public List<List<Word>> cut(String text) {
//        List<String> sents = StringUtil.docToSents(text);
        List<String> sents = new ArrayList<>();
        sents.add(text);
        return cut(sents);
    }

    @Override
    public List<List<Word>> pos(List<String> docs) {
        List<List<Word>> docWords = cut(docs);

        if (docWords.size() == 0) {
            return docWords;
        }

        List<List<Integer>> sentList = new ArrayList<>();
        for (int i = 0; i < docs.size(); i++) {
            List<Word> words = docWords.get(i);
            List<String> wordStrList = new ArrayList<>();
            for (Word w : words) {
                wordStrList.add(w.getContent());
            }
            List<Integer> vec = vocab.wordToVec(wordStrList);
            sentList.add(vec);
        }
        List<List<Integer>> paths = posModel.predict(sentList);
        for (int i = 0; i < docs.size(); i++) {
            List<String> labels = vocab.transPosLabel(paths.get(i));
            List<Word> words = docWords.get(i);
            for (int j = 0; j < words.size(); j++) {
                words.get(j).setPos(labels.get(j));
            }
        }
        return docWords;
    }

    @Override
    public List<List<Word>> pos(String text) {
        List<String> docs = new ArrayList<>();
        docs.add(text);
        return pos(docs);
    }

    @Override
    public List<AnalysisResult> analysis(List<String> docs) {
        List<List<Word>> docWords = pos(docs);
        List<AnalysisResult> res = new ArrayList<>();

        if (docWords.size() == 0) {
            return res;
        }

        List<List<Integer>> sentList = new ArrayList<>();
        for (String d : docs) {
            List<String> charList = strToList(d);
            List<Integer> charVec = vocab.charToVec(charList);
            sentList.add(charVec);
        }
        List<List<Integer>> nerPaths = nerPredictor.predict(sentList);
        for (int i = 0; i < docs.size(); i++) {
            AnalysisResult ares = new AnalysisResult();
            List<String> nerLabel = vocab.transNerLabel(nerPaths.get(i));
            String d = docs.get(i);
            List<Entity> entities = combineNer(strToList(d), nerLabel);
            ares.setEntities(entities);
            ares.setWords(docWords.get(i));
            res.add(ares);
        }
        return res;
    }

    private List<Entity> combineNer(List<String> charList, List<String> nerLabel) {
        String label;
        String tLabel = "";
        String ch;
        List<Entity> entities = new ArrayList<>();
        StringBuffer ner = new StringBuffer();

        for (int i = 0; i < charList.size(); i++) {
            label = nerLabel.get(i);
            ch = charList.get(i);

            if ("O".equals(label)) {
                continue;
            }

            tLabel = label.split("_")[1];
            if (label.startsWith("B")) {
                ner.append(ch);
            } else if (label.startsWith("M")) {
                ner.append(ch);
            } else if (label.startsWith("E")) {
                ner.append(ch);
                Entity entity = new Entity(i - ner.length() + 1, i, ner.toString(), tLabel);
                entities.add(entity);
                ner = new StringBuffer();
                // todo result how to parse
            } else if (label.startsWith("S")) {
                Entity entity = new Entity(i - ch.length() + 1, i, ch, tLabel);
                entities.add(entity);
            }
        }

        if (ner.length() != 0) {
            Entity entity = new Entity(charList.size() - ner.length() + 1, charList.size(), ner.toString(), tLabel);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public List<AnalysisResult> analysis(String text) {
        List<String> docs = new ArrayList<>();
        docs.add(text);
        return analysis(docs);
    }

    @Override
    public List<List<Entity>> ner(List<String> docs) {
        List<List<Integer>> sentList = new ArrayList<>();
        for (String d : docs) {
            if (d.length() == 0) {
                continue;
            }

            List<String> charList = strToList(d);
            List<Integer> charVec = vocab.charToVec(charList);
            sentList.add(charVec);
        }

        List<List<Integer>> nerPaths = nerPredictor.predict(sentList);
        List<List<Entity>> res = new ArrayList<>();

        for (int i = 0; i < sentList.size(); i++) {
            List<String> nerLabel = vocab.transNerLabel(nerPaths.get(i));
            String d = docs.get(i);

            while (d.length() == 0 && i < sentList.size()) {
                i += 1;
                d = docs.get(i);
            }
            List<Entity> ruleEntity = RuleEntityParserFactory.parserAll(d);
            List<Entity> entities = combineNer(strToList(d), nerLabel);
            entities.addAll(ruleEntity);
            res.add(entities);
        }
        return res;
    }

    @Override
    public List<List<Entity>> ner(String text) {
        List<String> docs = new ArrayList<>();
        docs.add(text);
        return ner(docs);
    }

}
