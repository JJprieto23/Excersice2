package com.problema2;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese una expresión matemática:");
        String expresion = scanner.nextLine();
        try {
            double resultado = evaluar(expresion);
            System.out.println("El resultado de la expresión es: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al evaluar la expresión: " + e.getMessage());
        }
        scanner.close();
    }

    public static double evaluar(String expresion) throws Exception {
        List<String> postfija = infijoAPostfijo(expresion);
        return evaluarPostfija(postfija);
    }

    private static List<String> infijoAPostfijo(String expresion) throws Exception {
        List<String> resultado = new ArrayList<>();
        Stack<Character> operadores = new Stack<>();
        StringBuilder numero = new StringBuilder();

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numero.append(c);
            } else {
                if (numero.length() > 0) {
                    resultado.add(numero.toString());
                    numero.setLength(0);
                }

                if (c == '(') {
                    operadores.push(c);
                } else if (c == ')') {
                    while (!operadores.isEmpty() && operadores.peek() != '(') {
                        resultado.add(operadores.pop().toString());
                    }
                    if (operadores.isEmpty() || operadores.pop() != '(') {
                        throw new Exception("Paréntesis desbalanceados");
                    }
                } else if (esOperador(c)) {
                    while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                        resultado.add(operadores.pop().toString());
                    }
                    operadores.push(c);
                }
            }
        }

        if (numero.length() > 0) {
            resultado.add(numero.toString());
        }

        while (!operadores.isEmpty()) {
            char op = operadores.pop();
            if (op == '(') {
                throw new Exception("Paréntesis desbalanceados");
            }
            resultado.add(String.valueOf(op));
        }

        return resultado;
    }

    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private static double evaluarPostfija(List<String> postfija) throws Exception {
        Stack<Double> pila = new Stack<>();
        for (String token : postfija) {
            if (esNumero(token)) {
                pila.push(Double.parseDouble(token));
            } else if (esOperador(token.charAt(0))) {
                double b = pila.pop();
                double a = pila.pop();
                switch (token.charAt(0)) {
                    case '+':
                        pila.push(a + b);
                        break;
                    case '-':
                        pila.push(a - b);
                        break;
                    case '*':
                        pila.push(a * b);
                        break;
                    case '/':
                        if (b == 0) {
                            throw new ArithmeticException("División por cero");
                        }
                        pila.push(a / b);
                        break;
                    default:
                        throw new Exception("Operador desconocido: " + token);
                }
            }
        }
        if (pila.size() != 1) {
            throw new Exception("Expresión inválida");
        }
        return pila.pop();
    }

    private static boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}