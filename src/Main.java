import parser.ExpressionParser;
import parser.expressions.BinOperation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        ExpressionParser parser = new ExpressionParser();
        System.out.println(((BinOperation) parser.parse(scanner.nextLine())).getRight().toString());
    }
}
