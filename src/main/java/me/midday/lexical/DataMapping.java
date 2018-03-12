package me.midday.lexical;


import java.util.HashMap;
import java.util.Map;

public class DataMapping {
    private String OOV = "<OOV>";
    private Map<String, Integer> labelToid = new HashMap<>();

    public Map<String, Integer> getLabelToid() {
        return labelToid;
    }

    public void setLabelToid(Map<String, Integer> labelToid) {
        this.labelToid = labelToid;
        this.sizes = labelToid.size();
    }

    public Map<Integer, String> getIdTolabel() {
        return idTolabel;
    }

    public void setIdTolabel(Map<Integer, String> idTolabel) {
        this.idTolabel = idTolabel;
        this.sizes = idTolabel.size();

    }

    private Map<Integer, String> idTolabel = new HashMap<>();

    private int sizes;

    public DataMapping() {
    }

    public int getSizes() {
        return sizes;
    }

    public boolean containsKey(String key) {
        return this.labelToid.containsKey(key);
    }

    public boolean containsValue(Integer value) {
        return this.idTolabel.containsKey(value);
    }

    public Integer getValue(String key) {
        if (this.labelToid.containsKey(key)) {
            return this.labelToid.get(key);
        } else {
            return this.labelToid.get(this.OOV);
        }
    }

    public String getKey(Integer value) {
        return this.idTolabel.get(value);
    }

}
