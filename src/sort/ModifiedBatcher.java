package sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModifiedBatcher {

    private static int getStartSize(int N) {
        int t = BigDecimal.valueOf(Math.log10(N)).divide(BigDecimal.valueOf(Math.log10(2)), RoundingMode.CEILING).intValue();
        return BigDecimal.valueOf(2).pow(t).intValue();
    }

    private static void doAction(List<Integer> arr, int blockSize, int offset, int distance) {
        int doubledBlockSize = 2 * blockSize;

        int blockCount = BigDecimal.valueOf(arr.size() - offset - distance)
                .divide(BigDecimal.valueOf(doubledBlockSize)).setScale(0, RoundingMode.UP).intValue();

        for (int blockN = 0, blockPos = offset; blockN < blockCount; blockN++, blockPos = offset + blockN * doubledBlockSize) {// variant 1
//        for (int blockPos = offset; (blockPos + distance) < arr.size(); blockPos += doubledBlockSize) {//variant 2
            for (int i = 0; i < blockSize; i++) {
                int aPos = blockPos + i;
                int bPos = aPos + distance;
                if (bPos >= arr.size()) break;

                if (arr.get(aPos) > arr.get(bPos)) Collections.swap(arr, aPos, bPos);
            }
        }
    }

    public static List<Integer> sort(List<Integer> arr) {
        List<Integer> retval = new ArrayList<Integer>(arr);
        for (int blockSize = getStartSize(retval.size()), counter = 0; blockSize > 0; blockSize /= 2, counter++) {
            doAction(retval, blockSize, 0, blockSize);// цепочка блоков начинается с 0
            for (int step = counter; step > 0; step--) {
                int distance = (int) (Math.pow(2, step) - 1) * blockSize;
                doAction(retval, blockSize, blockSize, distance); // цепочка блоков начинается со смещением в 1 блок
            }
        }
        return retval;
    }
}
