package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public class SingleUnitWithRiskFactor extends AbstractStrategy {
    
	public SingleUnitWithRiskFactor(Bidder player) {
		super(player);
	}

	
	/**
     * Retrieves the bidder's next bid.
     * @return {@link Bid} instance.
     */
    public Bid getNextBid() {
		
    	int lastPrice=getBidder().getAuction().getGoods().iterator().next().getInitialPrize();
    	if(getBidder().getLastBid() != null){
    	   lastPrice = getBidder().getLastBid().getAmmount(); 
    	}
    	int newBidAmmount = lastPrice + getBidder().getAuction().getMinStep();
    	double riskFactor=0.90; //for which the player would go lower even if he affords the items
    	
    	if (newBidAmmount <= getBidder().getBidderState().getBudget() && Math.random() < riskFactor){
    		return new Bid(newBidAmmount, 1, this.getBidder().getAID());
    	}
    	
    	return null;
    }
}
