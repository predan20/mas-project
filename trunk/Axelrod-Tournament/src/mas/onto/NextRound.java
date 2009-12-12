package mas.onto;

import mas.agent.AxelrodTournament;
import jade.content.Concept;

/**
 * Concept used by the {@link AxelrodTournament} agent to notify players for the
 * next turn in the game and to request their next action. This class has a
 * single property for the oponent's last action. This way there is no need for
 * separate message with the oponent's history of moves.
 */
public class NextRound implements Concept {
    private PlayerAction oponentLastAction;

    public NextRound() {
    }

    /**
     * Creates a new instance using the given last action of the oponent.
     * @param lastAction
     */
    public NextRound(PlayerAction lastAction) {
        this.oponentLastAction = lastAction;
    }

    /**
     * Gets the oponent's last action.
     * @return {@link PlayerAction} instance.
     */
    public PlayerAction getOponentLastAction() {
        return oponentLastAction;
    }

    public void setOponentLastAction(PlayerAction oponentLastAction) {
        this.oponentLastAction = oponentLastAction;
    }
}
