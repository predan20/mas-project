package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public class MGDutch extends AbstractStrategy {
    
	public MGDutch(Bidder player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	
	//i have a Bidder player
	/**
     * Retrieves the bidder's next bid.
     * @return {@link Bid} instance.
     */
    public Bid getNextBid() {
		
    //  retrieve itemsAvailable number of goods from the first type from the auction
    	int itemsAvailable=3;
    //  retrieve lastPrice
    	int lastPrice=700;
    	int budget=this.getBidder().getBidderState().getBudget();
    	int itemsWanted=this.getBidder().getBidderState().getItemsWanted();
    	
    	
    	double riskFactor=0.95; //for which the player would go lower even if he affords the items
    	
    	if (itemsAvailable>=itemsWanted && budget>=lastPrice*itemsWanted && Math.random()<riskFactor)
    		return new Bid(lastPrice*itemsWanted, this.getBidder().getAID());
    	
    	return null;
    }
}
