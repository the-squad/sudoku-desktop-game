package sudoku;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class rowChecker extends sudoku implements Runnable {

    private int row;

    @Override
    public void run() {
        this.checkrow();
    }

    public rowChecker(int row) {
        this.row = row;
    }

    private void checkrow() {
        Set<Integer> myset = new HashSet<>();
        Set<Integer> errorSet = new HashSet<>();
        for (int column = 0; column < this.sudoku.length; column++) {
            if (!myset.add(this.sudoku[row][column])) {
                errorSet.add(Arrays.asList(this.sudoku[row]).indexOf(this.sudoku[row][column]));
                errorSet.add(column);
            }
        }
        for (Iterator<Integer> setValues = errorSet.iterator(); setValues.hasNext();) {
            this.sudokuWrongCells[row][setValues.next()] = Boolean.TRUE;
        }
        myset.clear();

    }
}
