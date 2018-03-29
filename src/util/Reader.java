package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Reader {

    private BufferedReader br;
    private StringTokenizer st;

    public Reader() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public Reader(String fileName) throws IOException {
        br = Files.newBufferedReader(Paths.get(fileName));
    }

    public String nextLine() throws IOException {
        return br.readLine();
    }

    public String next() throws IOException {
        while (st == null || !st.hasMoreElements()) {
            st = new StringTokenizer(br.readLine());
        }
        return st.nextToken();
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    public int nextIndex() throws IOException {
        return nextInt() - 1;
    }

    public boolean hasNext() throws IOException {
        if (st == null) {
            st = new StringTokenizer(br.readLine());
        } else if (!st.hasMoreElements()) {
            String nextString = br.readLine();
            if (nextString == null) {
                return false;
            }
            st = new StringTokenizer(nextString);
        }
        return st.hasMoreElements();
    }
}
