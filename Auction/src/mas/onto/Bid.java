package mas.onto;

import jade.content.Concept;
import jade.core.AID;

public class Bid implements Concept {
    private int ammount;
    private int numberOfItems;
    private AID agent;
    
    public Bid(){}
    
    public Bid(int ammount, int numberOfItems, AID agent){
        this.ammount = ammount;
        this.agent = agent;
        this.numberOfItems = numberOfItems;
        
        
    }

    public int getAmmount() {
        return ammount;
    }
    
    public int getNumberOfItems() {
        return numberOfItems;
    }
    
    public AID getAgent() {
        return agent;
    }
    
    
}
