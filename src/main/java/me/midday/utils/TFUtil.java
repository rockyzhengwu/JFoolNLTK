package me.midday.utils;

import org.tensorflow.Tensor;

import java.util.List;

public class TFUtil {

    public static  Tensor createTensor(List<List<Integer>> idList, int maxLength){
        int[][] lList = intListToMat(idList, maxLength);
        Tensor tensor = Tensor.create(lList);
        return tensor;
    }

    public static  int[][] intListToMat(List<List<Integer>> intList, int maxLength){

        int sentCount = intList.size();

        int[][] mat = new int[sentCount][maxLength];

        for (int i=0; i < sentCount; i++){
            List<Integer> s = intList.get(i);
            for(int j=0 ; j<maxLength; j++){
                if (j>=s.size()){
                    mat[i][j] = 0;
                }else{
                    mat[i][j] = s.get(j);
                }
            }
        }
        return mat;
    }
}
