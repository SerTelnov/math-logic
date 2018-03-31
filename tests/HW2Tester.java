import hw1.OutputChecker;
import hw2.Solver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class HW2Tester {

    private void customTest(String input, String answer) {
        List<String> result = new Solver().solve(
                Stream
                        .of(input.split("\n"))
                        .collect(Collectors.toList()));
        assertEquals(answer, result.get(0) + result.get(1));
    }

    @Test
    public void test01() {
        customTest(
                "A,A|-A\n" +
                        "A",
                "A|-(A->A)\n" +
                        "(A->(A->A))\n" +
                        "A\n" +
                        "(A->A)");
    }

    @Test
    public void test02() {
        customTest(
                "A|-B->A\n" +
                        "A->B->A\n" +
                        "A\n" +
                        "B->A",
                "|-(A->(B->A))\n" +
                        "((A->(B->A))->(A->(A->(B->A))))\n" +
                        "(A->(B->A))\n" +
                        "(A->(A->(B->A)))\n" +
                        "(A->(A->A))\n" +
                        "((A->(A->A))->((A->((A->A)->A))->(A->A)))\n" +
                        "((A->((A->A)->A))->(A->A))\n" +
                        "(A->((A->A)->A))\n" +
                        "(A->A)\n" +
                        "((A->A)->((A->(A->(B->A)))->(A->(B->A))))\n" +
                        "((A->(A->(B->A)))->(A->(B->A)))\n" +
                        "(A->(B->A))"
        );
    }

    @Test
    public void test03() {
        customTest(
                "A, B, C|-A\n" +
                        "A\n" +
                        "A->C->A\n" +
                        "C->A\n" +
                        "C\n" +
                        "A",
                "A,B|-(C->A)\n" +
                        "(A->(C->A))\n" +
                        "A\n" +
                        "(C->A)"
        );
    }

    @Test
    public void test04() {
        customTest(
                "A,B,C,D,F,E,F,G|-Z",
                "A,B,C,D,F,E,F|-(G->Z)\n"
        );
    }

    @Test
    public void test05() {
        customTest(
                "D, E, A, T, D, T, Y|-Z",
                "D,E,A,T,D,T|-(Y->Z)\n"
        );
    }

    private String joinStrings(List<String> list) {
        StringJoiner joiner = new StringJoiner("\n");
        for (String s : list) {
            joiner.add(s);
        }
        return joiner.toString();
    }

    @Test
    public void mainTests() throws IOException {
        File directory = Paths.get("tests\\hw2").toFile();
        final int count = directory.list().length / 2 + 1;
        StringBuilder errorsInfo = new StringBuilder();
        for (int i = 1; i != count; i++) {
            String testInfo = "at test: '" + i + "'";
            Path path = Paths.get(String.format("tests\\hw2\\%d.in", i));
            List<String> input = Files
                    .lines(path)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            Path resPath = Paths.get(String.format("tests\\hw2\\%d.out", i));
            List<String> output = Files.readAllLines(resPath);

            try {
                customTest(joinStrings(input), joinStrings(output));
            } catch (Throwable e) {
                errorsInfo
                        .append(testInfo)
                        .append(e.getMessage());
            }
        }

        assertTrue(errorsInfo.length() == 0, errorsInfo.toString());
    }
}
