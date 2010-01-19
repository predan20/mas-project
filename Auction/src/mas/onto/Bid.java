package mas.onto;

import jade.content.Concept;
import jade.core.AID;

public class Bid implements Concept {
    private int ammount;
    private AID agent;
    
    public Bid(){}
    
    public Bid(int ammount, AID agent){
        this.ammount = ammount;
        this.agent = agent;
    }

    public int getAmmount() {
        return ammount;
    }
    
    public AID getAgent() {
        return agent;
    }
}
