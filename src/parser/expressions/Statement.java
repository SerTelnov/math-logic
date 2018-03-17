package parser.expressions;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Statement implements Expression {
    private String name;

    public Statement(String name) {
        this.name = name;
    }

    @Override
    public String toStr() {
        return name;
    }
}
