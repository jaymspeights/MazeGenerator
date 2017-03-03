package mazegenerator;

import java.util.ArrayList;
public class HashTest {

  public static void main(String[] args) {
      ArrayList<String> names = new ArrayList();
      boolean stop = false;
      String n;
      int count = 0;
      ArrayList<Maze> mazes = new ArrayList();
      System.out.println("Searching...");
      while(!stop){
        Maze m = new Maze(10, 10);
        mazes.add(m);
        while(!m.generate());
        n = m.hash();
        for (int i = 0; i < names.size(); i++){
          if (names.get(i).equals(n)){
            if (!m.toString().equals(mazes.get(i).toString())){
              m.draw("Maze1");
              mazes.get(i).draw("maze2");
              stop = true;
              System.out.println("Hash collision found at " + count);
            }
            System.out.println("Duplicate mazes found at " + count);
          }
        }
        count++;
        names.add(n);
      }


  }

}
