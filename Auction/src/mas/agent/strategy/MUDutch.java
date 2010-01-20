package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;
import mas.onto.Good;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public class MUDutch extends AbstractStrategy {
    
	public MUDutch(Bidder player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	
	//i have a Bidder player
	/**
     * Retrieves the bidder's next bid.
     * @return {@link Bid} instance.
     */
    public Bid getNextBid() {
		
    	Good theGood=getBidder().getAuction().getGoods().iterator().next();
    	int itemsAvailable = theGood.getAvailableCount();
    	int lastPrice=getBidder().getLastPrize(theGood);
    	int budget=this.getBidder().getBidderState().getBudget();
    	int itemsWanted=this.getBidder().getBidderState().getItemsWanted();
    	
    	
    	double riskFactor=0.95; //for which the player would go lower even if he affords the items
    	
    // from here modified	
    	//he bids one item at a time
    	//he pays higher for the first item to get rid of some AllOrNothing bidders
    	int maxItemsToGet=Math.min(itemsWanted, itemsAvailable);
    	if (itemsAvailable>0 && 
    			(budget/itemsWanted*maxItemsToGet)>=lastPrice*maxItemsToGet && 
    			Math.random()<riskFactor)
    		return new Bid(lastPrice, this.getBidder().getAID());
    	
    	return null;
    }
}
