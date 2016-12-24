package sudoku;

public class checker {

    //Creating objects from checker classes
    boxChecker box;
    rowChecker row;
    columnChecker column;

    /**
     * @author Muhammad Kamal, Mustafa Magdy
     * @throws InterruptedException
     */
    public void check() throws InterruptedException {
        Thread multiThreadrows[] = new Thread[9];
        Thread multiThreadcolumns[] = new Thread[9];

        for (int row = 0; row < 9; row++) {
            this.row = new rowChecker(row);
            multiThreadrows[row] = new Thread(this.row);
            multiThreadrows[row].start();
        }

        for (int column = 0; column < 9; column++) {
            this.column = new columnChecker(column);
            multiThreadcolumns[column] = new Thread(this.column);
            multiThreadcolumns[column].start();
        }

        int threadId = 0;

        Thread multiThreads[] = new Thread[9];
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                this.box = new boxChecker(row, col);
                multiThreads[threadId] = new Thread(this.box);
                multiThreads[threadId].start();
                threadId++;
            }
        }


        for (int i = 0; i < 9; i++) {
            multiThreads[i].join();
            multiThreadrows[i].join();
            multiThreadcolumns[i].join();
        }

    }
}
