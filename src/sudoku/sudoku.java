package sudoku;

import java.util.Arrays;

public class sudoku {
    protected static Integer[][] sudoku;
    protected static Boolean[][] sudokuWrongCells;
    
    public void initSudokuWrongCells() {
        sudokuWrongCells = new Boolean[9][9];
        
        for(Boolean row[] : sudokuWrongCells) {
            Arrays.fill(row, Boolean.FALSE);
        }
    }
    
    public Boolean[][] getsudokuWrongCells() {
        return sudokuWrongCells;
    }
    
    public Integer[][] getSudoku() {
        return sudoku;
    }

    public void setSudoku(Integer[][] sudoku) {
        this.sudoku = sudoku;
    }
    
    public Integer[][] solveSudoku() {
        //TODO
        
        /*
            This method will solve the Sudoku user have enetered
        */
        return null;
    }
}
