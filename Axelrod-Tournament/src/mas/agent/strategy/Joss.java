package mas.agent.strategy;

import mas.agent.Player;
import mas.onto.Defect;
import mas.onto.PlayerAction;

public class Joss extends TitForTat {

	public Joss(Player player) {
		super(player);
	}

	@Override
	public PlayerAction getNextAction() {
		return Math.random() < 0.3 ? 
				new Defect(getPlayer().getAID()) : 
				super.getNextAction();
	}

}
