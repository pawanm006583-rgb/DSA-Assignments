/**
 * DSA-II Assignment 2 solutions.
 * Compile: javac Assignment2.java
 * Run:     java Assignment2
 *
 * Array indices printed by this program are zero-based.
 */
public class Assignment2 {
    private static final class WindowResult {
        final int length;
        final int start;
        final int end;
        WindowResult(int length, int start, int end) {
            this.length = length; this.start = start; this.end = end;
        }
        @Override public String toString() {
            return length == 0 ? "length=0, no non-empty window" :
                    "length=" + length + ", indices=" + start + ".." + end;
        }
    }

    // Assignment 2A: recompute every size-k sum from scratch.
    static long maxFixedWindowReference(int[] values, int k) {
        validateWindowSize(values.length, k);
        long best = Long.MIN_VALUE;
        for (int start = 0; start <= values.length - k; start++) {
            long sum = 0;
            for (int i = start; i < start + k; i++) sum += values[i];
            best = Math.max(best, sum);
        }
        return best;
    }

    // Assignment 2A: keep a running sum, adding the entering value and removing
    // the value that leaves. Long sums avoid overflow for ordinary int arrays.
    static long maxFixedWindow(int[] values, int k) {
        validateWindowSize(values.length, k);
        long sum = 0;
        for (int i = 0; i < k; i++) sum += values[i];
        long best = sum;
        for (int right = k; right < values.length; right++) {
            sum += values[right] - (long) values[right - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    // Assignment 2B: enumerate every start and extend each subarray.
    // The input values must be non-negative and limit must be non-negative.
    static WindowResult longestAtMostKReference(int[] values, long limit) {
        validateNonNegativeInput(values, limit);
        int bestLength = 0, bestStart = -1, bestEnd = -1;
        for (int start = 0; start < values.length; start++) {
            long sum = 0;
            for (int end = start; end < values.length; end++) {
                sum += values[end];
                if (sum <= limit && end - start + 1 > bestLength) {
                    bestLength = end - start + 1;
                    bestStart = start;
                    bestEnd = end;
                }
            }
        }
        return new WindowResult(bestLength, bestStart, bestEnd);
    }

    // Assignment 2B: variable-size window. Correct because values are
    // non-negative: removing from the left cannot increase the running sum.
    static WindowResult longestAtMostK(int[] values, long limit) {
        validateNonNegativeInput(values, limit);
        int left = 0, bestLength = 0, bestStart = -1, bestEnd = -1;
        long sum = 0;
        for (int right = 0; right < values.length; right++) {
            sum += values[right];
            while (left <= right && sum > limit) sum -= values[left++];
            int length = right - left + 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left;
                bestEnd = right;
            }
        }
        return new WindowResult(bestLength, bestStart, bestEnd);
    }

    private static void validateWindowSize(int n, int k) {
        if (k < 1 || k > n) throw new IllegalArgumentException("Require 1 <= K <= array length.");
    }

    private static void validateNonNegativeInput(int[] values, long limit) {
        if (limit < 0) throw new IllegalArgumentException("K must be non-negative.");
        for (int value : values) {
            if (value < 0) throw new IllegalArgumentException("Variable-window input values must be non-negative.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Assignment 2A: maximum fixed window");
        int[] fixedInput = {2, 1, 5, 1, 3, 2};
        System.out.println("Reference max sum: " + maxFixedWindowReference(fixedInput, 3));
        System.out.println("Sliding-window max sum: " + maxFixedWindow(fixedInput, 3));
        System.out.println("Negative values: " + maxFixedWindow(new int[]{-4, -2, -7}, 2));

        System.out.println("\nAssignment 2B: longest subarray with sum at most K");
        int[] variableInput = {1, 2, 1, 1, 3};
        System.out.println("Reference: " + longestAtMostKReference(variableInput, 4));
        System.out.println("Sliding window: " + longestAtMostK(variableInput, 4));
        System.out.println("K=0: " + longestAtMostK(new int[]{4, 2, 1}, 0));
        System.out.println("Zeros: " + longestAtMostK(new int[]{0, 0, 1, 0}, 1));
    }
}