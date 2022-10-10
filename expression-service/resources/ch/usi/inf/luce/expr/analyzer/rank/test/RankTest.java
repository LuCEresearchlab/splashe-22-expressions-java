package ch.usi.inf.luce.expr.analyzer.rank.test;

class RankTest {

    void main() {
        int a = 1 + 2;
        int b = 1 + 2;
        int c = a + b / 2;
        String d = "hello".length() > c + b ? "no" : "yes";

        if (d.length() < a) {
            char e = (char) ((int) d.charAt(0) - 2);
        }

        float f = 1f + 2f + 3f + 4f + 5f + 6f;
    }
}