package sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Integer> list = createArrayOf(10);

        List<Integer> list1 = new ArrayList<Integer>(list);

        Collections.sort(list1);
        List<Integer> list2 = ModifiedBatcher.sort(list);
        List<Integer> list3 = BasicBatcher.sort(list, false);
        List<Integer> list4 = new ParallelBatcher().sort(list, 4);

        check(list1, list2, "ModifiedBatcher\t");
        check(list1, list3, "BasicBatcher\t");
        check(list1, list4, "ParallelBatcher\t");
    }

    private static void check(List<Integer> defaultSorted, List<Integer> list, String name) {
        if (!Arrays.equals(defaultSorted.toArray(), list.toArray())) {
            System.err.println(name + list);
        } else System.out.println(name + list);
    }

    private static List<Integer> createArrayOf(int n) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }
}
