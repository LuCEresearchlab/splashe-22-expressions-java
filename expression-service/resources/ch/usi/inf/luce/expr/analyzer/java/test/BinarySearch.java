package ch.usi.inf.luce.expr.analyzer.core.test;

public class BinarySearch {

    public static boolean binarySearch(float[] array, float x) {
        if (array.length == 0) {
            return false;
        } else {
            int m = array.length / 2;
            if (x > array[m]) {
                float[] a = new float[array.length - (m + 1)];
                System.arraycopy(array, m + 1, a, 0, array.length);
                return binarySearch(a, x);
            } else if (x < array[m]) {
                float[] a = new float[m];
                System.arraycopy(array, 0, a, 0, array.length);
                return binarySearch(a, x);
            } else {
                return true;
            }
        }
    }
}