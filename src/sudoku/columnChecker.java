/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mohamedkamal
 */
public class columnChecker extends sudoku implements Runnable {

    @Override
    public void run() {
        checkColumn();
    }

    private void checkColumn() {
        Set<Integer> myset = new HashSet<>();
        for (int column = 0; column < this.sudoku.length; column++) {
            Set<Integer> errorSet = new HashSet<>();
            for (int row = 0; row < this.sudoku.length; row++) {
                if (!myset.add(this.sudoku[row][column])) {
                    errorSet.add(this.columnToArray(sudoku, column).indexOf(sudoku[row][column]));
                    errorSet.add(row);
                }
            }
            for (Iterator<Integer> setValues = errorSet.iterator(); setValues.hasNext();) {
                this.sudokuError[setValues.next()][column] = Boolean.TRUE;
            }
            myset.clear();
        }
    }

    public ArrayList<Integer> columnToArray(Integer x[][], int i) {
        ArrayList<Integer> colArray = new ArrayList<Integer>();
        for (int j = 0; j < x.length; j++) {
            colArray.add(x[j][i]);
        }
        return colArray;
    }

}
