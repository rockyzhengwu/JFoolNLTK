package me.midday.lexical;


import java.io.Serializable;

public class Word implements Serializable{
    private String content = "";
    private String pos = "";

    public Word() {
    }

    public Word(String content, String pos) {
        this.content = content;
        this.pos = pos;
    }

    public Word(String content) {
        this.content = content;
        this.pos = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        String str;
        if ("".equals(pos)) {
            str = content;
        } else {
            str = content + "/" + pos;
        }
        return str;
    }
}
