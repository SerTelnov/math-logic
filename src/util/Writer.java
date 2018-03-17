package util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Writer {

    private BufferedWriter bw;
    private StringBuilder  sb;

    public Writer() {
        bw = new BufferedWriter(
                new OutputStreamWriter(System.out)
        );
        sb = new StringBuilder();
    }

    public Writer(String fileName) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName))
        );
        sb = new StringBuilder();
    }

    public void println(final int i) {
        sb.append(i).append("\n");
    }

    public void print(final int i) {
        sb.append(i).append(" ");
    }

    public void println(final long l) {
        sb.append(l).append("\n");
    }

    public void println(final String s) {
        sb.append(s).append("\n");
    }

    public void close() throws IOException {
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }
}