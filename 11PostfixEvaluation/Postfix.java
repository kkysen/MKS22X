import java.util.Stack;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Postfix {
    
    private Postfix() {}
    
    private static final boolean isOperator(final char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }
    
    private static final double parseDouble(final char[] a, final int from, final int to) {
        final boolean negative = a[from] == '-';
        int intPart = 0;
        for (int i = negative ? from + 1 : from; i < to; i++) {
            final char c = a[i];
            if (c == '.') {
                double doublePart = 0;
                for (int j = to - 1; j > i; j--) {
                    final char d = a[j];
                    doublePart = (doublePart + (d - '0')) / 10;
                }
                doublePart += intPart;
                return negative ? -doublePart : doublePart;
            }
            intPart = 10 * intPart + c - '0';
        }
        return negative ? -intPart : intPart;
    }
    
    public static final double eval(final char[] expr) {
        final DoubleStack operands = new DoubleStack();
        int i = 0;
        while (i < expr.length) {
            final int last = i;
            while (++i < expr.length && expr[i] != ' ') {}
            final char c = expr[i - 1];
            if (!isOperator(c)) {
                operands.push(parseDouble(expr, last, i));
            } else {
                final double b = operands.pop();
                final double a = operands.pop();
                double result;
                switch (c) {
                    case '+':
                        result = a + b;
                        break;
                    case '-':
                        result = a - b;
                        break;
                    case '*':
                        result = a * b;
                        break;
                    case '/':
                        result = a / b;
                        break;
                    case '%':
                        result = a % b;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                operands.push(result);
            }
            i++;
        }
        return operands.pop();
    }
    
    public static final double eval(final String expression) {
        return eval(expression.toCharArray());
    }
    
    private static final boolean isOperator(final String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")
                || token.equals("%");
    }
    
    public static final double slowEval(final String expr) {
        final String[] tokens = expr.split(" ");
        final Stack<Double> operands = new Stack<>();
        for (final String token : tokens) {
            if (isOperator(token)) {
                final double b = operands.pop();
                final double a = operands.pop();
                double result;
                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        result = a / b;
                        break;
                    case "%":
                        result = a % b;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                operands.push(result);
            } else {
                operands.push(Double.parseDouble(token));
            }
        }
        return operands.pop();
    }
    
    public static void main(final String[] args) {
        System.out.println(eval("5 3 + 3 * 7 % 4 / 100 * .25 / 5 / -20 -"));
        System.out.println((5 + 3) * 3 % 7 / 4. * 100 / .25 / 5 - -20);
        System.out.println(eval("10 2.0 +") + " is 12.0");
        System.out.println(eval("11 3 - 4 + 2.5 *") + " is 30.0");
        System.out.println(eval("8 2 + 99 9 - * 2 + 9 -") + " is 893.0");
        
        String s = "5 3 + 3 * 7 % 4 / 100 * .25 / 5 / -20 -";
        s += " ";
        final int iters = 1000000;
        final StringBuilder sb = new StringBuilder(s.length() * iters);
        for (int i = 0; i < iters; i++) {
            sb.append(s);
        }
        sb.deleteCharAt(sb.length() - 1);
        
        System.out.println();
        System.out.println("length: " + sb.length());
        
        System.out.println();
        final long start = System.nanoTime();
        System.out.println(eval(sb.toString()));
        final long time = System.nanoTime() - start;
        System.out.println("time: " + time / 1e9 + " sec");
        
        System.out.println();
        final long start1 = System.nanoTime();
        System.out.println(slowEval(sb.toString()));
        final long time1 = System.nanoTime() - start1;
        System.out.println("time: " + time1 / 1e9 + " sec");
        System.out.println("length: " + sb.length());
        
        System.out.println("ratio: " + time1 / time);
        
    }
    
}
