import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static int getRandomNumberInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    private static void test() {
        try (FileWriter writer = new FileWriter("test1.txt", false)) {
            int n = 100000;
            int max = 100000;
            writer.write(n);
            writer.append('\n');
            StringBuilder sb = new StringBuilder();
            Set<Integer> set = new HashSet<>(n);
            int m = 0;
            for (int i = 1; i <= n; i++) {
                m = getRandomNumberInt(0, max);
                while (set.contains(m)) {
                    m = getRandomNumberInt(0, max);
                }
                sb.append(m);
                sb.append(" ");
            }
            writer.write(sb.toString().trim());
            writer.append('\n');
            writer.flush();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
       // test();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("test1.txt")));

        int n = Integer.parseInt(reader.readLine());
        String[] parts = reader.readLine().split(" ");
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(parts[i]);
        }
        long result = solveHeap(arr, n);
        System.out.println(result);
    }
    public static long solveHeap(int arr[], int n) {
        PriorityQueue<Double> g = new PriorityQueue<>();
        PriorityQueue<Double> s = new PriorityQueue<>();
        long result = 0;
        for (int i = 0; i < n; i++) {
            s.add(-1.0 * arr[i]);
            g.add(-1.0 * s.poll());
            if (g.size() > s.size()) {
                s.add(-1.0 * g.poll());
            }
            result += (-1.0 * s.peek());
        }
        return result;
    }

    public static long solve(int[] arr, int n) {
        long result = 0;
        int[] curArray = new int[n];
        for (int i = 0; i < n; i++) {
            curArray[i] = arr[i];
            int mediana = 0;
            if ((i + 1) % 2 == 1) {
                mediana = quickSelect(curArray, (i + 1) / 2, i);
            } else {
                mediana = quickSelect(curArray, i / 2, i);
            }
            result += mediana;
        }
        return result;
    }
    private static int quickSelect(int[] arr, int k, int high) {
        int low = 0;
        //int high = arr.length - 1;
        while (low < high) {
            int pivotIndex = partition(arr, low, high);
            if (pivotIndex < k) {
                low = pivotIndex + 1;
            } else if (pivotIndex > k) {
                high = pivotIndex - 1;
            } else {
                return arr[pivotIndex];
            }
        }
        return arr[low];
    }
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, high);
        return i;
    }
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static long solveFast(int n, List<Integer> array) {
        LocalDateTime from = LocalDateTime.now();
        long result = 0;
        int prevMediana = 0;
        Integer lastValue = 0;
        List<Integer> curSort = new ArrayList<>(array);
        Collections.sort(curSort);
        boolean even = (n % 2 == 0);
        int index = (even) ? (n - 1) / 2 : n / 2;
        prevMediana = curSort.get(index);
        result += prevMediana;
        lastValue = array.get(n - 1);

        LocalDateTime to1 = LocalDateTime.now();
        System.out.println(Duration.between(from, to1).toMillis());
        for (int i = n - 1; i >= 1; i--) {
            even = !even;
            curSort.remove(lastValue);
            if ((!even) && lastValue <= prevMediana) {
                prevMediana = curSort.get(i / 2);
            } else if (even && lastValue >= prevMediana) {
                prevMediana = curSort.get((i - 1) / 2);
            }
            result += prevMediana;
            lastValue = array.get(i - 1);
        }
        LocalDateTime to = LocalDateTime.now();
        System.out.println(Duration.between(from, to).toMillis());
        return result;
    }

    public static long solveSlow(int n, int[] array) {
        long result = 0;
        int prevMediana = 0;
        int prevValue = 0;
        for (int i = n; i >= 1; i--) {
            if (i == n) {
                int[] cur = Arrays.copyOf(array, i);
                Arrays.sort(cur);
                if (i % 2 == 0) {
                    prevMediana = cur[(i - 1) / 2];
                } else {
                    prevMediana = cur[i / 2];
                }
                result += prevMediana;
                prevValue = array[i - 1];
                continue;
            }

            if (prevValue < prevMediana && (i % 2 != 0)) {
                int[] cur = Arrays.copyOf(array, i);
                Arrays.sort(cur);
                prevMediana = cur[i / 2];
            } else if (prevValue > prevMediana && (i % 2 == 0)) {
                int[] cur = Arrays.copyOf(array, i);
                Arrays.sort(cur);
                prevMediana = cur[(i - 1) / 2];
            } else if (prevValue == prevMediana) {
                int[] cur = Arrays.copyOf(array, i);
                Arrays.sort(cur);
                if (i % 2 == 0) {
                    prevMediana = cur[(i - 1) / 2];
                } else {
                    prevMediana = cur[i / 2];
                }
            }
            result += prevMediana;
            prevValue = array[i - 1];
        }
        return result;
    }
}