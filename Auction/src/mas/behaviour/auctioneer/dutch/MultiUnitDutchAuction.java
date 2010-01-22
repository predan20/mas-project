package mas.behaviour.auctioneer.dutch;

import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import mas.agent.Auctioneer;
import mas.behaviour.auctioneer.AnnouncePrize;
import mas.onto.AuctionDescription;
import mas.onto.Good;

public class MultiUnitDutchAuction extends TickerBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public MultiUnitDutchAuction(Auctioneer agent, AuctionDescription desc){
        super(agent, 500);
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
        if (goodsLeft>0 && price >= minPrice){
        	theGoods.setInitialPrize(price-minStep);
        }
    	}
    }
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }

}
