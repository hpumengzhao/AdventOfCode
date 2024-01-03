import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/day1.in"))) {
            String line;
            int res = 0;
            while ((line = reader.readLine()) != null) {
                res += calibrationValue2(line);
            }
            System.out.println(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static int calibrationValue(String s) {
        int len = s.length();
        int ans = 0;
        for (int i = 0; i < len; i++) {
            if (isNumber(s.charAt(i))){
                ans = s.charAt(i) - '0';
                break;
            }
        }
        for (int i = len - 1; i >= 0; --i) {
            if (isNumber(s.charAt(i))) {
                ans = ans * 10 + s.charAt(i) - '0';
                break;
            }
        }
        return ans;
    }

    private static int calibrationValue2(String s) {
        int len = s.length();

        List<String> englishDigSet = Arrays.asList("one", "two", "three", "four", "five"
                , "six", "seven", "eight", "nine");

        int firstIdx = Integer.MAX_VALUE;
        int lastIdx = -1;
        int firstNumber = -1;
        int lastNumber = -1;
        for (int i = 0; i < 9; i++) {
            String ele = englishDigSet.get(i);
            if (s.contains(ele)) {
                int pos = s.indexOf(ele);
                if (pos < firstIdx) {
                    firstIdx = pos;
                    firstNumber = i + 1;
                }
                pos = s.lastIndexOf(ele);
                if (pos > lastIdx) {
                    lastIdx = pos;
                    lastNumber = i + 1;
                }
            }
        }

        for (int i = 0; i < Math.min(len, firstIdx); i++) {
            if (isNumber(s.charAt(i))) {
                firstNumber = s.charAt(i) - '0';
                break;
            }
        }
        for (int i = len - 1; i >= Math.max(0, lastIdx); i--) {
            if (isNumber(s.charAt(i))) {
                lastNumber = s.charAt(i) - '0';
                break;
            }
        }
        return firstNumber * 10 + lastNumber;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}