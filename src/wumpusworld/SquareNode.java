/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;
import java.util.*;

public class SquareNode {
    public int stench;
    public int breeze;
    public int pit;
    public int wumpus;
    public int visited;
    
    
    public SquareNode()
    {
        //0 Means nothing, -1 Means possible, 1 Means certain
        this.stench = 0;
        this.breeze = 0;
        this.pit = 0;
        this.wumpus = 0;
        this.visited = 0;
    }
    
    
    public int getThing(String t)
    {
        if (t.toLowerCase().equals("s"))
        {
            return stench;
        } else if (t.toLowerCase().equals("b"))
        {
            return breeze;
        } else if (t.toLowerCase().equals("p"))
        {
            return pit;
        } else if (t.toLowerCase().equals("w"))
        {
            return wumpus;
        } else 
        {
            return -1;
        }
    }
    
    public void setThing(int value, String t)
    {
        if (t.toLowerCase().equals("s"))
        {
            this.stench = value;
        } else if (t.toLowerCase().equals("b"))
        {
            this.breeze = value;
        } else if (t.toLowerCase().equals("p"))
        {
            this.pit = value;
        } else if (t.toLowerCase().equals("w"))
        {
            this.wumpus = value;
        } else if (t.toLowerCase().equals("pw"))
        {
            this.pit = value;
            this.wumpus = value;
        }
    }
    
    
    
    
}
