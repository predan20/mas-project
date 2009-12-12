package mas.agent.strategy;

import mas.agent.Player;
import mas.onto.Cooperate;
import mas.onto.Defect;
import mas.onto.PlayerAction;

/**
 * On 1st round, defect. If the opponent also defected, then play TIT-FOR-TAT.
 * Otherwise cooperate or defect with 0.5 probability.
 */
public class Tester extends TitForTat {
    /**
     * Determines if we are playing TIT-FOT-TAT.
     */
    private boolean playTFT = false;

    public Tester(Player player) {
        super(player);
    }

    @Override
    public PlayerAction getNextAction() {
        if (getRound() == 1) {
            return new Defect(getPlayer().getAID());
        }

        if (getRound() == 2) {
            PlayerAction lastOponentMove = getOponentLastAction();
            if (lastOponentMove instanceof Defect) {
                playTFT = true;
            }
        }

        if (playTFT) {
            return super.getNextAction();
        }

        return Math.random() < 0.5 ? 
                new Defect(getPlayer().getAID()) : 
                new Cooperate(getPlayer().getAID());
    }

}
