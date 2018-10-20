package me.midday.ruleparser;



import me.midday.lexical.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleEntityParser {

    private Pattern  pattern ;
    private String label;

    public RuleEntityParser(){}


    public RuleEntityParser(Pattern pattern, String label){
        this.pattern = pattern;
        this.label = label;
    }


    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<Entity> parse(String text){
        Matcher matcher = pattern.matcher(text);
        List<Entity> entities = new ArrayList<>();
        while (matcher.find()){
            String content = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            Entity entity = new Entity(start, end, content, label);
            entities.add(entity);
        }
        return entities;
    }

}
