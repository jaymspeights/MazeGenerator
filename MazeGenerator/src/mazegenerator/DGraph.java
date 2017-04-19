package mazegenerator;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class DGraph extends Applet{
    int size = 100;
    int[] n = new int[size];
    int[] op_count = new int[size];
    int[] e = new int[size];
    int x = 1000;
    int y = 500;
    int max;
    DijkstraSolve ds;
    Maze m;
    
    @Override
    public void init(){
        resize(x,y);    
        max = -1;
        System.out.println(String.format("%-10s", "Runtime:") + String.format("%7s","i = ") + String.format("%-6s", "n") + " -> " + "op_count");
        System.out.println("-----------------------------------");
    }
    
    public void run(){
        long time = System.nanoTime();
        for (int i = 0; i < size; i++){
            m = new Maze(i+5,i+5);
            while(!m._generate());
            m.cycle(i/10 + 5);
            ds = new DijkstraSolve(m.getBitSet(),m.getWidth(),m.getHeight());
            ds.solve();
            n[i]=ds.n;
            e[i] = ds.e;
            op_count[i]=ds.op_count;
            System.out.println(String.format("%-10s", (System.nanoTime()-time)/1000/1000 + "ms: ") + String.format("%7s", i + " = ") + String.format("%-6s",+n[i]) + " -> " + op_count[i]);
            max++;
            m = new Maze(50,50);
            while(!m._generate());
            while(!m._generate());
            while(!m._generate());
            update(getGraphics());
        }
        System.out.println("Finished");
    }
    
    @Override
    public void update(Graphics g) { 
          paint(g); 
     } 
    
    @Override
    public void paint(Graphics g){
        if (max >= 0) {
            double lin_scale = (double)(op_count[max])/n[max];
            double sqr_scale = (double)(op_count[max])/n[max]/n[max];
            double sqr_x_e_scale = (double)(op_count[max])/n[max]/n[max]/e[max];
            double scale_x = (double)x/n[max];
            double scale_y = (double)y/op_count[max];
            g.clearRect(0, 0, x, y);
            for (int i = 0; i <= max; i++){
                g.setColor(Color.green);
                g.fillRect((int)(n[i]*scale_x) - x/size/2, y - (int)(n[i]*lin_scale*scale_y), x/size, (int)(n[i]*lin_scale*scale_y));

                g.setColor(Color.yellow);
                g.fillRect((int)(n[i]*scale_x) - x/size/2, y - (int)(scale_y*n[i]*sqr_scale*n[i]), x/size, (int)(scale_y*n[i]*sqr_scale*n[i]));

                g.setColor(Color.red);
                g.fillRect((int)(n[i]*scale_x) - x/size/2, y - (int)(e[i]*scale_y*n[i]*sqr_x_e_scale*n[i]), x/size, (int)(e[i]*scale_y*n[i]*sqr_x_e_scale*n[i]));

                g.setColor(Color.black);
                g.fillRect((int)(n[i]*scale_x) - x/size/6, y - (int)(op_count[i]*scale_y), x/size/3, (int)(op_count[i]*scale_y));
            }
        }
        else {
            run();
        }
    }
    
}
