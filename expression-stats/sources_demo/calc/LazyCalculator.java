class LazyCalculator {
    Calculator calc;
    public LazyCalculator() {
        calc = new Calculator();
    }
    int add(int a, int b) {
        return calc.add(a, b);
    }
}
