package mas.agent.strategy;

import java.util.List;

import mas.agent.Player;
import mas.onto.Cooperate;
import mas.onto.Defect;
import mas.onto.PlayerAction;

/**
 * We use the Opponent's History to see the percentages of Defects and Cooperations
 * We use that percentage to Defect or Cooperate (about as much as the opponent did)
 */
public class HistoryBased extends AbstractStrategy {

	public HistoryBased(Player player) {
		super(player);
	}

	@Override
	public PlayerAction getNextAction() {
		
		List<PlayerAction> opH=getPlayer().getOponentHistory(); 
		int noCoop=0;
		for (int i=0;i<opH.size();i++){
			if (opH.get(i) instanceof Cooperate)
				noCoop++;
		}
		double perc=noCoop/(double)opH.size();
		return Math.random() < perc ? 
				new Cooperate(getPlayer().getAID()):
				new Defect(getPlayer().getAID());
	}

}
