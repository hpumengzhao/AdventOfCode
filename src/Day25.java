import org.omg.CORBA.INTERNAL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day25 {

    private static final Map<String, Integer> index = new HashMap<>();
    private static final int[] fa = new int[1500];
    private static final int[] siz = new int[1500];

    private static final int[][] maze = new int[1500][1500];

    private static final int[] w = new int[1500];

    private static final int[] shrinked = new int[1500];

    private static final int[] vis = new int[1500];

    private static final int[] ord = new int[1500];

    private static int s = -1;

    private static int t = -1;


    private static final List<Edge> edges = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("Y2023/resources/day25.in");
        Scanner sc = new Scanner(input);
        List<String> lines = new ArrayList<>();
        while(sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        List<String> all = lines.stream().flatMap(line -> parseLine(line).stream()).collect(Collectors.toList());
        List<String> allUnique = all.stream().distinct().collect(Collectors.toList());

        int id = 0;
        for (String v: allUnique) {
            index.put(v, ++id);
        }

        int n = allUnique.size();
        System.out.println(n);

        //build graph
        for (String line: lines) {
            List<String> parsed = parseLine(line);
            int siz = parsed.size();
            for (int i = 1; i < siz; i++) {
                int u = index.get(parsed.get(0));
                int v = index.get(parsed.get(i));
                edges.add(new Edge(u, v));
                maze[u][v] = maze[v][u] = 1;
            }
        }

        System.out.println(StoerWagner(n));
    }

    //第x次合并
    private static int MiniCutPhase(int x, int n) {
        for (int i = 0; i <= n; i++) {
            vis[i] = 0;
            w[i] = 0;
        }

        w[0] = -1;
        for (int i = 1; i <= n - x + 1; i++) {//求出排列的顺序，按照权值排序
            int mx = 0;
            for (int j = 1; j <= n; j++) {
                if (shrinked[j] == 0 && vis[j] == 0 && w[j] > w[mx]) {
                    mx = j;
                }
            }
            //当前选择了mx
            vis[mx] = 1;
            ord[i] = mx;
            //更新集合A以及其他点到集合a的距离
            for (int j = 1; j <= n; j++) {
                if (shrinked[j] == 0 && vis[j] == 0) {
                    w[j] += maze[mx][j];
                }
            }
        }
        s = ord[n - x];
        t = ord[n - x + 1];
        return w[t];
    }

    private static int StoerWagner(int n) {
        int minCut = Integer.MAX_VALUE;
        init(n);
        for (int i = 1; i <= n - 1; i++) {
            minCut = Math.min(minCut, MiniCutPhase(i, n));
            if (MiniCutPhase(i, n) == 3) {
                System.out.println(siz[find(t)]);
                return (n - siz[find(t)]) * siz[find(t)];
            }
            shrinked[t] = 1;
            unite(s, t);
            for (int j = 1; j <= n; j++) {
                maze[s][j] += maze[t][j];
                maze[j][s] +=maze[j][t];
            }

        }
        return minCut;
    }

    private static void init(int n) {
        for (int i = 1; i <= n; i++) {
            fa[i] = i;
            siz[i] = 1;
        }
    }

    private static List<String> parseLine(String line) {
        String curr = "";
        List<String> lst = new ArrayList<>();
        int n = line.length();
        for (int i = 0; i < n; i++) {
            if (line.charAt(i) == ' ' || line.charAt(i) == ':') {
                if (!curr.isEmpty()) {
                    lst.add(curr);
                    curr = "";
                }
            } else {
                curr += line.charAt(i);
            }
        }
        lst.add(curr);
        return lst;
    }

    private static int find(int x) {
        if (x == fa[x]) {
            return x;
        }
        return fa[x] = find(fa[x]);
    }

    private static void unite(int u, int v) {
        u = find(u);
        v = find(v);
        if (u == v) {
            return ;
        }
        fa[u] = v;
        siz[v] += siz[u];
        siz[u] = 0;
    }
}

class Edge {
    private int u;
    private int v;
    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }
}
