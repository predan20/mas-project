package mas.onto;

import jade.content.Concept;

public class NextRound implements Concept {
    private PlayerAction oponentLastAction;
    
    public NextRound(){}
    
    public NextRound(PlayerAction lastAction){
        this.oponentLastAction = lastAction;
    }
}
