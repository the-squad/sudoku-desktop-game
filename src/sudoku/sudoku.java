/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.Arrays;

/**
 *
 * @author mohamedkamal
 */
public class sudoku {
    protected static Integer[][] sudoku;
    protected static Boolean[][] sudokuError;
    public void initsudokuError(){
        sudokuError = new Boolean[9][9];
        for(Boolean row[] : sudokuError)
        {
            Arrays.fill(row, Boolean.FALSE);
        }
    }
    public Boolean[][] getSudokuError() {
        return sudokuError;
    }
    
    public Integer[][] getSudoku() {
        return sudoku;
    }

    public void setSudoku(Integer[][] sudoku) {
        this.sudoku = sudoku;
    }
    
    public Integer[][] solveSudoku(){
        //TODO
        return null;
    }
}
