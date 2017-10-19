package wumpusworld;
import java.util.*;
import static wumpusworld.World.A_MOVE;
import static wumpusworld.World.A_SHOOT;
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
    
    public int DIR_UP = 0;
    public int DIR_RIGHT = 1;
    public int DIR_DOWN = 2;
    public int DIR_LEFT = 3;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
        System.out.println("LET'S DO THIS!");
    }
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        /*
        PRINT MAP OF KNOWN PITS!
        */
        for(int y=4;y>=1;y--){
            for(int x=1;x<=4;x++){
                if(isPit(x,y)){
                    System.out.print("P");
                }
                if(isWumpus(x,y)){
                    System.out.print("W");
                }
                if(!isWumpus(x,y) && !isPit(x,y))
                    System.out.print("S");
            }
            System.out.print("\n");
        }
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
        
        //List of all directitions for current move
        List<Integer> dirs = new ArrayList<Integer>();
        dirs.add(DIR_UP);
        dirs.add(DIR_RIGHT);
        dirs.add(DIR_DOWN);
        dirs.add(DIR_LEFT);
        //Sort list by current direction??
        
        //IF UNVALID MOVE, REMOVE FROM LIST
        for(int a=0;a<dirs.size();a++){
            if(validMove(dirs.get(a)) == false){
                dirs.remove(a);
                a--;
            }
        }
        
        //IF "PERFECT" MOVE, GO THERE
        for(int a=0;a<dirs.size();a++){
            if(goodMove(dirs.get(a)) == true){
                System.out.println("GoodMove");
                //rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF WE CAN KILL WUMPUS AND GO THERE, DO IT
        for(int a=0;a<dirs.size();a++){
            if(killWumpusMove(dirs.get(a)) == true){
                System.out.println("KillWumpusMove");
                //then rotate to that direction, shoot and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_SHOOT );
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF POSSIBLE OTHER WAY, GO THERE (A LITTLE FUZZY)
        for(int a=0;a<dirs.size();a++){
            if(retreatMove(dirs.get(a)) == true){
                System.out.println("RetreatMove");
                //then rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF NOT CERTAIN PIT AND NO WUMPUS, GO THERE
        for(int a=0;a<dirs.size();a++){
            if(perhapsPitMove(dirs.get(a)) == true){
                System.out.println("PerhapsPitMove");
                //then rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF POSSIBLE OTHER WAY, GO THERE (A LITTLE FUZZY)
        for(int a=0;a<dirs.size();a++){
            if(longRetreatMove(dirs.get(a)) == true){
                System.out.println("LongRetreatMove");
                //then rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF NOT CERTAIN WUMPUS AND NO PIT, GO THERE
        for(int a=0;a<dirs.size();a++){
            if(perhapsWumpusMove(dirs.get(a)) == true){
                System.out.println("perhapsWumpusMove");
                //then rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_MOVE);
                return;
            }
        }
        
        //IF NOT CERTAIN WUMPUS, GO THERE
        for(int a=0;a<dirs.size();a++){
            if(perhapsWumpusPitMove(dirs.get(a)) == true){
                System.out.println("perhapsWumpusPitMove");
                //then rotate to that direction and do move
                int current = w.getDirection();
                rotatePlayer(current, dirs.get(a));
                w.doAction(A_SHOOT);
                w.doAction(A_MOVE);
                return;
            }
        }
    }
    
    /**********************************************************************/
    //  This section holds the Move-functions that decides on a move        
    /**********************************************************************/
    
    //TRUE IF DIRECTION IS VALID
    public boolean validMove(int dir)
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
    //TRUE IF DIRECTION IS SAFE AND IS UNVISITED
    public boolean goodMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return isSafe(x,y+1) && w.isUnknown(x,y+1);
            case 1:
                return isSafe(x+1,y) && w.isUnknown(x+1,y);
            case 2:
                return isSafe(x,y-1) && w.isUnknown(x,y-1);
            case 3:
                return isSafe(x-1,y) && w.isUnknown(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION CERTAINLY CONTAINS WUMPUS AND NO PIT
    public boolean killWumpusMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return isWumpus(x,y+1) && noPit(x,y+1);
            case 1:
                return isWumpus(x+1,y) && noPit(x+1,y);
            case 2:
                return isWumpus(x,y-1) && noPit(x,y-1);
            case 3:
                return isWumpus(x-1,y) && noPit(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION IS SAFE AND HAVE UNVISITED NEIGHBORS
    public boolean retreatMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                //System.out.println("    isSafe:" + isSafe(x,y+1));
                //System.out.println("    haveUnvisited:" + haveUnvisited(x,y+1));
                return isSafe(x,y+1) && haveUnvisited(x,y+1);
            case 1:
                //System.out.println("    isSafe:" + isSafe(x+1,y));
                //System.out.println("    haveUnvisited:" + haveUnvisited(x+1,y));
                return isSafe(x+1,y) && haveUnvisited(x+1,y);
            case 2:
                //System.out.println("    isSafe:" + isSafe(x,y-1));
                //System.out.println("    haveUnvisited:" + haveUnvisited(x,y-1));
                return isSafe(x,y-1) && haveUnvisited(x,y-1);
            case 3:
                //System.out.println("    isSafe:" + isSafe(x-1,y));
                //System.out.println("    haveUnvisited:" + haveUnvisited(x-1,y));
                return isSafe(x-1,y) && haveUnvisited(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION MIGHT NOT CONTAIN A PIT AND CERTAIN NOT WUMPUS
    public boolean perhapsPitMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return !isPit(x,y+1) && noWumpus(x,y+1);
            case 1:
                return !isPit(x+1,y) && noWumpus(x+1,y);
            case 2:
                return !isPit(x,y-1) && noWumpus(x,y-1);
            case 3:
                return !isPit(x-1,y) && noWumpus(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION IS SAFE AND HAVE UNVISITED NEIGHBORS IN DIAGONAL
    public boolean longRetreatMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return isSafe(x,y+1) && haveUnvisitedDiagonal(x,y+1);
            case 1:
                return isSafe(x+1,y) && haveUnvisitedDiagonal(x+1,y);
            case 2:
                return isSafe(x,y-1) && haveUnvisitedDiagonal(x,y-1);
            case 3:
                return isSafe(x-1,y) && haveUnvisitedDiagonal(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION MIGHT NOT CONTAIN A WUMPUS AND CERTAIN NOT PIT
    public boolean perhapsWumpusMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return !isWumpus(x,y+1) && noPit(x,y+1);
            case 1:
                return !isWumpus(x+1,y) && noPit(x+1,y);
            case 2:
                return !isWumpus(x,y-1) && noPit(x,y-1);
            case 3:
                return !isWumpus(x-1,y) && noPit(x-1,y);
            default:
                return false;
        }
    }
    //TRUE IF DIRECTION MIGHT NOT CONTAIN A WUMPUS
    public boolean perhapsWumpusPitMove(int dir)
    {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        switch(dir){
            case 0:
                return !isWumpus(x,y+1);
            case 1:
                return !isWumpus(x+1,y);
            case 2:
                return !isWumpus(x,y-1);
            case 3:
                return !isWumpus(x-1,y);
            default:
                return false;
        }
    }
    
    /**********************************************************************/
    //  This section holds the IS/NO/HAVE-functions for coord information   
    /**********************************************************************/
    
    //TRUE IF COORDINATES ARE SAFE, NO WUMPUS AND NO PIT
    public boolean isSafe(int x, int y)
    {
        return (noWumpus(x,y) && noPit(x,y));
    }
    /*TRUE IF CERTAIN A WUMPUS*/
    public boolean isWumpus(int x, int y)
    {
        if(noWumpus(x, y) || !w.isValidPosition(x, y)) return false;
        int stenches = (w.hasStench(x+1, y)?1:0) + (w.hasStench(x, y+1)?1:0) + (w.hasStench(x-1, y)?1:0) + (w.hasStench(x, y-1)?1:0);
        if( (stenches >= 2) && w.isUnknown(x, y)){
            return true;
        }

        return false;
    }
    /*TRUE IF CERTAIN NOT A WUMPUS*/
    public boolean noWumpus(int x, int y)
    {
        if(((!w.hasStench(x+1, y) && w.isVisited(x+1, y))|| 
            (!w.hasStench(x, y+1) && w.isVisited(x, y+1))|| 
            (!w.hasStench(x-1, y) && w.isVisited(x-1, y))|| 
            (!w.hasStench(x, y-1) && w.isVisited(x, y-1))||
            (w.isVisited(x, y))
            )){
            return true;
        }
        /*else if((w.hasStench(x+1, y) || w.hasStench(x, y+1) || w.hasStench(x-1, y) || w.hasStench(x, y-1)) &&
                (isWumpus(x+1, y+1) || isWumpus(x+1, y-1) || isWumpus(x-1, y+1) || isWumpus(x-1, y-1))){
            System.out.println("HERE IM, ONES AGAIN");
            return true;
        }*/
        return false;
    }
    /*TRUE IF CERTAIN A PIT*/
    public boolean isPit(int x, int y){
            
        if(w.hasPit(x, y) == true) return true;
       
        if(w.hasBreeze(x+1, y))
        {
            if (!((w.hasPit(x+1, y+1) ^ w.isVisited(x+1, y+1)) ^ w.isValidPosition(x+1, y+1)) &&
                !((w.hasPit(x+1, y-1) ^ w.isVisited(x+1, y-1)) ^ w.isValidPosition(x+1, y-1)) &&
                !((w.hasPit(x+1+1, y) ^ w.isVisited(x+1+1, y)) ^ w.isValidPosition(x+1+1, y)))
            {
                return true;
            }
        }      
        if(w.hasBreeze(x-1, y))
        {
            if (!((w.hasPit(x-1, y+1) ^ w.isVisited(x-1, y+1)) ^ w.isValidPosition(x-1, y+1)) &&
                    !((w.hasPit(x-1, y-1) ^ w.isVisited(x-1, y-1)) ^ w.isValidPosition(x-1, y-1)) &&
                    !((w.hasPit(x-1-1, y) ^ w.isVisited(x-1-1, y)) ^ w.isValidPosition(x-1-1, y)))  
            {
                return true;
            }
        }
        if(w.hasBreeze(x, y+1))
        {
            if (!((w.hasPit(x-1, y+1) ^ w.isVisited(x-1, y+1)) ^ w.isValidPosition(x-1, y+1)) &&
                !((w.hasPit(x+1, y+1) ^ w.isVisited(x+1, y+1)) ^ w.isValidPosition(x+1, y+1)) &&
                !((w.hasPit(x, y+1+1) ^ w.isVisited(x, y+1+1)) ^ w.isValidPosition(x, y+1+1)))
            {               
                return true;
            }
        }
        if(w.hasBreeze(x, y-1))
        {
            if (!((w.hasPit(x-1, y-1) ^ w.isVisited(x-1, y-1)) ^ w.isValidPosition(x-1, y-1)) &&
                !((w.hasPit(x+1, y-1) ^ w.isVisited(x+1, y-1)) ^ w.isValidPosition(x+1, y-1)) &&
                !((w.hasPit(x, y-1-1) ^ w.isVisited(x, y-1-1)) ^ w.isValidPosition(x, y-1-1)))  
            {            
                return true;
            }
        }
       
       
        return false;
    }

    /*TRUE IF CERTAIN NOT A PIT*/
    public boolean noPit(int x, int y)
    {
        if(((!w.hasBreeze(x+1, y) && w.isVisited(x+1, y))|| 
            (!w.hasBreeze(x, y+1) && w.isVisited(x, y+1))|| 
            (!w.hasBreeze(x-1, y) && w.isVisited(x-1, y))|| 
            (!w.hasBreeze(x, y-1) && w.isVisited(x, y-1))||
            (w.isVisited(x, y) && !w.hasPit(x, y))
            )){
            return true;
        }
        return false;
    }
    /*TRUE IF HAVE UNVISITED NEIGHBORS*/
    public boolean haveUnvisited(int x, int y)
    {    
        return (w.isUnknown(x,y+1)) || (w.isUnknown(x+1,y)) || (w.isUnknown(x,y-1)) || (w.isUnknown(x-1,y));
    }
    /*TRUE IF HAVE UNVISITED NEIGHBORS*/
    public boolean haveUnvisitedDiagonal(int x, int y)
    {    
        return (w.isUnknown(x+1,y+1)) || (w.isUnknown(x-1,y-1)) || (w.isUnknown(x+1,y-1)) || (w.isUnknown(x-1,y+1));
    }
    /*TRUE IF A NEIGHBOR IS WUMPUS*/
    public boolean haveWumpus(int x, int y)
    {    
        if(w.hasStench(x,y)){
            if(isWumpus(x+1,y)){
                return true;
            }
            else if(isWumpus(x,y+1)){
                return true;
            }
            else if(isWumpus(x-1,y)){
                return true;
            }
            else if(isWumpus(x,y-1)){
                return true;
            }
        }
        return false;
    }
    
    /**********************************************************************/
    //  This section have other helping functions
    /**********************************************************************/
    
   //GIVEN CURRENT AND WANTED DIRECTION, ROTATE TO WANTED
    public void rotatePlayer(int currentDir, int wantedDir)
    {
        System.out.println(currentDir + "::" + wantedDir);
        if(currentDir == wantedDir){
            return;
        }//(0,0), (1,1), (2,2), (3,3)
        if(Math.abs(currentDir-wantedDir) == 2){
            w.doAction(A_TURN_LEFT);
            w.doAction(A_TURN_LEFT);
            return;
        }//(0,2), (2,0), (1,3), (3,1)
        if(currentDir-wantedDir < 0 || (currentDir == 3 && wantedDir == 0)){
            w.doAction(A_TURN_RIGHT);
            return;
        }//(0,1), (1,2), (2,3), (3,0)
        if(currentDir-wantedDir > 0 || (currentDir == 0 && wantedDir == 0)){
            w.doAction(A_TURN_LEFT);
            return;
        }//(1,0), (2,1), (3,2), (0,3) 
    }
    
    
    
    
     
}

