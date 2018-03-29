import parser.ExpressionParser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("input.txt");
        List<String> list = Files.readAllLines(path);

        String res = new ExpressionParser().parse(list.get(0)).toTree();
        BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"));
        bw.write(res);
        bw.close();
    }
}
