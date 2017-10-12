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
        
        int direction = -1;
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
            direction = 1;
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
            direction = 2;
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
            direction = 3;
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
            direction = 4;
        }
        System.out.println("breeze " + map[cX-1][cY-1].getThing("b"));
        System.out.println("stench " + map[cX-1][cY-1].getThing("s"));
        System.out.println("pit " + map[cX-1][cY-1].getThing("p"));
        System.out.println("");
        
        
        //decide next move
        //print("HEJ1");
        if (!w.hasStench(cX, cY) && !w.hasPit(cX, cY) && !w.hasBreeze(cX, cY))
        {
            //print("HEJ2");
            int tempX = cX+1;
            int tempY = cY;
            
            if (w.isUnknown(tempX, tempY))
            {
                setMapThing(tempX, tempY, 2, "pw");
            }
            tempX = cX-1;
            tempY = cY;
            
            if (w.isUnknown(tempX, tempY))
            {
                setMapThing(tempX, tempY, 2, "pw");
            }
            tempX = cX;
            tempY = cY+1;
            
            if (w.isUnknown(tempX, tempY))
            {
                setMapThing(tempX, tempY, 2, "pw");
            }
            tempX = cX;
            tempY = cY-1;
            
            if (w.isUnknown(tempX, tempY))
            {
                setMapThing(tempX, tempY, 2, "pw");
            }
            
        }
        
        
        
        // action time
        
        if (w.isValidPosition(cX+1, cY))
        {
            int tempx = cX+1;
            int tempy = cY;
            if(getMapThing(tempx, tempy, "p") == 2 && getMapThing(tempx, tempy, "w") == 2)
            {
                print("HEJ3");
                moveTo(tempx, tempy, direction);
            }
        }else if (w.isValidPosition(cX, cY+1))
        {
            int tempx = cX;
            int tempy = cY+1;
            if(getMapThing(tempx, tempy, "p") == 2 && getMapThing(tempx, tempy, "w") == 2)
            {
                print("HEJ4");
                moveTo(tempx, tempy, direction);
            }           
        }
        
    }   
    
    public void moveTo(int x, int y, int curDir) //use only for neighbour
    {
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        if(!w.isValidPosition(x,y))
        {
            System.out.println("YOU CAN'T DO THAT");
            return;
        }
        //right left up down
        if((cX-x) == 0) //up or down
        {
            if ((cY-y) == 1) //down
            {
                if(curDir == 4)
                {
                    w.doAction(World.A_MOVE);
                }else if(curDir == 3)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 2)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
                
            } else if ((cY-y) == -1) //up
            {
                if(curDir == 4)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 3)
                {
                    w.doAction(World.A_MOVE);
                }else if(curDir == 2)
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }else
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
            }
        }
    }
    
    public void setMapThing(int x, int y, int value, String t)
    {
        if (w.isValidPosition(x, y))
        {
            map[x-1][y-1].setThing(value, t);
        }
    }
    
    
    public int getMapThing(int x , int y, String t)
    {
        if (w.isValidPosition(x, y))
        {
            return map[x-1][y-1].getThing(t);
        }
        return -1;
    }
    
    public void print(String t)
    {
        System.out.println(t);
    }
     /**
     * Generates a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    
    
}

