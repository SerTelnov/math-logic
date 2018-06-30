import hw6.KripkeInterpreter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class MainHW6 {
    public static void main(String[] args) throws IOException {
        List<String> list = Files.readAllLines(Paths.get("input.txt"));

        String input = list.get(0);
        List<String> kripkeStr = list.subList(1, list.size());

        BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"));
        bw.write(new KripkeInterpreter().check(input, kripkeStr));
        bw.close();
    }
}
