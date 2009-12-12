package mas.agent.strategy;

import java.util.List;

import mas.agent.Player;
import mas.onto.PlayerAction;

/**
 * Abstract implementation of the {@link Strategy} interface. All other
 * implementors are recommended to extend this class. The class provides access
 * to the player using the strategy and his and his oponent's histories.
 * 
 * 
 */
public abstract class AbstractStrategy implements Strategy {
    private Player player;

    public AbstractStrategy(Player player) {
        this.player = player;
    }

    protected List<PlayerAction> getOponentHistory() {
        return player.getOponentHistory();
    }

    protected List<PlayerAction> getOwnHistory() {
        return player.getOwnHistory();
    }

    protected int getRound() {
        return getOwnHistory().size() + 1;
    }

    protected Player getPlayer() {
        return this.player;
    }

    protected PlayerAction getOponentLastAction() {
        return getOponentHistory().get(getRound() - 2);
    }

}
