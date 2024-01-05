import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day3 {

    //{{x, from_y, to_y, value}}
    private static List<String> maze;

    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Y2023/resources/day3.in"))) {
            String line;
            maze = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                maze.add(line);
            }
            //Part-1
            System.out.println(calculateSumOfPartNumber(maze));
            //Part-2
            System.out.println(calculateGearRatio(maze));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int calculateSumOfPartNumber(List<String> maze) {
        int sumOfPartNumber = 0;
        List<List<Integer>> numberInfo = extractNumbers(maze);
        for (List<Integer> list: numberInfo) {
            if (isPartNumber(list.get(0), list.get(1), list.get(2), maze.size(), maze.get(0).length())) {
                sumOfPartNumber += list.get(3);
            }
        }
        return sumOfPartNumber;
    }

    private static int calculateGearRatio(List<String> maze) {
        int n = maze.size();
        int m = maze.get(0).length();
        int[][] cnt = new int[n][m];
        int[][] gearRatio = new int[n][m];
        List<List<Integer>> numberInfo = extractNumbers(maze);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                gearRatio[i][j] = 1;
            }
        }

        for (List<Integer> lst: numberInfo) {
            int x = lst.get(0);
            int from = lst.get(1);
            int to = lst.get(2);
            for (int i = Math.max(0, x - 1); i <= Math.min(n - 1, x + 1); i++) {
                for (int j = Math.max(from - 1, 0); j <= Math.min(to + 1, m - 1); j++) {
                    if (isSymbol(maze.get(i).charAt(j))) {
                        cnt[i][j]++;
                        gearRatio[i][j] *= lst.get(3);
                    }
                }
            }
        }

        int sumOfGearRatio = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (cnt[i][j] == 2) {
                    sumOfGearRatio += gearRatio[i][j];
                }
            }
        }

        return sumOfGearRatio;
    }

    private static List<List<Integer>> extractNumbers(List<String> maze) {
        int n = maze.size();
        int m = maze.get(0).length();
        int currValue = 0;
        List<List<Integer>> numberInfo = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int from = -1;
            int to = -1;
            for (int j = 0; j < m; j++) {
                if (isDigit(maze.get(i).charAt(j))) {
                    to = j;
                    if (from == -1) {
                        from = j;
                    }
                    currValue = currValue * 10 + maze.get(i).charAt(j) - '0';
                } else {
                    if (to != -1) {
                        numberInfo.add(Arrays.asList(i, from, to, currValue));
                        from = -1;
                        to = -1;
                        currValue = 0;
                    }
                }
            }
            if (to != -1) {
                numberInfo.add(Arrays.asList(i, from, to, currValue));
                currValue = 0;
            }
        }

        return numberInfo;
    }


    private static boolean isPartNumber(int x, int from, int to, int n, int m) {
        for (int i = Math.max(0, x - 1); i <= Math.min(n - 1, x + 1); i++) {
            for (int j = Math.max(from - 1, 0); j <= Math.min(to + 1, m - 1); j++) {
                if (isSymbol(maze.get(i).charAt(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isSymbol(char c) {
        return c != '.' && ! (c >= '0' && c <= '9');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

}
