package org.example;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiltersParser {
    String[] allFilters;
    ArrayList<String> operands;

    FiltersParser (String filters) {
        operands = new ArrayList<>();
        Matcher m = Pattern.compile("&\\s*|\\|\\|\\s*").matcher(filters);
        this.allFilters = filters.split("\\|\\||&");
        while (m.find()) {
            operands.add(m.group());
        }
    }

    private boolean match(String[] columns, String filter) {
        if (filter == null || filter.length() == 0) {
            return true;
        }
        Matcher m = Pattern.compile("column\\[(\\d+)](<>|>|=|<)(\\d+|.\\w+.)").matcher(filter);
        m.find();
        int columnIndex;
        String operator;

        try {
            columnIndex = Integer.parseInt(m.group(1)) - 1;
            operator = m.group(2);
        } catch (IllegalStateException e) {
            return false;
        }
        try {
            double operand = Double.parseDouble(m.group(3));

            switch (operator) {
                case ">":
                    return Double.parseDouble(columns[columnIndex]) > operand;
                case "=":
                    return Double.parseDouble(columns[columnIndex]) == operand;
                case "<":
                    return Double.parseDouble(columns[columnIndex]) < operand;
                case "<>":
                    return Double.parseDouble(columns[columnIndex]) != operand;
            }

        } catch (NumberFormatException e) {
            String operand = m.group(3);

            if (operand.contains("’")) {
                operand = operand.replaceAll("’", "\"");
            }

            switch (operator) {
                case ">":
                    return columns[columnIndex].compareToIgnoreCase(operand) > 0;
                case "=":
                    return columns[columnIndex].equals(operand);
                case "<":
                    return columns[columnIndex].compareToIgnoreCase(operand) < 0;
                case "<>":
                    return !columns[columnIndex].equals(operand);
            }
        }

        return false;
    }

    public boolean matchFilters(String candidate) {
        String[] columns = candidate.split(",");

        if (allFilters.length == 1) {
            return match(columns, allFilters[0]);
        }

        boolean[] matchRes = new boolean[allFilters.length];
        for (int i = 0; i < allFilters.length; i++) {
            matchRes[i] = match(columns, allFilters[i]);
        }
        boolean operand1 = matchRes[0];
        boolean operand2 = matchRes[1];
        int itr = 2;
        for (String op : operands) {
            switch (op.trim()) {
                case "&":
                    operand1 = operand2 && operand1;
                    break;
                case "||":
                    operand1 = operand2 || operand1;
                    break;
            }
            if (itr < matchRes.length) {
                operand2 = matchRes[itr];
                itr++;
            }
        }
        return operand1;
    }

}
