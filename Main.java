public class Main {

    private static final int[] THREAD_COUNTS = {2, 4, 8, 16};
    private static final int OPERATIONS_PER_THREAD = 1000;
    private static final int NUMBER_OF_RUNS = 5;

    public static int calculateValue(int a, int b) {
        return (a * OPERATIONS_PER_THREAD) + (b % OPERATIONS_PER_THREAD);
    }

    public static void main(String[] args) throws InterruptedException 
    {

        System.out.println("Threads | Coarse-Grained Time | Fine-Grained Time");
        System.out.println();
        for (int threadCount : THREAD_COUNTS) {
            long totalCoarseTime = 0;
            long totalFineTime = 0;
            long averageCoarseTime = 0;
            long averageFineTime = 0;
            
            for (int i = 0; i < NUMBER_OF_RUNS; i++) {
                CoarseList cl = new CoarseList();
                Thread[] threads = new Thread[threadCount];

                for (int j = 0; j < threadCount; j++) {
                    final int threadId = j;
                    threads[j] = new Thread(() -> {
                        for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                            int value = calculateValue(threadId, k);

                            if (k%3 == 0) {
                                cl.add(value);
                            } else if (k%3 == 1) {
                                cl.contains(value);
                            } else {
                                cl.remove(value);
                            }
                        }
                    });
                }

                long startTime = System.nanoTime();

                for (int r = 0; r < threadCount; r++) {
                    threads[r].start();
                }

                for (int r = 0; r < threadCount; r++) {
                    threads[r].join();
                }

                long endTime = System.nanoTime();

                totalCoarseTime += (endTime - startTime);
            }

            for (int i = 0; i < NUMBER_OF_RUNS; i++) {
                FineList fl = new FineList();
                Thread[] threads = new Thread[threadCount];

                for (int j = 0; j < threadCount; j++) {
                    final int threadId = j;
                    threads[j] = new Thread(() -> {
                        for (int k = 0; k < OPERATIONS_PER_THREAD; k++) {
                            int value = calculateValue(threadId, k);

                            if (k%3 == 0) {
                                fl.add(value);
                            } else if (k%3 == 1) {
                                fl.contains(value);
                            } else {
                                fl.remove(value);
                            }
                        }
                    });
                }

                long startTime = System.nanoTime();

                for (int r = 0; r < threadCount; r++) {
                    threads[r].start();
                }

                for (int r = 0; r < threadCount; r++) {
                    threads[r].join();
                }

                long endTime = System.nanoTime();

                totalFineTime += (endTime - startTime);
            }
                averageCoarseTime = totalCoarseTime / NUMBER_OF_RUNS;
                averageCoarseTime /= 1000000;
                averageFineTime = totalFineTime / NUMBER_OF_RUNS;
                averageFineTime /= 1000000;

            System.out.printf("%-7d | %-8d | %d%n",
                threadCount,
                averageCoarseTime,
                averageFineTime
            );
        }
    }
}