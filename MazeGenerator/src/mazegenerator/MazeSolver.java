package mazegenerator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Stack;

public class MazeSolver {
    BitSet mazeBS;
    BitSet visitBS;
    Stack<Integer> stack;
    int width;
    int height;
    int finish = -1;
    boolean solved = false;
    int previous;
    int loc;
    int start;
    Maze maze;
    int length = 1;
    
    public MazeSolver(Maze m){
        maze = m;
        mazeBS = maze.getBitSet();
        visitBS = new BitSet(mazeBS.size());
        stack = new Stack();
        this.width = maze.getWidth();
        this.height = maze.getHeight();
    }
    
    public BitSet getBitSet(){
        return visitBS;
    }
    
    public boolean isSolved(){
        return solved;
    }
    
    public int getLoc(){
        return loc;
    }
    
    public boolean solve(){
        if (!solved){
            if (stack.empty()) {               
                for (int i = 0; i < width; i++){
                    if (mazeBS.get(i)){
                        start = stack.push(i+width);  
                        loc = start;
                        for (int j = 1; j <= width; j++){
                            if (mazeBS.get(width*height - j)){
                                finish = width*height - j - width;
                                return false;
                            }
                        }
                    }
                }
                return true;
            }           
            int m;
            loc = stack.peek();
            ArrayList<Integer> move = new ArrayList();
            if (loc > 3*width && mazeBS.get(loc - width)  && !visitBS.get(loc - 2*width)) move.add(-1 * width);
            if (loc + 3*width < width * height && mazeBS.get(loc + width)  && !visitBS.get(loc + 2*width)) move.add(width);
            if (mazeBS.get(loc + 1) && !visitBS.get(loc + 2)) move.add(1);
            if (mazeBS.get(loc - 1) && !visitBS.get(loc - 2)) move.add(-1);

            if (move.size()>0){
                
                m = move.get((int)(Math.random()*move.size()));
                if (loc + 2*m == finish) {
                    
                    visitBS.clear();
                    previous = loc + 2*m;
                    visitBS.set(loc + 2*m);
                    visitBS.set(start - width);
                    visitBS.set(finish + width);
                    solved = true;
                    return false;   
                }
                visitBS.set(stack.push(loc + 2*m));
                return false;
            }           
            stack.pop();
            return stack.empty();
        }
        else{  
            length+=2;
            loc = stack.pop();
            int split = (loc - previous) / 2;
            visitBS.set(previous + split);
            visitBS.set(loc);
            previous = loc;
            return stack.empty();
        }
    }       
}
