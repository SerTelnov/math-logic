package parser.expressions;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public interface Expression {
    String toTree();
    void setVariables(Set<String> variables);
}
