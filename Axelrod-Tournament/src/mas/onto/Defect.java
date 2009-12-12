package mas.onto;

import jade.core.AID;

/**
 * Concept representing one of the possible player actions, namely DEFECT.
 */
public class Defect extends PlayerAction {

    public Defect(){}
    
    public Defect(AID player) {
        super(player);
    }

}
