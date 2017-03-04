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
    private char[] hashKey;

    
    public Maze(BitSet maze, int width, int height){
        this.maze = maze;
        this.width = width;
        this.height = height;
    }
    
    //initializes the maze
    public Maze(int w, int h){
        width = w * 2 + 1;
        height = h * 2 + 1;
        maze = new BitSet(width * height);

        int finish = (int)(Math.random() * (width / 2)) * 2 + 1;
        maze.set(width*height - 1 - finish);

        int start = (int)(Math.random() * (width / 2)) * 2 + 1;
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
