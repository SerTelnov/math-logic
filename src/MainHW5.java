import hw5.ExpressionRefutation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Telnov Sergey on 23.06.2018.
 */
public class MainHW5 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"));
        String input = reader.readLine();

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"));
        writer.write(new ExpressionRefutation().check(input));
        writer.close();
    }
}
