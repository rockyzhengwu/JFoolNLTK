package me.midday.lexical;

import me.midday.utils.TFUtil;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TFPredictor {

    protected String modelPath = "";
    protected int numClass = 0;

    protected Session session;
    protected Graph graph = new Graph();
    protected Tensor dropT = Tensor.create(1.0f);
    protected boolean isInit = false;
    protected float[][] tranValue;

    protected Session.Runner runner;

    public TFPredictor() {

    }

    public TFPredictor(String modelPath, int numClass) {
        this.initModel(modelPath, numClass);
    }


    public TFPredictor(byte[] modelByte, int numClass){
        initModel(modelByte, numClass);
    }


    public void initModel(byte[] modelByte, int numClass){
        if (isInit) {
            return;
        }
        this.numClass = numClass;
        this.graph.importGraphDef(modelByte);
        this.session = new Session(graph);
        isInit = true;
    }


    public void initModel(String modelPath, int numClass) {
        if (isInit) {
            return;
        }

        this.numClass = numClass;
        this.modelPath = modelPath;
        byte[] modelByte = null;

        Path path = Paths.get(modelPath);
        try {
            modelByte = Files.readAllBytes(path);
            this.graph.importGraphDef(modelByte);
            this.session = new Session(graph);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("load model " + modelPath.toString() + " error ");
        }
        isInit = true;
    }


    public List<List<Integer>> predict(List<List<Integer>> sents) {
        List<List<Integer>> path = new ArrayList();

        int sentLength = sents.size();
        int maxSentLength = 0;
        int[] sentLengths = new int[sentLength];

        for (int i = 0; i < sentLength; i++) {
            int s = sents.get(i).size();
            sentLengths[i] = s;
            if (maxSentLength < s) {
                maxSentLength = s;
            }
        }

        Tensor inputx = TFUtil.createTensor(sents, maxSentLength);
        Tensor lengths = Tensor.create(sentLengths);
        Tensor logits = session.runner()
                .feed("char_inputs", inputx)
                .feed("dropout", dropT)
                .feed("lengths", lengths)
                .fetch("project/logits").run().get(0);

        if (tranValue == null){
            tranValue = new float[numClass + 1][numClass + 1];
            Tensor trans = session.runner()
                    .feed("char_inputs", inputx)
                    .feed("dropout", dropT)
                    .feed("lengths", lengths)
                    .fetch("crf_loss/transitions").run().get(0);
            trans.copyTo(tranValue);
        }


        float[][][] flog = new float[sentLength][maxSentLength][this.numClass];

        logits.copyTo(flog);

        for (int i = 0; i < flog.length; i++) {
            List<Integer> sentPath = decode(flog[i], tranValue, sentLengths[i]);
            path.add(sentPath);
        }
        return path;
    }

    public List<Integer> decode(float[][] logits, float[][] trans, int sentLength) {
        List<Integer> path = new ArrayList<>();

        float[][] scores = new float[sentLength + 1][numClass + 1];
        int[][] paths = new int[sentLength + 1][numClass + 1];
        float[][] logitsAppend = new float[sentLength + 1][numClass + 1];

        for (int i = 0; i < sentLength + 1; i++) {
            for (int j = 0; j < numClass + 1; j++) {
                if (i == 0) {
                    logitsAppend[i][j] = -1000;
                } else if (j == numClass) {
                    logitsAppend[i][j] = -1000;
                } else {
                    logitsAppend[i][j] = logits[i - 1][j];
                }
            }
        }
        logitsAppend[0][numClass] = 0;

//        System.out.println("trans");
//        for(int i=0; i<numClass + 1; i++){
//            for(int j=0; j<numClass + 1;j++){
//                System.out.print(trans[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println("");
//        }
//
//        System.out.println("logits:");
//        for (int i = 0; i < sentLength + 1; i++) {
//            for (int j = 0; j < numClass + 1; j++) {
//                System.out.print(logitsAppend[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println(" ");
//        }

        for (int i = 0; i < numClass + 1; i++) {
            scores[0][i] = logitsAppend[0][i];
        }

        for (int i = 1; i < sentLength + 1; i++) {
            for (int j = 0; j < numClass + 1; j++) {
                float maxS = -10000;
                for (int t = 0; t < numClass + 1; t++) {
                    float ss = scores[i - 1][t] + trans[t][j];
                    if (ss > maxS) {
                        maxS = ss;
                        paths[i][j] = t;
                    }
                }
                scores[i][j] = maxS + logitsAppend[i][j];
            }
        }

//        System.out.println("scores");
//        for (int i = 0; i < sentLength + 1; i++) {
//            for (int j = 0; j < numClass + 1; j++) {
//                System.out.print(scores[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println("");
//        }
//
//        for (int i = 0; i < sentLength + 1; i++) {
//            for (int j = 0; j < numClass + 1; j++) {
//                System.out.print(paths[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println(" ");
//        }
        // back path
        float maxvalue = -10000;
        int maxPath = 0;
        for (int j = 0; j < numClass; j++) {
            if (scores[sentLength][j] > maxvalue) {
                maxvalue = scores[sentLength][j];
                maxPath = j;
            }
        }
        path.add(maxPath);
        for (int i = sentLength; i > 1; i--) {
            maxPath = paths[i][maxPath];
            path.add(maxPath);
        }
        Collections.reverse(path);
        return path;
    }
}
