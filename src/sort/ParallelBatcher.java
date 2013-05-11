package sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelBatcher {
    private int getStartSize(int N) {
        int t = BigDecimal.valueOf(Math.log10(N)).divide(BigDecimal.valueOf(Math.log10(2)), RoundingMode.CEILING).intValue();
        return BigDecimal.valueOf(2).pow(t).intValue();
    }

    class Task<T extends Comparable<T>> implements Runnable {
        private List<T> arr;
        private int d;
        private int p;
        private int r;
        private int start;
        private int end;

        public Task(List<T> arr, int p, int d, int r, int start, int end) {
            this.arr = arr;
            this.p = p;
            this.d = d;
            this.r = r;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                if ((i & p) == r) {
                    if (arr.get(i).compareTo(arr.get(i + d)) > 0) Collections.swap(arr, i, i + d);
                }
            }
        }
    }

    public <T extends Comparable<T>> List<T> sort(List<T> arr, int nThreads) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        List<T> retval = new ArrayList<T>(arr);
        int startSize = getStartSize(retval.size());
        for (int mask = startSize; mask > 0; mask /= 2) {
            for (int dist = mask, q = startSize, offset = 0;
                 q >= mask;
                 dist = q - mask, q /= 2, offset = mask) {

                List<Future> futures = goOverArray(retval, nThreads, threadPool, mask, offset, dist);

                // wait for all threads finish their task
                for (Future future : futures) {
                    future.get();
                }
            }
        }
        threadPool.shutdown();
        return retval;
    }

    private <T extends Comparable<T>> List<Future> goOverArray(List<T> arr, int nThreads, ExecutorService service, int p, int r, int d) {
        int n = arr.size() - d;
        int start = 0;
        int end;
        List<Future> futures = new ArrayList<Future>();
        for (int i = nThreads; i > 0; i--) {
            int size = n / i;
            n -= size;

            end = start + size;
            futures.add(service.submit(new Task<T>(arr, p, d, r, start, end)));
            start = end;
        }
        return futures;
    }
}
