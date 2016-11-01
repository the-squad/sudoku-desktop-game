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
public class columnChecker extends sudoku implements Runnable{

    @Override
    public void run() {
        checkColumn();
    }

    private void checkColumn() {
    }
    
}
