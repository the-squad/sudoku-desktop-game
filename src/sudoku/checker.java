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
    public void check() throws InterruptedException {
        this.row = new rowChecker();
        this.column = new columnChecker();
        Thread thread1 = new Thread(this.row);
        Thread thread2 = new Thread(this.column);

        thread1.start();
        thread2.start();

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

        thread1.join();
        thread2.join();

        for (int i = 0; i < 9; i++) {
            multiThreads[i].join();
        }

    }
}
