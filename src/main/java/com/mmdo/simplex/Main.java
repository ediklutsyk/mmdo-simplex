package com.mmdo.simplex;

import com.mmdo.simplex.DTO.SAE;
import com.mmdo.simplex.DTO.Table;
import com.mmdo.simplex.utils.InputUtils;
import com.mmdo.simplex.utils.SimplexUtils;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        SAE sae = new InputUtils().parseSAE("input1.txt");
        List<Table> tables = SimplexUtils.solve(sae);
        if (tables != null) {
            tables.forEach(Table::print);
        } else {
            System.out.println("Can't solve!");
        }
    }
}
