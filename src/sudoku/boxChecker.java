package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class boxChecker extends sudoku implements Runnable{
    private int startRowbox;
    private int startColumnbox;
    private HashMap<Integer,ArrayList<String>> map;

    /**
     * @author Muhammad Kamal
     * @param startRowbox
     * @param startColumnbox 
     */
    public boxChecker(int startRowbox, int startColumnbox) {
        this.startRowbox = startRowbox;
        this.startColumnbox = startColumnbox;
        map  = new HashMap<>();
    }
    
    @Override
    public void run() {
        checkBox();
    }
    
    /**
     * @author Muhammad Kamal
     */
    private void checkBox() {
        for (int i = this.startRowbox; i < this.startRowbox + 3; i++) {
            for (int j = this.startColumnbox; j < this.startColumnbox + 3; j++) {
                ArrayList<String> indexes;
                if (this.map.containsKey(this.sudoku[i][j])) {
                    indexes = this.map.get(this.sudoku[i][j]);
                } else {
                    indexes = new ArrayList<>();
                }
                indexes.add(i + "," + j);
                this.map.put(this.sudoku[i][j], indexes);
            }
        }
        for (ArrayList<String> value : this.map.values()) {
            if (value.size() > 1) {
                for (String Indexxy : value) {
                    this.sudokuWrongCells[Integer.parseInt(Indexxy.charAt(0)+"")][Integer.parseInt(Indexxy.charAt(2)+"")] = Boolean.TRUE;
                }
            }
        }
    }
}
