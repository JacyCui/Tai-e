/**
 * Simple test case for ifds uninitialized variables analysis.
 */
public class Example {

    public static int g;

    public static void main(String[] args) {
        int x;
        x = 0;
        P(x);
    }

    public static void P(int a) {
        if (a > 0) {
            g = 0;
            a = a - g;
            P(a);
        }
    }

}
