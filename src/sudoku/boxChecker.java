package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class boxChecker extends sudoku implements Runnable{
    private int startRowbox;
    private int startColumnbox;
    private int threadId;
    private HashMap<Integer,ArrayList<String>> map[]=new HashMap[9];

    public boxChecker(int startRowbox, int startColumnbox, int threadId) {
        this.startRowbox = startRowbox;
        this.startColumnbox = startColumnbox;
        this.threadId = threadId;
        this.map[this.threadId] = new HashMap<>();
    }
    
    @Override
    public void run() {
        checkBox();
    }
    
    private void checkBox()
    {
        for (int i = this.startRowbox; i < this.startRowbox + 3; i++) {
            for (int j = this.startColumnbox; j < this.startColumnbox + 3; j++) {
                ArrayList<String> indexes;
                if (this.map[this.threadId].containsKey(this.sudoku[i][j])) {
                    indexes = this.map[this.threadId].get(this.sudoku[i][j]);
                } else {
                    indexes = new ArrayList<>();
                }
                indexes.add(i + "," + j);
                this.map[this.threadId].put(this.sudoku[i][j], indexes);
            }
        }
        for (ArrayList<String> value : this.map[this.threadId].values()) {
            if (value.size() > 1) {
                for (String Indexxy : value) {
                    this.sudokuWrongCells[Integer.parseInt(Indexxy.charAt(0)+"")][Integer.parseInt(Indexxy.charAt(2)+"")] = Boolean.TRUE;
                }
            }
        }
    }
}
