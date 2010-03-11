package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;

public class AllInStrategy extends AbstractStrategy {

    public AllInStrategy(Bidder agent){
        super(agent);
    }
    @Override
    public Bid getNextBid() {
        int budget = getBidder().getBidderState().getBudget();
        
        Bid bid = null;
        if(getBidder().getLastBid() != null 
                && budget >= getBidder().getLastBid().getAmmount() + getBidder().getAuction().getMinStep()){
            bid = new Bid(budget, 1, getBidder().getAID());
        }
        return bid;
    }

}