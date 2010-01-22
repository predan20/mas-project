package mas.behaviour.bidder.english;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.agent.Bidder;
import mas.behaviour.bidder.ReceiveBids;

public class SingleUnitEnglishAuction extends OneShotBehaviour {
    public SingleUnitEnglishAuction(Bidder agent){
        super(agent);
    }
    @Override
    public void action() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new Bid(getBidder()));
        b.addSubBehaviour(new ReceiveBids(getBidder()));
        
        myAgent.addBehaviour(b);
    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }

}
