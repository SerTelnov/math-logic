import hw1.OutputChecker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Telnov Sergey on 28.03.2018.
 */
public class MainHW1 {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("input.txt");
        List<String> list = Files
                .lines(path)
                .filter(it -> !it.isEmpty())
                .collect(Collectors.toList());

        String[] data = new String[list.size()];
        String[] res = new OutputChecker().check(list.toArray(data));

        BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"));
        for (int i = 0; i != res.length; i++) {
            bw.append("(").append(String.valueOf(i + 1)).append(") ");
            bw.append(res[i]);
            bw.append("\n");
        }
        bw.close();
    }
}
