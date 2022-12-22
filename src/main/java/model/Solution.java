package model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public enum ElementType {
        LEFT_BRACKET, RIGHT_BRACKET,
        PLUS, MINUS, MULTIPLY, DIV,
        NUMBER,
        ENDofLine;
    }

    public static class Element {
        ElementType type;
        String value;

        public Element(ElementType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Element(ElementType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "Element{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class ElementBuffer {
        private int pos;

        public List<Element> elements;

        public ElementBuffer(List<Element> elements) {
            this.elements = elements;
        }

        public Element next() {
            return elements.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    public static List<Element> textAnalyze(String expressionText) {
        ArrayList<Element> elements = new ArrayList<>();
        int pos = 0;
        while (pos < expressionText.length()) {
            char c = expressionText.charAt(pos);
            switch (c) {
                case '(':
                    elements.add(new Element(ElementType.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    elements.add(new Element(ElementType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    elements.add(new Element(ElementType.PLUS, c));
                    pos++;
                    continue;
                case '-':
                    elements.add(new Element(ElementType.MINUS, c));
                    pos++;
                    continue;
                case '*':
                    elements.add(new Element(ElementType.MULTIPLY, c));
                    pos++;
                    continue;
                case '/':
                    elements.add(new Element(ElementType.DIV, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expressionText.length()) {
                                break;

                            }
                            c = expressionText.charAt(pos);

                        } while (c <= '9' && c >= '0' || c == '.');
                        elements.add(new Element(ElementType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        pos++;
                    }
            }
        }
        elements.add(new Element(ElementType.ENDofLine, ""));
        return elements;
    }

    public static double expr(ElementBuffer elements) {
        Element element = elements.next();
        if (element.type == ElementType.ENDofLine) {
            return 0;
        } else {
            elements.back();
            return plusminus(elements);
        }
    }

    public static double plusminus(ElementBuffer elements) {
        double value = multdiv(elements);
        while (true) {
            Element element = elements.next();
            switch (element.type) {
                case PLUS:
                    value += multdiv(elements);
                    break;
                case MINUS:
                    value -= multdiv(elements);
                    break;
                case ENDofLine:
                case RIGHT_BRACKET:
                    elements.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + element.value
                            + " at position: " + elements.getPos());
            }
        }
    }

    public static double multdiv(ElementBuffer elements) {
        double value = factor(elements);
        while (true) {
            Element element = elements.next();
            switch (element.type) {
                case MULTIPLY:
                    value *= factor(elements);
                    break;
                case DIV:
                    value /= factor(elements);
                    break;
                case ENDofLine:
                case RIGHT_BRACKET:
                case PLUS:
                case MINUS:
                    elements.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + element.value
                            + " at position: " + elements.getPos());
            }
        }
    }

    public static double factor(ElementBuffer elements) {
        Element element = elements.next();
        switch (element.type) {

            case MINUS:
                double value;
                value = factor(elements);
                return -value;

            case NUMBER:
                return Double.parseDouble(element.value);
            case LEFT_BRACKET:
                value = plusminus(elements);
                element = elements.next();
                if (element.type != ElementType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + element.value
                            + " at position: " + elements.getPos());
                }

                return value;
            default:
                throw new RuntimeException("Unexpected token: " + element.value
                        + " at position: " + elements.getPos());
        }
    }

    public static int quantityOfNumbers(String expressionText) {
        int quantity = 0;

        ArrayList<Element> elements = new ArrayList<>();
        int pos = 0;
        while (pos < expressionText.length()) {
            char c = expressionText.charAt(pos);
            switch (c) {
                case '(':
                    pos++;
                    continue;
                case ')':
                    pos++;
                    continue;
                case '+':
                    pos++;
                    continue;
                case '-':
                    pos++;
                    continue;
                case '*':
                    pos++;
                    continue;
                case '/':
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0' || c == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expressionText.length()) {
                                break;
                            }
                            c = expressionText.charAt(pos);

                        } while (c <= '9' && c >= '0' || c == '.');
                        elements.add(new Element(ElementType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                    }
                    pos++;
                    quantity++;
            }
        }
        return quantity;
    }
}
