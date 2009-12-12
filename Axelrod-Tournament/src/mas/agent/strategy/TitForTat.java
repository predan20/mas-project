package mas.agent.strategy;

import mas.agent.Player;
import mas.onto.Cooperate;
import mas.onto.PlayerAction;

/**
 * Strategy that:
 * 1. On first round cooperates
 * 2. On all other rounds, do what the oponent did on the previous round.
 */
public class TitForTat extends AbstractStrategy {

	public TitForTat(Player player) {
		super(player);
	}

	@Override
	public PlayerAction getNextAction() {
		if (getRound() == 1)
			return new Cooperate(getPlayer().getAID());
		else {
			PlayerAction lastMove = getOponentLastAction();
			lastMove.setPlayer(getPlayer().getAID());
			return lastMove;
		}
	}

}
