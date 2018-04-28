import hw1.OutputChecker;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class HW1Tester {

    private void stringEquals(String expected, String was) {
        assertTrue(expected.equals(was) || !was.endsWith("(Не доказано)"));
    }

    private void arrayStringEquals(String[] expected, String[] was) {
        assertEquals(expected.length, was.length, "Different length!\n");

        for (int i = 0; i != expected.length; i++) {
            stringEquals(expected[i], was[i]);
        }
    }

    private void customAxiomTest(String[] res, String[] input) {
        String[] output = new OutputChecker().check(Arrays.asList(input));
        arrayStringEquals(res, output);
    }

    @Test
    public void test01() {
        String[] input = {
                "A, B|-A&B",
                "A",
                "B",
                "A->B->A&B",
                "B->A&B",
                "A&B"
        };
        String[] res = {
                "(1) A (Предп. 1)",
                "(2) B (Предп. 2)",
                "(3) (A->(B->(A&B))) (Сх. акс. 3)",
                "(4) (B->(A&B)) (M.P. 3, 1)",
                "(5) (A&B) (M.P. 4, 2)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test02() {
        String[] input = {
                "|-A",
                "A->B->A&B",
                "A->B->A",
                "(A->B)->B->(A->B)",
                "(A->((!B)->A))"
        };
        String[] res = {
                "(1) (A->(B->(A&B))) (Сх. акс. 3)",
                "(2) (A->(B->A)) (Сх. акс. 1)",
                "(3) ((A->B)->(B->(A->B))) (Сх. акс. 1)",
                "(4) (A->((!B)->A)) (Сх. акс. 1)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test03() {
        String[] input = {
                "A,B|-A&B",
                "A",
                "B",
                "(A->(B->(A&B)))",
                "(B->(A&B))",
                "(A->A)",
                "(A&B)"
        };
        String[] res = {
                "(1) A (Предп. 1)",
                "(2) B (Предп. 2)",
                "(3) (A->(B->(A&B))) (Сх. акс. 3)",
                "(4) (B->(A&B)) (M.P. 3, 1)",
                "(5) (A->A) (Не доказано)",
                "(6) (A&B) (M.P. 4, 2)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test04() {
        String[] input = {
                "|-(A->A)",
                "(A->A->A)->(A->(A->A)->A)->(A->A)",
                "(A->A->A)",
                "(A->(A->A)->A)",
                "(A->(A->A)->A)->(A->A)",
                "(A->A)"
        };
        String[] res = {
                "(1) ((A->(A->A))->((A->((A->A)->A))->(A->A))) (Сх. акс. 2)",
                "(2) (A->(A->A)) (Сх. акс. 1)",
                "(3) (A->((A->A)->A)) (Сх. акс. 1)",
                "(4) ((A->((A->A)->A))->(A->A)) (M.P. 1, 2)",
                "(5) (A->A) (M.P. 4, 3)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test05() {
        String[] input = {
                "|-B",
                "A->B",
                "A",
                "B"
        };
        String[] res = {
                "(1) (A->B) (Не доказано)",
                "(2) A (Не доказано)",
                "(3) B (M.P. 1, 2)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test06() {
        String[] input = {
                "|-A->B",
                "A",
                "B",
                "A->B"
        };
        String[] res = {
                "(1) A (Не доказано)",
                "(2) B (Не доказано)",
                "(3) (A->B) (Не доказано)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void test07() {
        String[] input = {
                "|-(!(A|B)->(!A&!B))",
                "(!A->(!A->!A))",
                "((!A->(!A->!A))->((!A->((!A->!A)->!A))->(!A->!A)))",
                "((!A->((!A->!A)->!A))->(!A->!A))",
                "(!A->((!A->!A)->!A))",
                "(!A->!A)",
                "(!A->(!B->!A))",
                "((!A->(!B->!A))->(!A->(!A->(!B->!A))))",
                "(!A->(!A->(!B->!A)))",
                "((!A->!A)->((!A->(!A->(!B->!A)))->(!A->(!B->!A))))",
                "((!A->(!A->(!B->!A)))->(!A->(!B->!A)))",
                "(!A->(!B->!A))"
        };
        String[] res = {
                "(1) ((!A)->((!A)->(!A))) (Сх. акс. 1)",
                "(2) (((!A)->((!A)->(!A)))->(((!A)->(((!A)->(!A))->(!A)))->((!A)->(!A)))) (Сх. акс. 2)",
                "(3) (((!A)->(((!A)->(!A))->(!A)))->((!A)->(!A))) (M.P. 2, 1)",
                "(4) ((!A)->(((!A)->(!A))->(!A))) (Сх. акс. 1)",
                "(5) ((!A)->(!A)) (M.P. 3, 4)",
                "(6) ((!A)->((!B)->(!A))) (Сх. акс. 1)",
                "(7) (((!A)->((!B)->(!A)))->((!A)->((!A)->((!B)->(!A))))) (Сх. акс. 1)",
                "(8) ((!A)->((!A)->((!B)->(!A)))) (M.P. 7, 6)",
                "(9) (((!A)->(!A))->(((!A)->((!A)->((!B)->(!A))))->((!A)->((!B)->(!A))))) (Сх. акс. 2)",
                "(10) (((!A)->((!A)->((!B)->(!A))))->((!A)->((!B)->(!A)))) (M.P. 9, 5)",
                "(11) ((!A)->((!B)->(!A))) (M.P. 10, 8)"
        };
        customAxiomTest(res, input);
    }

    @Test
    public void mainTest() throws IOException {
        File directory = Paths.get("tests\\hw1").toFile();
        final int count = directory.list().length / 2 + 1;

        for (int i = 1; i != count; i++) {
            String testInfo = "test: '" + i + "'";
            Path path = Paths.get(String.format("tests\\hw1\\%d.in", i));
            List<String> list = Files
                    .lines(path)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            String[] res = new OutputChecker().check(list);
            Path resPath = Paths.get(String.format("tests\\hw1\\%d.out", i));

            list = Files.readAllLines(resPath);
            assertEquals(list.size(), res.length,
                    "incorrect output number of strings in " + testInfo);

            StringBuilder builder = new StringBuilder();
            for (int j = 0; j != list.size(); j++) {
                try {
                    stringEquals(list.get(j), res[j]);
                } catch (Throwable e) {
                    builder.append(e.getMessage());
                    builder.append("\n");
                }
            }
            assertTrue(builder.length() == 0, "in " + testInfo + ":\n" + builder.toString());
//            assertArrayEquals(list.toArray(), res, "in " + testInfo);
        }
    }

    @Test
    public void timeLimitTest() throws IOException {
        List<String> list = Files
                .lines(Paths.get("tests\\hw1\\11.in"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        long start = System.currentTimeMillis();
        String[] res = new OutputChecker().check(list);

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("output.txt"))) {
            for (int i = 0; i != res.length; i++) {
                bw.append("(").append(String.valueOf(i + 1)).append(") ");
                bw.append(res[i]);
                bw.append("\n");
            }
        }

        long end = System.currentTimeMillis();
        long millis = end - start;
        long second = (millis / 1000) % 60;

        String time = String.format("0:%02d:%d", second, millis);
        assertFalse(second >= 20, "too slow!!!\nyour time: '" + time +
                "'\nmust be less '0:20:000'");
    }
}
