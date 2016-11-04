package sudoku;

public class checker {
    
    //Creating objects from checker classes
    boxChecker box;
    rowChecker row;
    columnChecker column;

    public checker() {
        //Init objects
        this.box = new boxChecker();
        this.row = new rowChecker();
        this.column = new columnChecker();
    }
    
    void check(sudoku su) {
        
    }
}
