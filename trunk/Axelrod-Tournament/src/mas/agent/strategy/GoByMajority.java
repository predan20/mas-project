package mas.agent.strategy;

import java.util.List;

import mas.agent.Player;
import mas.onto.Cooperate;
import mas.onto.Defect;
import mas.onto.PlayerAction;

/**
 * Strategy that cooperates on the first round. On all subsequent rounds,
 * examines the history of the other player's actions, counting the total number
 * of defections and cooperations by the other player. If the other player's
 * defections outnumber his cooperations, go-by-majority will defect; otherwise
 * this strategy will cooperate.
 */
public class GoByMajority extends AbstractStrategy {
    public GoByMajority(Player player) {
        super(player);
    }

    @Override
    public PlayerAction getNextAction() {
        if(getRound() == 1){
            return new Cooperate(getPlayer().getAID());
        }
        
        List<PlayerAction> oponentHistory = getOponentHistory();
        
        //count the number of defects of the oponent
        int oponentDefectsCount = 0;
        for(PlayerAction a : oponentHistory){
            if(a instanceof Defect){
                oponentDefectsCount++;
            }
        }
        
        //count my cooperations
        int oponentCoopCount = 0;
        for(PlayerAction a : oponentHistory){
            if(a instanceof Cooperate){
                oponentCoopCount++;
            }
        }
        
        //defect if oponent defects outnumber own cooperations
        if(oponentDefectsCount > oponentCoopCount){
            return new Defect(getPlayer().getAID());
        }
        
        return new Cooperate(getPlayer().getAID());
    }

}
