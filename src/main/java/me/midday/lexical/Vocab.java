package me.midday.lexical;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class Vocab {

    private DataMapping charToId = new DataMapping();
    private DataMapping wordToId = new DataMapping();
    private DataMapping idToSeg = new DataMapping();
    private DataMapping idToNer = new DataMapping();
    private DataMapping idToPos = new DataMapping();
    private Map<String, Integer> nerSeg = new HashMap<>();

    public Vocab() {

    }

    public Vocab(InputStream in){
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            line = br.readLine();
            parseJson(line);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Vocab(String vocabZipFileName) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(vocabZipFileName);
            InputStream in = zf.getInputStream(zf.getEntry("all_map.json"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            line = br.readLine();
            parseJson(line);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void parseJson(String content) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(content);
        JSONObject jsonObject = (JSONObject) obj;

        Map<String, Integer> wordMap = parseStrMap((JSONObject) jsonObject.get("word_map"));
        Map<String, Integer> charMap = parseStrMap((JSONObject) jsonObject.get("char_map"));

        Map<Integer, String> segMap = parseIdMap((JSONObject) jsonObject.get("seg_map"));
        Map<Integer, String> posMap = parseIdMap((JSONObject) jsonObject.get("pos_map"));
        Map<Integer, String> nerMap = parseIdMap((JSONObject) jsonObject.get("ner_map"));

        charToId.setLabelToid(charMap);
        wordToId.setLabelToid(wordMap);
        idToSeg.setIdTolabel(segMap);
        idToPos.setIdTolabel(posMap);
        idToNer.setIdTolabel(nerMap);
    }

    private Map<Integer, String> parseIdMap(JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        Map<Integer, String> res = new HashMap<>();
        for (String key : keys) {
            String value = (String) jsonObject.get(key);
            Integer k = Integer.valueOf(key);
            res.put(k, value);
        }
        return res;
    }

    private Map<String, Integer> parseStrMap(JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();

        Map<String, Integer> res = new HashMap<>();
        for (String key : keys) {
            Number value = (Number) jsonObject.get(key);
            res.put(key, value.intValue());
        }
        return res;
    }


    public List<Integer> charToVec(List<String> chars) {
        List<Integer> vec;
        vec = chars.stream().map(p -> charToId.getValue(p)).collect(Collectors.toList());
        return vec;
    }


    public List<Integer> wordToVec(List<String> words) {
        List<Integer> vec;
        vec = words.stream().map(w -> wordToId.getValue(w)).collect(Collectors.toList());
        return vec;
    }

    public List<String> tranSegLabel(List<Integer> paths) {
        List<String> labels;
        labels = paths.stream().map(p -> idToSeg.getKey(p)).collect(Collectors.toList());
        return labels;
    }

    public List<String> transPosLabel(List<Integer> paths) {
        List<String> labels;
        labels = paths.stream().map(p -> idToPos.getKey(p)).collect(Collectors.toList());
        return labels;
    }

    public List<String> transNerLabel(List<Integer> paths) {
        List<String> labels;
        labels = paths.stream().map(p -> idToNer.getKey(p)).collect(Collectors.toList());
        return labels;
    }

    public int getSegLabelNum() {
        return idToSeg.getSizes();
    }

    public int getPosLabelNum() {
        return idToPos.getSizes();
    }

    public int getNerLabelNum() {
        return idToNer.getSizes();
    }

    public List<Integer> getNerSegPath(List<Integer> segPath) {
        List<String> segLabel = tranSegLabel(segPath);
        List<Integer> nerSegPath = new ArrayList<>();
        for (String l : segLabel) {
            nerSegPath.add(nerSeg.get(l));
        }
        return nerSegPath;
    }

}
