package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;
import mas.onto.Good;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public class MUDutchAllOrNothing extends AbstractStrategy {
    
	public MUDutchAllOrNothing(Bidder player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	
	//i have a Bidder player
	/**
     * Retrieves the bidder's next bid.
     * @return {@link Bid} instance.
     */
    public Bid getNextBid() {
		
    	Good theGood=this.getBidder().getAuction().getGoods().iterator().next();
    	int itemsAvailable = theGood.getAvailableCount();
    	int lastPrice=this.getBidder().getLastPrize(theGood);
    	int budget=this.getBidder().getBidderState().getBudget();
    	int itemsWanted=this.getBidder().getBidderState().getItemsWanted();
    	    	
    	double riskFactor=0.95; //for which the player would go lower even if he affords the items
    	
    	if (itemsAvailable>=itemsWanted && budget>=lastPrice*itemsWanted && Math.random()<riskFactor){
    		System.out.println("MUDAON: "+lastPrice*itemsWanted+" "+ itemsWanted+" "+ this.getBidder().getLocalName()+" "+budget);
    		return new Bid(lastPrice*itemsWanted, itemsWanted, this.getBidder().getAID());
    	}
    	return null;
    }
}
