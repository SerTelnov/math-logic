package hw3;

/**
 * Created by Telnov Sergey on 27.04.2018.
 */
public class UnprovableException extends Exception {

    public UnprovableException(String message) {
        super("Высказывание ложно при " + message);
    }
}
