package mas.behaviour.bidder.dutch;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import mas.agent.Bidder;
import mas.behaviour.bidder.BidNow;
import mas.behaviour.bidder.GetWinner;
import mas.behaviour.bidder.ReceiveInitialPrize;
import mas.onto.AuctionDescription;

public class MultiUnitDutchAuction extends TickerBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public MultiUnitDutchAuction(Bidder agent, AuctionDescription desc){
        super(agent, 1000);
        auctionDescription = desc;
    }
    @Override
    public void onTick() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new ReceiveInitialPrize(getBidder()));
        b.addSubBehaviour(new BidNow(getBidder()));
        //b.addSubBehaviour(new GetWinner(getBidder()));
        //TODO something here to run it/stop it
        myAgent.addBehaviour(b);

    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }

}
