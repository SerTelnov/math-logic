import hw1.OutputChecker;
import hw2.Solver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class MainHW2 {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("input.txt");
        List<String> list = Files
                .lines(path)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> res = new Solver().solve(list);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (String s : res) {
                bw.write(s);
            }
        }
    }
}
