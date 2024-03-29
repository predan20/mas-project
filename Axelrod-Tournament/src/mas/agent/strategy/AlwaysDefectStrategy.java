package mas.agent.strategy;

import mas.agent.Player;
import mas.onto.Defect;
import mas.onto.PlayerAction;

/**
 * Strategy that always returns a {@link Defect} instance.
 *
 */
public class AlwaysDefectStrategy extends AbstractStrategy {

    public AlwaysDefectStrategy(Player player) {
        super(player);
    }

    @Override
    public PlayerAction getNextAction() {
        return new Defect(getPlayer().getAID());
    }

}
