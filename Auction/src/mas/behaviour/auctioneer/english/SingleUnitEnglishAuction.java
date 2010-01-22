package mas.behaviour.auctioneer.english;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.agent.Auctioneer;

public class SingleUnitEnglishAuction extends OneShotBehaviour {
    
    public SingleUnitEnglishAuction(Auctioneer agent){
        super(agent);
    }
    @Override
    public void action() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new HandleEnglishAuctionBids(getAuctioneer()));
        
        myAgent.addBehaviour(b);

    }
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }

}
