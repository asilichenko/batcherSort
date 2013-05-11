package sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasicBatcher {
    private static int getStartSize(int N) {
        int t = BigDecimal.valueOf(Math.log10(N)).divide(BigDecimal.valueOf(Math.log10(2)), RoundingMode.CEILING).intValue();
        return BigDecimal.valueOf(2).pow(t).intValue();
    }

    public static <T extends Comparable<T>> List<T> sort(List<T> arr, boolean debugLog) {
        String[] debugArray = null;
        List<T> retval = new ArrayList<T>(arr);
        int startSize = getStartSize(retval.size());
        for (int mask = startSize; mask > 0; mask /= 2) {
            for (int dist = mask,
                         q = startSize,
                         offset = 0;
                 q >= mask;
                 dist = q - mask,
                         q /= 2,
                         offset = mask) {
                if (debugLog) {
                    System.out.println(String.format("\nmask = %d\nq = %d\ndist = %d\noffset = %d", mask, q, dist, offset));
                }

                int toPos = retval.size() - 1 - dist;
                for (int i = 0; i <= toPos; i++) {
                    if (debugLog) {
                        debugArray = new String[retval.size()];
                        Arrays.fill(debugArray, "_");
                    }
                    if (offset == (i & mask)) {
                        if (debugLog) {
                            debugArray[i] = ">";
                            debugArray[i + dist] = "<";
                        }
                        if (retval.get(i).compareTo(retval.get(i + dist)) > 0) {
                            Collections.swap(retval, i, i + dist);
                        }
                    } else {
                        if (debugLog) {
                            debugArray[i] = "S";
                        }
                    }
                    if (debugLog) {
                        System.out.println(String.format("%s\ti = %d, i+dist = %d",
                                Arrays.toString(debugArray), i, i + dist));
                    }
                }
            }
            if (debugLog) System.out.println("\n======= decrease mask by 2 =======");
        }
        return retval;
    }
}
