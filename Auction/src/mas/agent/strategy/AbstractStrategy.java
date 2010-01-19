package mas.agent.strategy;

import mas.agent.Bidder;

/**
 * Abstract implementation of the {@link Strategy} interface. All other
 * implementors are recommended to extend this class. The class provides access
 * to the bidder using this strategy.
 */
public abstract class AbstractStrategy implements Strategy {
    private Bidder player;

    public AbstractStrategy(Bidder player) {
        this.player = player;
    }

    protected Bidder getPlayer() {
        return this.player;
    }
}
