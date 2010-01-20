package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public class MGDutchAllOrNothing extends AbstractStrategy {
    
	public MGDutchAllOrNothing(Bidder player) {
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
    //  retrieve lastPrice
    	int budget=this.player.getBudget();
    //	int itemsWanted=player.getNumberOfGoods(typeofGood);
    	
    	
    	double riskFactor=0.95; //for which the player would go lower even if he affords the items
    	//if (itemsAvailable>=itemsWanted && budget>=lastPrice*itemsWanted && Math.random()<riskFactor)
    		//return new Bid(lastPrice*itemsWanted, player.getAID());
    	
    	return null;
    }
}
