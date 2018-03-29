import org.junit.jupiter.api.Test;
import parser.ExpressionParser;
import parser.expressions.Expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class ParserTester {
    @Test
    public void test01() {
        assertEquals("(->,A,B)", new ExpressionParser().parse("A->B").toTree());
    }

    @Test
    public void test02() {
        assertEquals(
                "(->,P,(->,(!QQ),(|,(&,(!R10),S),(&,(&,(!T),U),V))))",
                new ExpressionParser().parse("P->!QQ->!R10&S|!T&U&V").toTree());
    }

    @Test
    public void test03() {
        assertEquals(
                "(->,(&,(!A),(!B)),(!(|,A,B)))",
                new ExpressionParser().parse("!A&!B->!(A|B)").toTree()
        );
    }
}
