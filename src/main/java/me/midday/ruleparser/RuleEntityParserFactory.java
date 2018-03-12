package me.midday.ruleparser;



import me.midday.lexical.Entity;

import java.util.List;
import java.util.regex.Pattern;

public class RuleEntityParserFactory {

    private static final Pattern phonePattern = Pattern.compile("(?:(132|130|131|134|135|136|137|138|139|147|148|150|151|152|157|155|156|" +
                           "158|159|172|178|182|183|184|187|188|198|133|149|" +
                           "153|173|174|177|180|181|189|199|170|176|186|132|185)\\d{8})|(?:\\d{3,4}-\\d{8})");

    private static final Pattern emailPattern = Pattern.compile("\\w+@\\w+\\.\\w+");
    private static final Pattern urlPattern = Pattern.compile("(https?|ftp|file)://[a-zA-z\\d\\.\\-/#\\?=&\\+%]+");

    private static final  String phoneLabel = "phone";
    private static final  String emailLabel = "email";
    private static final  String urlLabel = "url";

    private static final RuleEntityParser phoneParser = new RuleEntityParser(phonePattern, phoneLabel);
    private static final RuleEntityParser emailParser = new RuleEntityParser(emailPattern, emailLabel);
    private static final RuleEntityParser urlParser = new RuleEntityParser(urlPattern, urlLabel);


    public static RuleEntityParser getParser(String name){
        if (null == name){
            return null;
        }
        if (name.equals(phoneLabel)){
            return phoneParser;
        }
        else if(name.equals(emailLabel)){
            return emailParser;
        }
        else if(name.equals(urlLabel)){
            return urlParser;
        }
        else{
            return null;
        }
    }

    public static List<Entity> parserAll(String text){

        List<Entity> emails = emailParser.parse(text);
        List<Entity> phones = phoneParser.parse(text);
        List<Entity> urls = urlParser.parse(text);
        emails.addAll(phones);
        emails.addAll(urls);
        return emails;
    }
}
