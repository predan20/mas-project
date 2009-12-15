package mas.agent.strategy;

import mas.onto.PlayerAction;

/**
 * Interface representing given strategy that a player will follow during play
 * in the Axelrod's Tournament.
 */
public interface Strategy {
    /**
     * Retrieves the player's next action.
     * @return {@link PlayerAction} instance.
     */
    PlayerAction getNextAction();
}
