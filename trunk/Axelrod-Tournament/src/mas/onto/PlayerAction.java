package mas.onto;

import jade.content.Concept;
import jade.core.AID;

public class PlayerAction implements Concept {
    private AID player;
    
    public PlayerAction(){}
    
    public PlayerAction(AID player) {
        setPlayer(player);
    }
    
    public void setPlayer(AID player){
        this.player = player;
    }
    
    public AID getPlayer(){
        return this.player;
    }

}
