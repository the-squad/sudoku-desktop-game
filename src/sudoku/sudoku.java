package sudoku;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class sudoku {
    protected static Integer[][] sudoku;
    protected static Integer[][] solvedSudoku;
    protected static Boolean[][] sudokuWrongCells;
    /**
     * Time when solveSudoku() is called.
     */
    private long startTime;
    
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
    
    public void setUserSudoku(Integer[][] sudoku) {
        this.sudoku = sudoku;
    }
    
    /**
     * Get Sudoku solved by solveSudoku()
     * @return Sudoku solution.
     */
    public Integer[][] getSudokuSolution() {
        return solvedSudoku;
    }

    public void setSudoku(Integer[][] sudoku) {
        solvedSudoku = new Integer[9][9];
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                solvedSudoku[rowCounter][columnCounter] = sudoku[rowCounter][columnCounter];
            }
        }
    }
    
    /**
     * This method will solve the Sudoku user have entered.
     * To retreve solution, call getSudokuSolution()
     * @since 3/11/2016
     * @return ture if solution exist, otherwise false.
     */
    public boolean solveSudoku() {
        startTime = System.currentTimeMillis();
        List<List<Integer>> sudokuList = Arrays.stream(solvedSudoku).map(Arrays::asList).collect(Collectors.toList());
        return solveSudoku(0, 0, sudokuList);
    }

    /**
     * auxiliary function for solveSudoku()
     *
     * @since 3/11/2016
     * @param x row index
     * @param y column index
     * @param sudokuList Sudoku
     * @return ture if solution exist, otherwise false.
     */
    private boolean solveSudoku(int x, int y, List<List<Integer>> sudokuList) {
        // Check if the function exceeded one second.
        if(System.currentTimeMillis() - startTime > 1000) {
            return false;
        }
        // Next cell number
        int nextCellNum = x*9 + y + 1;
        // If cell is empty.
        if (solvedSudoku[x][y] == 0) {
            // Try numbers from 1 to 9.
            for (int PossibleNumber = 1; PossibleNumber < 10; PossibleNumber++) {
                if (uniqueNumber(PossibleNumber, x, y, sudokuList)) {
                    // If the number is unique.
                    solvedSudoku[x][y] = PossibleNumber;
                    sudokuList.get(x).set(y, PossibleNumber);
                    if ((x == 8 && y == 8) || solveSudoku(nextCellNum/9, nextCellNum % 9, sudokuList)) {
                        // If this is the last cell or the next cell returned true.
                        return true;
                    }
                }
            }
            // if there is no avilable number, sets current cell to zero and return to the previous cell.
            solvedSudoku[x][y] = 0;
            sudokuList.get(x).set(y, 0);
            return false;
        } else {
            // cell not empty.
            return (x == 8 && y == 8) || solveSudoku(nextCellNum/9, nextCellNum % 9, sudokuList);
        }
    }

    /**
     * Check if the number is unique in row, column and block.
     * @param PossibleNumber The number to check if its exist or not.
     * @param sudokuList Sudoku.
     * @param x Row index.
     * @param y Column index.
     * @return If number exist or not.
     */
    private boolean uniqueNumber(int PossibleNumber, int x, int y, List<List<Integer>> sudokuList) {
        // first cell in the block that contains (x, y).
        int block_x = (x / 3) * 3;
        int block_y = (y / 3) * 3;
        // if number exist in row.
        if (sudokuList.get(x).contains(PossibleNumber)) {
            return false;
        }
        // if number exist in culomn.
        if (sudokuList.stream().anyMatch(row -> row.get(y) == PossibleNumber)) {
            return false;
        }
        // if number exist in block.
        for (int j = 0; j < 3; j++) {
            if (sudokuList.get(block_x + j).subList(block_y, block_y + 3).contains(PossibleNumber)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Fill empty cell with it's correct number.
     * @return Integer array with the following format {x, y, value}
     */
    public int [] hint(){
        solveSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sudoku[i][j] == 0){
                    sudoku[i][j] = solvedSudoku[i][j];
                    return new int[]{i, j, solvedSudoku[i][j]};
                }
            }
        }
        return null;
    }
}
