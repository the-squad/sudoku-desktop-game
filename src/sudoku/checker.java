/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author mohamedkamal
 */
public class checker {
    boxChecker box;
    rowChecker row;
    columnChecker column;

    public checker() {
        this.box = new boxChecker();
        this.row = new rowChecker();
        this.column = new columnChecker();
    }
    
    void check(sudoku su)
    {
        
    }
}
