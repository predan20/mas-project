package mas.behaviour.auctioneer.dutch;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import mas.agent.Auctioneer;
import mas.behaviour.auctioneer.AnnouncePrize;
import mas.onto.AuctionDescription;
import mas.onto.Good;

public class MultiUnitDutchAuction extends TickerBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public MultiUnitDutchAuction(Auctioneer agent, AuctionDescription desc){
        super(agent, 1500);
        auctionDescription = desc;
    }
    @Override
    public void onTick() {
    	
    	Good theGoods = getAuctioneer().getAuctionDescription().getGoods().iterator().next();
        int goodsLeft=theGoods.getAvailableCount();
        int price=theGoods.getInitialPrize();
        int minStep=getAuctioneer().getAuctionDescription().getMinStep();
        int minPrice=theGoods.getReservationPrize();
    	
    	
    	if (goodsLeft<=0 || price < minPrice){
        	System.out.print(myAgent.getLocalName()+": END of AUCTION. ");
        	if (goodsLeft<=0)
        		System.out.println("There are no more goods left");
        	else
        		System.out.println("The minimum price has been reached");
        	stop();
        }
    	else {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new AnnouncePrize(getAuctioneer(), auctionDescription.getGoods().iterator().next().getInitialPrize(),
        		auctionDescription.getGoods().iterator().next().getAvailableCount()));
        b.addSubBehaviour(new ListenForDutchBids(getAuctioneer(), auctionDescription.getGoods().iterator().next().getInitialPrize(), 
        		auctionDescription.getGoods().iterator().next().getAvailableCount()));
        myAgent.addBehaviour(b);
        //System.out.println("goodsLeft,price,minStep,minPrice = "+goodsLeft+" "+price+" "+minStep+" "+minPrice);
        if (goodsLeft>0 && price >= minPrice){
        	theGoods.setInitialPrize(price-minStep);
        }
    	}
        // 
        //if there are goods left and the price is not minimum
        //decrease price by minimum step 
        //else stop
        
        
        /*else{
        	System.out.println("exit through stop");
        	stop();
        }*/
        
        
    }
    
  @Override
  	public void stop() {
	// Auto-generated method stub
	 // TODO another behaviour that adds to the topic that the auction stopped?
	super.stop();
}
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }

}
