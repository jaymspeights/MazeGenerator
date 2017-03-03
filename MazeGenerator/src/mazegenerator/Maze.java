package mazegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Stack;
import javax.imageio.ImageIO;

public class Maze {

    private int width;
    private int height;
    private BitSet maze;
    private int[] move;
    private Stack<Integer> stack;

    //initializes the maze
    public Maze(int w, int h){
        width = w * 2 + 1;
        height = h * 2 + 1;
        maze = new BitSet(width * height);
        move = new int[] {1, -1 * width, -1, 1 * width};

        int finish = (int)(Math.random() * (width / 2)) * 2 + 1;
        maze.set(width*height - 1 - finish);

        int start = (int)(Math.random() * (width / 2)) * 2 + 1;
        maze.set(start);
        maze.set(start + width);

        stack = new Stack();

        stack.push(start + width);
    }

    //takes one step towards converting maze
    //returns true if maze is finished
    //else returns false
    public boolean generate(){
        if (stack.empty()) return true;
        int count = 0;
        int loc = stack.peek();
        int dir = (int)(Math.random()*4);
        while (count < 4){
            if (loc + 2*move[dir] >= 0 && loc + 2*move[dir] < width * height && ((loc + 2*move[dir])%width == loc%width || (loc + 2*move[dir])/width == loc/width) && !maze.get(loc + 2*move[dir])){
                maze.set(stack.push(loc + 2*move[dir]));
                maze.set(loc + move[dir]);
                return false;
            }
            else {
                count++;
                dir = (dir+1)%4;
            }
        }
        stack.pop();
        return stack.empty();
    }

    //hashes the maze into a string
    public String hash(){
        String name = "";
        String str = "";
        int num;
        for (int i = width + 1; i < width * height; i++){
            if (maze.get(i)) str+='1';
            else str+='0';
            num = Integer.parseInt(str) - i%width;
            if ((num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
               name += (char)num;
               str = "";
            }
            else if (num > 122) str = "";
        }
        return name;
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
