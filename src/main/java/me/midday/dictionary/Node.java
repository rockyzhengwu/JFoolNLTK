package me.midday.dictionary;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String content;
    private int depth;
    private Map<String, Node> success = new HashMap<>();
    private Node fail;
    private String emits = null;

    public String getEmits() {
        return emits;
    }

    public void setEmits(String emits) {
        this.emits = emits;
    }

    public Node(){}

    public Node(String content){
        this.content = content;
    }

    public Node(String content, int depth){
        this.content = content;
        this.depth = depth;
    }
    public Map<String, Node> getSuccess(){
        return this.success;
    }

    public void addSuccess(Node node){
        this.success.put(node.content, node);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }


    public Node getFail() {
        return fail;
    }

    public void setFail(Node fail) {
        this.fail = fail;
    }

    public Node getNext(Node node){
        String word = node.content;
        return getNext(word);
    }

    public Node getNext(String word){
        if (this.success.containsKey(word)){
            return this.success.get(word);
        }
        else{
            return null;
        }
    }
}



