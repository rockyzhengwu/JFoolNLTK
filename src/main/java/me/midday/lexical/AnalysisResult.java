package me.midday.lexical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnalysisResult implements Serializable{
    private List<Entity> entities;
    private List<Word> words;

    public AnalysisResult() {
        entities = new ArrayList<>();
        words = new ArrayList<>();
    }

    public AnalysisResult(List<Word> words, List<Entity> entities) {
        this.entities = entities;
        this.words = words;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void addWord(Word word) {
        this.words.add(word);
    }


    public List<String> getStrWords() {
        List<String> strWords = new ArrayList<>();
        for (Word wo : words) {
            strWords.add(wo.getContent());
        }
        return strWords;
    }

    public void addPos(List<String> pos) {
        for (int i = 0; i < pos.size(); i++) {
            Word w = words.get(i);
            w.setPos(pos.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuffer worBr = new StringBuffer();
        for (Word w : words) {
            worBr.append(w.toString());
            worBr.append(" ");
        }
        StringBuffer entityBr = new StringBuffer();
        for (Entity entity : entities) {
            entityBr.append(entity.toString());
            entityBr.append(" ");
        }
        return "words: " + worBr.toString() + " entities:" + entityBr.toString();
    }
}
