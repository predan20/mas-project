package mas.onto;

import jade.content.Concept;

public class NextRound implements Concept {
    private PlayerAction oponentLastAction;
    
    public NextRound(){}
    
    public NextRound(PlayerAction lastAction){
        this.oponentLastAction = lastAction;
    }

    public PlayerAction getOponentLastAction() {
        return oponentLastAction;
    }

    public void setOponentLastAction(PlayerAction oponentLastAction) {
        this.oponentLastAction = oponentLastAction;
    }
}
