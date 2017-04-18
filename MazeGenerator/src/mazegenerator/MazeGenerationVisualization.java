package mazegenerator;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.BitSet;
import mazegenerator.DijkstraSolve.Node;

public class MazeGenerationVisualization extends Applet implements Runnable{
    Maze m;
    MazeSolver ms;
    DijkstraSolve ds;
    BitSet bs;
    Thread thread;
    int height = 10;
    int width = 10;
    int scale = 25;
    int w = width*2+1;
    int h = height*2+1;
    int count = 0;
    boolean solved;
    int reprint = 0;
    
    
    
    Graphics bufferOS;
    Graphics bufferMS;
    Image offscreen;
    Image mazescreen;
    
    @Override
    public void init(){
        resize(w * scale, h * scale + 10);
        offscreen = createImage(w*scale,h*scale + 10);
        mazescreen = createImage(w*scale,h*scale + 10);
        bufferOS = offscreen.getGraphics();
        bufferMS = mazescreen.getGraphics();
        m = new Maze(width, height);
        bs = m.getBitSet();
        solved = false;
        thread = new Thread(this);
        thread.start();
    }
    

    @Override
    public void run() {
        m._init_(1);
        while (!m._generate_()){
            count++;
            bs = m.getBitSet();
            repaint();
            try {
                thread.sleep(1,0);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        m.cycle(6);
        bs = m.getBitSet();
        repaint();
        ms = new MazeSolver(m);
        solved = true;
        System.out.println("Solving");
        while (!ms.solve()){
//            count++;
//            if (ms.isSolved()){
//                bs = ms.getBitSet();
//            }
//            repaint();
//            try {
//                thread.sleep(1,0);
//            } catch (InterruptedException ex) {
//                System.out.println(ex);
//            }
        }
        bs = ms.getBitSet();
        repaint();
        ds = new DijkstraSolve(m.getBitSet(),m.getWidth(),m.getHeight());
        ds.solve();
        while(!ds.isDone());
        repaint();
        kill();
        System.out.println("Random search length: " + ms.length + 
                "\nDijkstra search length: " + ds.length);
    }
    
    public void kill(){
        System.out.println("Killing");
        thread = null;
    }
    
    @Override
    public void update(Graphics g) { 
          paint(g); 
     } 
    
    @Override
    public void paint(Graphics g){
        if (solved && ms.isSolved()){
            bufferMS.setColor(Color.black);
            bufferMS.clearRect(0, 0, w*scale, h*scale);
            bufferMS.drawString(""+count, 0, scale * h + 10);
            bufferMS.setColor(Color.yellow);
            bufferMS.drawImage(offscreen, 0, 0, this);
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    if (bs.get(i * w + j)){
                        bufferMS.fillRect(j * scale, i * scale, scale, scale);
                    }
                }
            }
            g.drawImage(mazescreen, 0, 0, this);  
        }
        if (solved && ms.solved){
            bufferMS.setColor(Color.red);
            bufferMS.clearRect(0, 0, w*scale, h*scale);
            bufferMS.drawString(""+count, 0, scale * h + 10);
            bufferMS.drawImage(offscreen, 0, 0, this); 
            bufferMS.setColor(Color.yellow);
            bufferMS.drawImage(offscreen, 0, 0, this);
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    if (bs.get(i * w + j)){
                        bufferMS.fillRect(j * scale, i * scale, scale, scale);
                    }
                }
            }
            if (ds != null && ds.isDone()){
                bufferMS.setColor(Color.red);
                for (Node n:ds.getNodes()){
                    bufferMS.fillRect((n.loc%w) * scale, scale * (n.loc/w), scale, scale);
                }
            }
            g.drawImage(mazescreen, 0, 0, this);
        }
        else{
            bufferOS.clearRect(0, 0, w*scale, h*scale + 10);
            bufferOS.drawString(""+count, 0, scale * h + 10);
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    if (!bs.get(i * w + j)){
                        bufferOS.fillRect(j * scale, i * scale, scale, scale);
                    }
                }
            }
            if (solved){
                bufferOS.setColor(Color.red);
                int x = (ms.getLoc()%w) * scale;
                int y = (ms.getLoc()/w) * scale; 
                bufferOS.fillRect(x, y, scale, scale); 
                bufferOS.setColor(Color.black);
            }
            g.drawImage(offscreen, 0, 0, this);
        }
    }
    
}
