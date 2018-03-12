package me.midday.lexical;

import java.io.Serializable;

public class Entity implements Serializable{
    private String content;
    private int start;
    private int end;
    private String label;

    public Entity() {
    }

    public Entity(int start, int end, String content, String label) {
        this.start = start;
        this.end = end;
        this.content = content;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        String str = "";
        str += this.content + "|" + this.label + "_" + this.start + "_" + this.end;
        return str;
    }
}
