package tests;

import org.junit.jupiter.api.Test;
import parser.ExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class ParserTester {
    @Test
    public void test01() {
        assertEquals("(->,a,b)", new ExpressionParser().parse("a->b").toStr());
    }

    @Test
    public void test02() {
        assertEquals(
                "(->,P,(->,(!QQ),(|,(&,(!R10),S),(&,(&,(!T),U),V))))",
                new ExpressionParser().parse("P->!QQ->!R10&S|!T&U&V").toStr());
    }

    @Test
    public void test03() {
        assertEquals(
                "(->,(&,(!A),(!B)),(!(|,A,B)))",
                new ExpressionParser().parse("!A&!B->!(A|B)").toStr()
        );
    }
}
