package mazegenerator;

import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author Jay Speights
 */
public class DijkstraSolve {
    
    private BitSet bs;
    private ArrayList<Node> nodes;
    private int width;
    private int height;
    private ArrayList<Node> known_nodes;
    private Node finish;
    private boolean done;
    int length = 0;
    int op_count = 0;
    int n = 0;
    int e = 0;
    
    public DijkstraSolve(BitSet bs, int width, int height){
        this.bs = bs;
        nodes = new ArrayList();
        known_nodes = new ArrayList();
        this.width = width;
        this.height = height;
        done = false;
    }
    
    public void solve(){
        char name = 'A';
        int loc;
        for (int j = 1; j < height-1; j++){
            for (int i = 1; i < width - 1; i++){
                loc = j*width + i;
                if (bs.get(loc)){
                    if (((bs.get(loc - width))||(bs.get(loc + width)))&&
                            ((bs.get(loc + 1)) || (bs.get(loc - 1)))) {                       
                        nodes.add(new Node(loc, name, width*height));
                        name = (char)(name+1); 
                    }
                }
            }
        }
        nodes.forEach((n) -> {
            connect(n);
        });
        n = nodes.size();
        dijkstra();
        backtrack();
        length = finish.cost;
        e = e/2;
        done = true;
    }
    
    private void connect(Node node){
        int loc = node.loc;
        int new_loc;
        int count;
        int[] dir = {1, -width, -1, width};
        for (int d = 0; d < 4; d++){
            new_loc = loc + dir[d];
            count = 1;
            
            loop1:
            while(true){
                if (!bs.get(new_loc)) // is a wall
                    break loop1;
                if (new_loc > width*(height-1)) { // is finish
                    finish = node;
                    break loop1;
                }
                if (new_loc < width) {// is start
                    node.cost = 0;
                    node.path = node;
                    break loop1;
                } 
                for (Node n: nodes){
                    if (n.loc == new_loc){
                        for (int i = 0; i < node.connections.size(); i++){
                            if (node.connections.get(i).node == n){
                                if (node.connections.get(i).weight <= count)
                                    break loop1;
                                else {
                                    node.connections.remove(i);
                                    break;
                                }
                            }
                        }
                        node.connections.add(new Connection(count, n));
                        break loop1;
                    }
                }
                new_loc += dir[d];
                count += 1;
            } 
        }
        e += node.connections.size();
    }
    
    private void dijkstra(){
        while(nodes.size()>0){
            int smallest_index = -1;
            int smallest = width*height;
            for (int i = 0; i < nodes.size(); i++){
                op_count++;
                if (nodes.get(i).cost < smallest) {
                    smallest_index = i;
                    smallest = nodes.get(i).cost;
                }
            }
            Node b = nodes.remove(smallest_index);
            b.known = true;
            known_nodes.add(b);
            for (Connection c: b.connections){
                op_count++;
                if (!c.node.known){
                    if (b.cost + c.weight < c.node.cost){
                        c.node.cost = b.cost + c.weight;
                        c.node.path = b;
                    } 
                }
            }
        }
    }
    
    private void backtrack(){
        Node n = finish;
        while (n.path != n){
            nodes.add(n);
            n = n.path;
        }
        nodes.add(n);
    }
    
    public ArrayList<Node> getNodes(){
        return nodes;
    }
    
    public boolean isDone(){
        return done;
    }
    
    class Node {
        ArrayList<Connection> connections;
        int cost;
        int loc;
        char name;
        Node path;
        boolean known;
        
        public Node(int loc, char name, int cost){
            this.loc = loc;
            this.name = name;
            connections = new ArrayList();
            this.cost = cost;
            known = false;
        }
        
        @Override
        public String toString(){
            return ""+name + " " + loc + " cost: " + cost;
        }
    }
    
    class Connection {
        int weight;
        Node node;
        
        public Connection(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
    }
}
