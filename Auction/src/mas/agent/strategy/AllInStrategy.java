package mas.agent.strategy;

import mas.agent.Bidder;
import mas.onto.Bid;

public class AllInStrategy extends AbstractStrategy {

    public AllInStrategy(Bidder agent){
        super(agent);
    }
    @Override
    public Bid getNextBid() {
        return new Bid(getBidder().getBidderState().getBudget(), 1, getBidder().getAID());
    }

}
