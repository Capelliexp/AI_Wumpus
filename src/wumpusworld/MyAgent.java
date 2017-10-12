package wumpusworld;
import java.util.Vector;


/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    int rnd;
    
    //Vector<Vector<SquareNode>> map; 
    SquareNode[][] map;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
        //map = new Vector<Vector<SquareNode>>();
        //map = new Vector<>();
        System.out.println("HEJ");
        map = new SquareNode[4][4];
        
        for (int row = 0; row < 4; row ++)
            for (int col = 0; col < 4; col++)
                map[row][col] = new SquareNode();
        
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            map[cX-1][cY-1].setThing(1, "b");
        }
        if (w.hasStench(cX, cY))
        {
            map[cX-1][cY-1].setThing(1, "s");
        }
        if (w.hasPit(cX, cY))
        {
            map[cX-1][cY-1].setThing(1, "p");
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        System.out.println("breeze " + map[cX-1][cY-1].getThing("b"));
        System.out.println("stench " + map[cX-1][cY-1].getThing("s"));
        System.out.println("pit " + map[cX-1][cY-1].getThing("p"));
        System.out.println("");
        //decide next move
        /*
        rnd = decideRandomMove();
        if (rnd==0)
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);
        }
        
        if (rnd==1)
        {
            w.doAction(World.A_MOVE);
        }
                
        if (rnd==2)
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);
        }
                        
        if (rnd==3)
        {
            w.doAction(World.A_TURN_RIGHT);
            w.doAction(World.A_MOVE);
        }
        */
    }    
    
     /**
     * Generates a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    
    
}

