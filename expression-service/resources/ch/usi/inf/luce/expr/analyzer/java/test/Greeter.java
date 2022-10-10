package ch.usi.inf.luce.expr.analyzer.core.test;

import java.util.Random;

public final class Greeter {

    private boolean isHappy;

    public String greet(String name) {
        return (Character.isUpperCase(name.charAt(0)) ? "Hello" : "Hi")
                + " "
                + name
                + (isHappy ? "!" : "");
    }

    public int getAge() {
        return new Random().nextInt(100);
    }

    public double solve(double a, double b, double c) {
        return ((b * -1.0) + Math.sqrt(Math.pow(b, 2.0) - (4.0 * a * c))) / (2.0 * a);
    }

    public void changeMood() {
        java.util.function.Function<Boolean, Boolean> inv = (x) -> !x;
        isHappy = inv.apply(isHappy);
    }
}