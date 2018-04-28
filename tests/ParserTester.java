import org.junit.jupiter.api.Test;
import parser.ExpressionParser;
import parser.expressions.Expression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class ParserTester {
    private static final ExpressionParser parser = new ExpressionParser();

    @Test
    public void test01() {
        assertEquals("(->,A,B)", parser.parse("A->B").toTree());
    }

    @Test
    public void test02() {
        assertEquals(
                "(->,P,(->,(!QQ),(|,(&,(!R10),S),(&,(&,(!T),U),V))))",
                parser.parse("P->!QQ->!R10&S|!T&U&V").toTree());
    }

    @Test
    public void test03() {
        assertEquals(
                "(->,(&,(!A),(!B)),(!(|,A,B)))",
                parser.parse("!A&!B->!(A|B)").toTree()
        );
    }

    private void customWriteVariableTest(String s, String[] variables) {
        Expression exp = parser.parse(s);
        Set<String> strVariables = new HashSet<>();
        exp.setVariables(strVariables);
        assertEquals(strVariables, Arrays.stream(variables).collect(Collectors.toSet()));
    }

    @Test
    public void writeVariableTest01() {
        String s = "A->B";
        String[] variables = {"A", "B"};
        customWriteVariableTest(s, variables);
    }

    @Test
    public void writeVariableTest02() {
        String s = "A->B&B2->A->B1";
        String[] variables = {"A", "B", "B2", "B1"};
        customWriteVariableTest(s, variables);
    }
}
