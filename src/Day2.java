import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day2 {
    private static int RED = 12;
    private static int GREEN = 13;

    private static int BLUE = 14;
    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Y2023/resources/day2.in"))) {
            String line;
            int ans = 0;
            long sumOfPower = 0;
            while ((line = bufferedReader.readLine()) != null) {
                line += ";";
                int res = possibleForWholeGame(line);
                sumOfPower += getPowerOfWholeGame(line);
                System.out.println(getPowerOfWholeGame(line));
                if (res != -1) {
                    ans += res;
                }
            }
//            System.out.println(ans);
            System.out.println(sumOfPower);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static long getPowerOfWholeGame(String gameString) {
        List<List<Integer>> parsedGame = parseGame(gameString);

        int MAXR = 0;
        int MAXG = 0;
        int MAXB = 0;
        for (int i = 1; i < parsedGame.size(); i++) {
            MAXR = Math.max(MAXR, parsedGame.get(i).get(0));
            MAXG = Math.max(MAXG, parsedGame.get(i).get(1));
            MAXB = Math.max(MAXB, parsedGame.get(i).get(2));
        }
        return (long) MAXR * MAXG * MAXB;
    }


    private static int possibleForWholeGame(String gameString) {
        List<List<Integer>> parsedGame = parseGame(gameString);
        int gameId = parsedGame.get(0).get(0);

        int siz = parsedGame.size();
        for (int i = 1; i < siz; i++) {
            if (!validForSingleSelection(parsedGame.get(i))) {
                return -1;
            }
        }

        return gameId;
    }


    private static boolean validForSingleSelection(List<Integer> colorBalls) {
        return colorBalls.get(0) <= RED && colorBalls.get(1) <= GREEN && colorBalls.get(2) <= BLUE;
    }

    private static List<List<Integer>> parseGame(String gameString) {
        int gameId = 0;
        int len = gameString.length();
        int curr = 0;
        for (int i = 5; i < len; i++) {
            if (':' == gameString.charAt(i)) {
                curr = i;
                break;
            } else {
                gameId = gameId * 10 + gameString.charAt(i) - '0';
            }
        }

        List<List<Integer>> balls = new ArrayList<>();
        balls.add(Collections.singletonList(gameId));

        int[] colorNumber = new int[3];
        int currCount = 0;
        StringBuilder currColor = new StringBuilder();
        for (int i = curr; i < len; i++) {
            char c = gameString.charAt(i);
            if (isDigit(c)) {
                currCount = currCount * 10 + c - '0';
            } else if (isAlpha(c)) {
                currColor.append(c);
            } else if (',' == c) {
                int colorId = getColorId(currColor.toString());
                assert (colorId != -1);
                colorNumber[colorId] += currCount;
                currColor = new StringBuilder();
                currCount = 0;
            } else if (';' == c) {

                int colorId = getColorId(currColor.toString());
                assert (colorId != -1);
                colorNumber[colorId] += currCount;

                balls.add(Arrays.asList(colorNumber[0], colorNumber[1], colorNumber[2]));
                for (int j = 0; j < 3; j++) {
                    colorNumber[j] = 0;
                }
                currColor = new StringBuilder();
                currCount = 0;
            }
        }
        return balls;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static int getColorId(String color) {
        if ("red".equals(color)) {
            return 0;
        } else if ("green".equals(color)) {
            return 1;
        } else if ("blue".equals(color)) {
            return 2;
        }
        return -1;
    }
}
