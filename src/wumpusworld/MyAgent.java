package wumpusworld;
import java.util.*;
import static wumpusworld.World.A_MOVE;
import static wumpusworld.World.A_TURN_LEFT;
import static wumpusworld.World.A_TURN_RIGHT;

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
    
    public int DIR_UP = 0;
    public int DIR_RIGHT = 1;
    public int DIR_DOWN = 2;
    public int DIR_LEFT = 3;
    
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
        
        //Anton test!

        List<Integer> dirs = new ArrayList<Integer>();
        dirs.add(DIR_UP);
        dirs.add(DIR_RIGHT);
        dirs.add(DIR_DOWN);
        dirs.add(DIR_LEFT);
        
        for(int a=0;a<dirs.size();a++){
            if(isValid(a) == false){
                dirs.remove(a);
            }
        }
        for(int a=0;a<dirs.size();a++){
            if(isGoodMove(dirs.get(a)) == true){
                //rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        int prehapsNotPit = -1;
        for(int a=0;a<dirs.size();a++){
            prehapsNotPit = a; //least likely to be a pit
        }
        
        
        //End Anton test!
        
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
        //right left up down
        }else if ((cY-y) == 0) //left or right
        {
           if ((cX-x) == 1) //left
            {
                if(curDir == 4)
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 3)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 2)
                {
                    w.doAction(World.A_MOVE);
                }else
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
                
            } else if ((cX-x) == -1) //right
            {
                if(curDir == 4)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 3)
                {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }else if(curDir == 2)
                {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }else
                {
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
            
    public boolean haveWumpus(int x, int y)
    {
        if((   ((w.hasStench(x+1, y)?1:0) + (w.hasStench(x, y+1)?1:0) + (w.hasStench(x-1, y)?1:0) + (w.hasStench(x, y-1)?1:0)) >= 2   )){
            return true;
        }
        return false;
    }
    /*isGoodMove checks if given coordinates is safe and have unvisited neighbors*/
    public boolean isGoodMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return isSafe(x,y+1) && haveUnvisited(x,y+1);
            case 1:
                return isSafe(x+1,y) && haveUnvisited(x+1,y);
            case 2:
                return isSafe(x,y-1) && haveUnvisited(x,y-1);
            case 3:
                return isSafe(x-1,y) && haveUnvisited(x-1,y);
            default:
                return false;
        }
    }
    /*isSafe checks if given coordinates is safe to go to*/
    public boolean isSafe(int x, int y)
    {
        for(int c=0;c<4;c++){
            
        }
        return true;
    }
    /*haveUnvisited checks if given coordinates have unvisited neighbors*/
    public boolean haveUnvisited(int x, int y)
    {
        if(!w.isVisited(x,y+1) || !w.isVisited(x+1,y) || !w.isVisited(x,y-1) || !w.isVisited(x-1,y)){ //isVisited just say false if unvalid
            return true;
        }
        return false;
    }
    
    
    public boolean isValid(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return w.isValidPosition(x,y+1);
            case 1:
                return w.isValidPosition(x+1,y);
            case 2:
                return w.isValidPosition(x,y-1);
            case 3:
                return w.isValidPosition(x-1,y);
            default:
                return false;
        }
    }
    public void rotatePlayer(int currentDir, int wantedDir)
    {
        if(currentDir == wantedDir){
            return;
        }
        
        if(Math.abs(currentDir-wantedDir) == 2){
            w.doAction(A_TURN_LEFT);
            w.doAction(A_TURN_LEFT);
            return;
        }
        
        if(currentDir-wantedDir < 0 || currentDir == 3){
            w.doAction(A_TURN_RIGHT);
            return;
        }
        if(currentDir-wantedDir > 0 || wantedDir == 3){
            w.doAction(A_TURN_LEFT);
            return;
        }
        //(0,2), (2,0), (1,3), (3,1)
        //(0,1), (1,2), (2,3), (3,0)
        //(1,0), (2,1), (3,2), (0,3)
    }
}

