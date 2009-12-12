package mas.behaviour.tournament;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

import java.awt.Point;
import java.util.Iterator;

import mas.agent.AxelrodTournament;
import blockworld.Env;

/**
 * One shot behavior for initializing the Blockworld GUI.
 */
public class InitBlockwolrd extends OneShotBehaviour {

    public InitBlockwolrd(AxelrodTournament a) {
        super(a);
    }

    @Override
    public void action() {
        Env env = Env.getEnv();
        env.setSenseRange(Integer.MAX_VALUE);
        
        for(int row = 2; row < getTournament().getNumberOfRounds() + 2; row++){
            env.addBomb(new Point(6, row));
            env.addBomb(new Point(7, row));
        }
        
        Iterator<AID> players = getTournament().getPlayers().iterator();
        
        AID player1 = players.next();
        AID player2 = players.next();
        
        
        env.addAgent(player1.getLocalName());
        env.enter(player1.getLocalName(), 5.0, 2.0, "blue");
        
        env.addAgent(player2.getLocalName());
        env.enter(player2.getLocalName(), 8.0, 2.0, "red");

    }
    
    private AxelrodTournament getTournament(){
        return (AxelrodTournament) myAgent;
    }
}
