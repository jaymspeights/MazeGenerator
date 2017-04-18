package mazegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Stack;
import javax.imageio.ImageIO;

public class Maze {

    private int width;
    private int height;
    private BitSet maze;
    private Stack<Integer> stack = new Stack();
    private ArrayList<Stack<Integer>> stackList;
    private char[] hashKey;
    private int finish;
    private int start;
    private int count = 0;

    
    public Maze(BitSet maze, int width, int height){
        this.maze = maze;
        this.width = width;
        this.height = height;
    }
    
    //initializes the maze
    //in bitset, true = path && false = wall
    public Maze(int w, int h){
        width = w * 2 + 1;
        height = h * 2 + 1;
        maze = new BitSet(width * height);

        finish = (int)(Math.random() * (width / 2)) * 2 + 1;
        maze.set(width*height - 1 - finish);
        finish = width*height - 1 - finish - width;
        
        start = (int)(Math.random() * (width / 2)) * 2 + 1;
        maze.set(start);
        maze.set(start + width);
        
        stack.push(start + width);
    }

    public BitSet getBitSet(){
        return maze;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    //takes one step towards converting maze
    //returns true if maze is finished
    //else returns false
    public boolean generate(){
        if (stack.empty()) return true;
        int m;
        int loc = stack.peek();
        
        ArrayList<Integer> move = new ArrayList();
        if (loc - 2 * width >= 0 && !maze.get(loc - 2*width)) move.add(-1 * width);
        if (loc + 2 * width < width * height && !maze.get(loc + 2*width)) move.add(1 * width);
        if (loc + 2 < width * height && (loc + 2)/width == loc/width && !maze.get(loc + 2)) move.add(1);
        if (loc - 2 >= 0 && (loc - 2)/width == loc/width && !maze.get(loc - 2)) move.add(-1);

        if (move.size()>0){
            m = move.get((int)(Math.random()*move.size()));
            maze.set(stack.push(loc + 2*m));
            maze.set(loc + m);
            return false;
        }
        stack.pop();
        return stack.empty();
    }
    
    //takes one step towards converting maze
    //returns true if maze is finished
    //else returns false
    //reverses the stack after every move
    public boolean _generate(){
        if (stack.empty()) return true;
        int m;
        int loc = stack.peek();
        
        ArrayList<Integer> move = new ArrayList();
        if (loc - 2 * width >= 0 && !maze.get(loc - 2*width)) move.add(-1 * width);
        if (loc + 2 * width < width * height && !maze.get(loc + 2*width)) move.add(1 * width);
        if (loc + 2 < width * height && (loc + 2)/width == loc/width && !maze.get(loc + 2)) move.add(1);
        if (loc - 2 >= 0 && (loc - 2)/width == loc/width && !maze.get(loc - 2)) move.add(-1);
           
        if (move.size()>0){
            count++;
            m = move.get((int)(Math.random()*move.size()));
            maze.set(stack.push(loc + 2*m));
            maze.set(loc + m);
            if (loc + 2*m == finish){
                Stack<Integer> temp = new Stack();
                while(!stack.empty())
                    temp.push(stack.pop());
                stack = temp;
            }
            if (count == 1){
            Stack<Integer> temp = new Stack();
            while(!stack.empty())
                    temp.push(stack.pop());
            stack = temp;
            count = 0;
            }
            return false;
        }
            
        stack.pop();
        return stack.empty();
    }
    
    
    //num must be greater than 0
    public void _init_(int num){
        stackList = new ArrayList();
        for (int i = 0; i < num; i++){
            stackList.add(new Stack<>());
        }
        while(stack.size() < stackList.size())
            generate();
        for (int i = 0; i < num; i++){
            stackList.get(i).push(stack.pop());
        }
    }
    
    //takes one step towards generating maze
    //returns true if maze is finished
    //else returns false
    //
    //
    public boolean _generate_(){
        for (int i = 0; i < stackList.size(); i++){
            if (stackList.get(i).isEmpty()){
                for (int j = 1; i <= stackList.size(); j++){
                    if (stackList.get((i+j)%stackList.size()).isEmpty()) {
                        if (j==stackList.size()){
                            return true;
                        }
                        continue;
                    }
                    stackList.get(i).push(stackList.get((i+j)%stackList.size()).pop());
                    break;
                }
            }
                    
            
            int m;
            int loc = stackList.get(i).peek();

            ArrayList<Integer> move = new ArrayList();
            if (loc - 2 * width >= 0 && !maze.get(loc - 2*width)) move.add(-1 * width);
            if (loc + 2 * width < width * height && !maze.get(loc + 2*width)) move.add(1 * width);
            if (loc + 2 < width * height && (loc + 2)/width == loc/width && !maze.get(loc + 2)) move.add(1);
            if (loc - 2 >= 0 && (loc - 2)/width == loc/width && !maze.get(loc - 2)) move.add(-1);

            if (move.size()>0){
                m = move.get((int)(Math.random()*move.size()));
                maze.set(stackList.get(i).push(loc + 2*m));
                maze.set(loc + m);
            }
            else{
                stackList.get(i).pop();
                i--;
            }
        }
        return false;
    }
    
    public void cycle(int count){
        while (count > 0){
            int row = (int)(Math.random()*(height/2))*2+1;
            for (int i = (int)(Math.random()*(width-3)); i < width-3; i++){
                if (maze.get(i + row*width) && !maze.get(i + 1 + row*width) && maze.get(i + 2 + row*width)){
                    maze.set(i + 1 + row*width);
                    count -= 1;
                    break;
                }
            }
        }
    }

    //hashes the maze into a string
    public String stringify(){
        
        hashKey = new char[64];
        for (int i = 0; i < 26; i++){
            hashKey[i] = (char)(i + 65);
        }
        hashKey[26] = '_';
        hashKey[27] = '.';
        for (int i = 28; i < 54; i++){
            hashKey[i] = (char)(i + 69);
        }
        for (int i = 54; i < 64; i++){
            hashKey[i] = (char)(i - 6);
        }
        
        String hash = "";
        String str = "";
        for (int i = 0; i < height; i++){
            for (int j = (i%2) + 1; j < width - 1; j+=2){
                if (maze.get(i*height+j)) str+='1';
                else str+='0';
                if (str.length() == 6){
                    hash += hashKey[Integer.parseInt(str, 2)];
                    str = "";
                }  
            }  
        }
        hash += hashKey[Integer.parseInt(str, 2)];
        return hash;
    }

    //returns a string representation of the maze
    //1 represents path and 0 represents wall
    @Override
    public String toString(){
        String str = "";
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (maze.get(i * width + j)){
                    str += 1;
                }
                else str += 0;
            }
            str += "\n";
        }
        return str;
    }

    //draws the maze to a png file
    public void draw(String name){
        BufferedImage bi = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.setColor(Color.black);

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (!maze.get(i * width + j)){
                    g.drawLine(j, i, j, i);
                }
            }
        }

        try {
            File outputfile = new File(name + ".png");
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {}
    }
}
