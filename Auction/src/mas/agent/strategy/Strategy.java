package mas.agent.strategy;

import mas.onto.Bid;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public interface Strategy {
    /**
     * Retrieves the bidder's next bid.
     * @return {@link Bid} instance.
     */
    Bid getNextBid();
}
