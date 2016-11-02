package sudoku;

public class checker {

    //Creating objects from checker classes
    boxChecker box;
    rowChecker row;
    columnChecker column;

    /*public checker() {
     //Init objects
     this.box = new boxChecker();
     this.row = new rowChecker();
     this.column = new columnChecker();
     }*/
    void check() {
        this.row = new rowChecker();
        this.column = new columnChecker();
        Thread thread;
        thread = new Thread(this.row);
        thread = new Thread(this.column);
        int threadId = 0;
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                this.box = new boxChecker(row, col, threadId);
                thread = new Thread(this.box);
                threadId++;
            }
        }
    }
}
