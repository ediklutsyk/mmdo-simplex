package com.mmdo.simplex.utils;

import com.mmdo.simplex.DTO.Condition;
import com.mmdo.simplex.DTO.Equation;
import com.mmdo.simplex.DTO.SAE;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputUtils {

    public InputUtils() {
    }

    public List<String> readFileFromResources(String fileName) {
        try {
            URL resource = this.getClass().getClassLoader().getResource(fileName);
            if (resource != null) {
                return Files.readAllLines(new File(resource.getFile()).toPath());
//                return Files.readAllLines(Paths.get("src/main/resources/" + fileName));
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public List<String> parseFile(String fileName) {
        List<String> fileLines = readFileFromResources(fileName);
        if (fileLines != null) {
            return fileLines.stream()
                    .map(line -> line.replaceAll("/\\*.*\\*/", "").trim())
                    .filter(line -> !line.startsWith("//")).collect(Collectors.toList());
        }
        return null;
    }

    public SAE parseSAE(String fileName) {
        List<String> fileLines = parseFile(fileName);
        SAE sae = new SAE();
        sae.setAmountOfUnknown(Integer.parseInt(fileLines.get(0)));
        sae.setAmountOfEquations(Integer.parseInt(fileLines.get(1)));
        sae.setCondition(Condition.valueOf(fileLines.get(2).toUpperCase()));
        sae.setFunction(parseEquation(fileLines.get(3)).getA());
        List<Equation> equations = new ArrayList<>();
        for (int i = 4; i < fileLines.size(); i++) {
            equations.add(parseEquation(fileLines.get(i)));
        }
        sae.setEquations(equations);
        return sae;
    }

    private Equation parseEquation(String equation) {
        Equation res = new Equation();
        equation = equation.replaceAll("\\s", "");
        String leftPart = equation;
        if (equation.contains("<") || equation.contains(">")) {
            int indexOfSign = equation.indexOf('<') == -1 ? equation.indexOf('>') : equation.indexOf('<');
            res.setSign(equation.charAt(indexOfSign));
            leftPart = equation.substring(0, indexOfSign);
            res.setB(Integer.parseInt(equation.substring(indexOfSign + 1)));
        }
        res.setA(Arrays.stream(leftPart.replace("-", "+-").split("\\+"))
                .filter(e -> !e.isEmpty())
                .map(Integer::parseInt).collect(Collectors.toList())
        );
        return res;
    }
}
