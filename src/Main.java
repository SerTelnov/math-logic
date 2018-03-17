import parser.ExpressionParser;
import util.Reader;
import util.Writer;

import java.io.IOException;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader("input.txt");
        Writer writer = new Writer("output.txt");

        writer.println(new ExpressionParser().parse(reader.nextLine()).toStr());
        writer.close();
    }
}
