package mas.behaviour.bidder.dutch;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.agent.Bidder;
import mas.behaviour.bidder.Bid;
import mas.behaviour.bidder.ReceiveInitialPrize;
import mas.onto.AuctionDescription;

public class MultiUnitDutchAuction extends OneShotBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public MultiUnitDutchAuction(Bidder agent, AuctionDescription desc){
        super(agent);
        auctionDescription = desc;
    }
    @Override
    public void action() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new ReceiveInitialPrize(getBidder()));
        b.addSubBehaviour(new Bid(getBidder()));
        
        myAgent.addBehaviour(b);

    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }

}
