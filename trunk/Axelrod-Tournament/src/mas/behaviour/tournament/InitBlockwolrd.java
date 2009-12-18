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
        int noRounds=getTournament().getNumberOfRounds();
        int bombsPerColumn=getTournament().getBombsPerColumn();

       
       
        env.setSize(((noRounds/bombsPerColumn+( noRounds%bombsPerColumn==0? 0 : 1))*10+4) , ( noRounds < bombsPerColumn ? noRounds : bombsPerColumn)+5); //4 squares horizontal margin, 4 squares between columns
        									
        env.setSenseRange(Integer.MAX_VALUE);
        

        for (int b=0; b< noRounds;b++){
        	env.addBomb(new Point(10*(b/bombsPerColumn)+6, b%bombsPerColumn+2));
        	//System.out.println(b+"-"+(10*(b/bombsPerColumn)+6)+" "+(b%bombsPerColumn+2));
        	env.addBomb(new Point(10*(b/bombsPerColumn)+7, b%bombsPerColumn+2));
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
