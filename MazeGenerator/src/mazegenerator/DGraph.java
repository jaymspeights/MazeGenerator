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
    
    @Override
    public void init(){
        resize(x,y);
        Maze m;
        DijkstraSolve ds;
        for (int i = 0; i < size; i++){
            m = new Maze(i+5,i+5);
            while(!m._generate());
            m.cycle(i/10 + 5);
            ds = new DijkstraSolve(m.getBitSet(),m.getWidth(),m.getHeight());
            ds.solve();
            n[i]=ds.n;
            e[i] = ds.e;
            op_count[i]=ds.op_count;
            System.out.println(n[i] + " -> " + op_count[i]);
        }
    }
    
    @Override
    public void update(Graphics g) { 
          paint(g); 
     } 
    
    @Override
    public void paint(Graphics g){
        int max = op_count[size-1];
        double lin_scale = (double)(max)/n[size-1];
        double sqr_scale = (double)(max)/n[size-1]/n[size-1];
        double sqr_x_e_scale = (double)(max)/n[size-1]/n[size-1]/e[size-1];
        double scale_x = (double)x/n[size-1];
        double scale_y = (double)y/op_count[size-1];
        
        for (int i = 0; i < size; i++){
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
    
}
