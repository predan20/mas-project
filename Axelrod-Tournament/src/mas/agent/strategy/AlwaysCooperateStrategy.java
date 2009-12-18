package mas.agent.strategy;

import mas.agent.Player;
import mas.onto.Defect;
import mas.onto.Cooperate;
import mas.onto.PlayerAction;

/**
 * Strategy that always returns a {@link Defect} instance.
 *
 */
public class AlwaysCooperateStrategy extends AbstractStrategy {

    public AlwaysCooperateStrategy(Player player) {
        super(player);
    }

    @Override
    public PlayerAction getNextAction() {
        return new Cooperate(getPlayer().getAID());
    }

}
