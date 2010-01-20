package mas.agent.strategy;

import mas.agent.Bidder;

/**
 * Abstract implementation of the {@link Strategy} interface. All other
 * implementors are recommended to extend this class. The class provides access
 * to the bidder using the strategy.
 */
public abstract class AbstractStrategy implements Strategy {
    private Bidder bidder;

    public AbstractStrategy(Bidder player) {
        this.bidder = player;
    }

    protected Bidder getBidder() {
        return this.bidder;
    }
}
