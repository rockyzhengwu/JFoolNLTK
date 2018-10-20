package me.midday.dictionary;

import java.util.*;

public class Trie {
    private Node root = new Node("", 0);

    public Trie() {
    }

    public void addWord(String word) {
        int length = word.length();
        Node currentNode = this.root;
        Node nextNode = null;
        for (int i = 0; i < length; i++) {
            String c = String.valueOf(word.charAt(i));
            nextNode = currentNode.getNext(c);
            if (nextNode != null) {
                currentNode = nextNode;
            } else {
                Node tmpNode = new Node(c, currentNode.getDepth() + 1);
                currentNode.addSuccess(tmpNode);
                currentNode = currentNode.getNext(c);
            }
        }
        currentNode.setEmits(word);
    }

    public void createFail() {
        this.root.setFail(this.root);
        Queue<Node> queue = new LinkedList<>();
        Node currentNode = this.root;
        for (Map.Entry<String, Node> entry : currentNode.getSuccess().entrySet()) {
            Node node = entry.getValue();
            node.setFail(currentNode);
            queue.add(node);
        }

        while (!queue.isEmpty()) {
            currentNode = queue.poll();
            for (Map.Entry<String, Node> entry : currentNode.getSuccess().entrySet()) {
                Node targetNode = entry.getValue();
                String word = entry.getKey();
                queue.add(targetNode);
                Node traceNode = currentNode.getFail();
                while (traceNode.getNext(word) == null && traceNode.getDepth()!=0) {
                    traceNode = traceNode.getFail();
                }

                // root
                if (traceNode.getDepth() == 0) {
                    if (traceNode.getNext(word) != null) {
                        targetNode.setFail(traceNode.getNext(word));
                    } else {
                        targetNode.setFail(traceNode);
                    }
                }
                // not root
                else {
                    targetNode.setFail(traceNode.getNext(word));
                }
            }
        }
    }

    public List<Match> parseText(String text) {
        Node currentNode = this.root;
        List<Match> matches = new ArrayList<>();
        Node nextNode;
        for (int i = 0; i < text.length(); i++) {
            String w = String.valueOf(text.charAt(i));
            nextNode = currentNode.getNext(w);
            while (nextNode == null && currentNode.getDepth() != 0) {
                currentNode = currentNode.getFail();
                nextNode = currentNode.getNext(w);
            }
            if (nextNode != null) {
                String emit = nextNode.getEmits();
                if (emit != null) {
                    int start = i - emit.length();
                    Match match = new Match(start+1, i+1, emit);
                    matches.add(match);
                }
                currentNode = nextNode;
            }
        }
        return matches;
    }

}
