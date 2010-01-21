package mas.behaviour.auctioneer.dutch;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import mas.agent.Auctioneer;
import mas.behaviour.auctioneer.AnnouncePrize;
import mas.onto.AuctionDescription;

public class MultiUnitDutchAuction extends TickerBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public MultiUnitDutchAuction(Auctioneer agent, AuctionDescription desc){
        super(agent, 1500);
        auctionDescription = desc;
    }
    @Override
    public void onTick() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new AnnouncePrize(getAuctioneer(), auctionDescription.getGoods().iterator().next().getInitialPrize(),
        		auctionDescription.getGoods().iterator().next().getAvailableCount()));
        b.addSubBehaviour(new ListenForDutchBids(getAuctioneer(), auctionDescription.getGoods().iterator().next().getInitialPrize(), 
        		auctionDescription.getGoods().iterator().next().getAvailableCount()));
        myAgent.addBehaviour(b);
        // TODO 
        //if there are goods left and the price is not minimum
        //decrease price by minimum step 
        //else stop

    }
    
  @Override
  	public void stop() {
	// TODO Auto-generated method stub
	super.stop();
}
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }

}
