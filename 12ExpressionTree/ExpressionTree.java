import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import stacks.BoolStack;
import stacks.CharStack;
import stacks.Stack;

/**
 * This expression tree is a binary tree that represents
 * an arithmetic expression. The results are always doubles,
 * no int operations nor booleans/strings etc.
 * -Example:
 * @formatter:off
 *   +
 *  / \
 * 3   *
 *    / \
 *   2  10
 * @formatter:on
 * - Any node with children is treated like an operator.
 * - Any node without children is treated like a value.
 *
 * Assumptions that can be made:
 * - A TreeNode has 0 or 2 children.
 * - A TreeNode with children must have an operator.
 * - For now all operators are binary and are one of these: '+', '-', '*', '/'.
 * 
 * @author Khyber Sen
 */
public class ExpressionTree {
    
    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final char[] OPERATORS = {'+', '-', '*', '/', '%'};
    
    private final double value;
    private final char operator;
    
    private final ExpressionTree left;
    private final ExpressionTree right;
    
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
    
    public static final ExpressionTree postfix(final char[] expr) {
        final Deque<ExpressionTree> stack = new ArrayDeque<>();
        int i = 0;
        while (i < expr.length) {
            final int last = i;
            while (++i < expr.length && expr[i] != ' ') {}
            final char c = expr[i - 1];
            if (!isOperator(c)) {
                stack.push(new ExpressionTree(parseDouble(expr, last, i)));
            } else {
                final ExpressionTree right = stack.pop();
                stack.push(new ExpressionTree(c, stack.pop(), right));
            }
            i++;
        }
        return stack.pop();
    }
    
    public static final ExpressionTree prefix(final char[] expr) {
        final Deque<ExpressionTree> stack = new ArrayDeque<>();
        int i = expr.length;
        while (i >= 0) {
            final int last = i;
            i--;
            while (--i >= 0 && expr[i] != ' ') {}
            final char c = expr[i + 1];
            if (!isOperator(c)) {
                stack.push(new ExpressionTree(parseDouble(expr, i + 1, last)));
            } else {
                stack.push(new ExpressionTree(c, stack.pop(), stack.pop()));
            }
        }
        return stack.pop();
    }
    
    public static final ExpressionTree infix(final char[] expr) {
        final Stack<ExpressionTree> stack = new Stack<>();
        final CharStack operators = new CharStack();
        for (int i = 0; i < expr.length; i++) {
            final char c = expr[i];
            switch (c) {
                case ' ':
                    continue;
                case ')':
                    char operator;
                    while ((operator = operators.pop()) != '(') {
                        final ExpressionTree right = stack.pop();
                        stack.push(new ExpressionTree(operator, stack.pop(), right));
                    }
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                    i++; // skip space
                case '(':
                    operators.push(c);
                    break;
                default: // number
                    final int last = i;
                    char b;
                    while ((b = expr[i]) != ' ' && b != ')') {
                        i++;
                    }
                    final double d = parseDouble(expr, last, i);
                    stack.push(new ExpressionTree(d));
                    if (b == ')') {
                        i--; // no space between number and ')'
                    }
                    break;
            }
        }
        return stack.pop();
    }
    
    public static final ExpressionTree postfix(final String expression) {
        return postfix(expression.toCharArray());
    }
    
    public static final ExpressionTree prefix(final String expression) {
        return prefix(expression.toCharArray());
    }
    
    public static final ExpressionTree infix(final String expression) {
        return infix(expression.toCharArray());
    }
    
    public static final ExpressionTree random(final int size) {
        final StringBuilder sb = new StringBuilder();
        sb.append(RANDOM.nextInt());
        sb.append(' ');
        for (int i = 0; i < size; i++) {
            sb.append(RANDOM.nextInt());
            sb.append(' ');
            sb.append(OPERATORS[RANDOM.nextInt(OPERATORS.length)]);
            sb.append(' ');
        }
        return postfix(sb.toString());
    }
    
    private ExpressionTree(final double value, final char operator, final ExpressionTree left,
            final ExpressionTree right) {
        this.value = value;
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
    
    public ExpressionTree(final double value) {
        this(value, (char) 0, null, null);
    }
    
    public ExpressionTree(final char operator, final ExpressionTree left,
            final ExpressionTree right) {
        this(0, operator, left, right);
    }
    
    private boolean isOperator() {
        return operator != 0;
    }
    
    private boolean isValue() {
        return !isOperator();
    }
    
    private void appendValue(final StringBuilder sb) {
        final int intValue = (int) value;
        if (Math.rint(value) == value) {
            sb.append(intValue);
        } else {
            sb.append(value);
        }
    }
    
    private final void toStringInfix(final StringBuilder sb) {
        if (isValue()) {
            appendValue(sb);
        } else {
            sb.append('(');
            left.toStringInfix(sb);
            sb.append(' ');
            sb.append(operator);
            sb.append(' ');
            right.toStringInfix(sb);
            sb.append(')');
        }
    }
    
    private final void toStringPostfix(final StringBuilder sb) {
        if (isValue()) {
            appendValue(sb);
        } else {
            left.toStringPostfix(sb);
            sb.append(' ');
            right.toStringPostfix(sb);
            sb.append(' ');
            sb.append(operator);
        }
    }
    
    private final void toStringPrefix(final StringBuilder sb) {
        if (isValue()) {
            appendValue(sb);
        } else {
            sb.append(operator);
            sb.append(' ');
            left.toStringPrefix(sb);
            sb.append(' ');
            right.toStringPrefix(sb);
        }
    }
    
    /**
     * The sample tree at the top would be: "( 3 + (2 * 10))"
     * 
     * @return the expression as an infix notation string with parenthesis
     */
    public String toStringInfix() {
        final StringBuilder sb = new StringBuilder();
        toStringInfix(sb);
        //// delete surrounding ()
        //sb.deleteCharAt(0);
        //sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * The sample tree would be: "3 2 10 * +"
     * 
     * @return the expression as a postfix notation string without parenthesis
     */
    public String toStringPostfix() {
        final StringBuilder sb = new StringBuilder();
        toStringPostfix(sb);
        return sb.toString();
    }
    
    /**
     * The sample tree would be: "+ 3 * 2 10"
     * 
     * @return the expression as a prefix notation string without parenthesis
     */
    public String toStringPrefix() {
        final StringBuilder sb = new StringBuilder();
        toStringPrefix(sb);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toStringInfix();
    }
    
    public double evaluateIter() {
        final Stack<ExpressionTree> tree = new Stack<>();
        final BoolStack evaledRight = new BoolStack();
        final ExpressionTree expr = this;
        for (;;) {
            break;
        }
        return 0; // TODO
    }
    
    /**
     * @return the value of the expression tree
     */
    public double evaluate() {
        if (isValue()) {
            return value;
        }
        final double a = left.evaluate();
        final double b = right.evaluate();
        final double result;
        switch (operator) {
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
        return result;
    }
    
    public static void test(final ExpressionTree expr) {
        System.out.println(expr.toStringInfix());
        System.out.println(expr.toStringPostfix());
        System.out.println(expr.toStringPrefix());
        System.out.println(expr.evaluate());
        System.out.println(expr.evaluate() == StackCalc.eval(expr.toStringPostfix()));
        System.out.println(expr.evaluate() == postfix(expr.toStringPostfix()).evaluate());
        System.out.println(expr.evaluate() == prefix(expr.toStringPrefix()).evaluate());
        //System.out.println(prefix(expr.toStringPrefix()));
        System.out.println(expr.evaluate() == infix(expr.toStringInfix()).evaluate());
        //System.out.println(infix(expr.toStringInfix()));
        System.out.println();
    }
    
    public static void main(final String[] args) {
        final ExpressionTree expr = //
                new ExpressionTree('+',
                        new ExpressionTree(3),
                        new ExpressionTree('*',
                                new ExpressionTree(2),
                                new ExpressionTree(10)));
        System.out.println(expr.evaluate());
        System.out.println(expr);
        System.out.println(expr.toStringPostfix());
        System.out.println();
        
        final ExpressionTree a = new ExpressionTree(4.0);
        final ExpressionTree b = new ExpressionTree(2.0);
        
        final ExpressionTree c = new ExpressionTree('+', a, b);
        test(c);
        
        final ExpressionTree d = new ExpressionTree('*', c, new ExpressionTree(3.5));
        test(d);
        
        ExpressionTree ex = new ExpressionTree('-', d, new ExpressionTree(1.0));
        test(ex);
        
        ex = new ExpressionTree('+', new ExpressionTree(1.0), ex);
        test(ex);
        
        ex = new ExpressionTree('/', ex, new ExpressionTree(2.0));
        test(ex);
        
        final ExpressionTree bigExpr = random(1000);
        System.out.println(bigExpr);
        System.out.println(bigExpr.evaluate());
    }
    
}