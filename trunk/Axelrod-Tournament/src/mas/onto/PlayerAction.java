package mas.onto;

import jade.content.Concept;
import jade.core.AID;

/**
 * Concept representing a possible player action during play.
 * This is a base class containing one property for the player who performed the action.
 */
public class PlayerAction implements Concept {
    private AID player;
    
    public PlayerAction(){}
    
    public PlayerAction(AID player) {
        setPlayer(player);
    }
    
    public void setPlayer(AID player){
        this.player = player;
    }
    
    /**
     * The player who performed the action.
     * @return AID of the player
     */
    public AID getPlayer(){
        return this.player;
    }

}
