package UI;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Class to init all global variables and method
 * @author Muhammad
 */
public class global {
    static BorderPane windowLayout;
    static BorderPane gamePlayContainer;
    static GridPane mainMenuContainer;
    
    //<editor-fold defaultstate="collapsed" desc="Sudoku Arrays">
    static Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    static Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    static Boolean[][] markSolution = new Boolean[9][9]; //Where computer returns the wrong cells
//</editor-fold>
    
    static int playingMode;
    
        /**
     *
     * @param opType
     */
    static void sudokuOperation(int opType) {
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {

                switch (opType) {
                    //Read Sudoku
                    case 1:
                        userSudoku[rowCounter][columnCounter] = Integer.parseInt(sudokuCells[rowCounter][columnCounter].getText());
                        break;
                    //Print Sudoku
                    case 2:
                        if (computerSolution[rowCounter][columnCounter] != 0) {
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                            sudokuCells[rowCounter][columnCounter].setDisable(true);
                        }
                        break;
                    //Clear Sudoku fields and array
                    case 3:
                        sudokuCells[rowCounter][columnCounter].setText("");
                        sudokuCells[rowCounter][columnCounter].setDisable(false);
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-danger");
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-success");

                        userSudoku[rowCounter][columnCounter] = 0;
                        computerSolution[rowCounter][columnCounter] = 0;
                        markSolution[rowCounter][columnCounter] = Boolean.FALSE;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
