package mas.behaviour.tournament;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mas.agent.AxelrodTournament;
import mas.onto.Cooperate;
import mas.onto.Defect;
import mas.onto.PlayerAction;
import blockworld.Env;

/**
 * One shot behavior for initializing the Blockworld GUI.
 */
public class EndOfTournament extends OneShotBehaviour {

    public EndOfTournament(AxelrodTournament a) {
        super(a);
    }

    @Override
    public void action() {
       /* Env env = Env.getEnv();
        int noRounds=getTournament().getNumberOfRounds();
        int bombsPerColumn=200;
        int noCols=noRounds/bombsPerColumn;
   //     if (noRounds%bombsPerColumn!=0)
     //   	noCols++;
       
       
        env.setSize((noRounds/bombsPerColumn+1)*10+4, ( noRounds < bombsPerColumn ? noRounds : bombsPerColumn)+5); //4 squares horizontal margin, 4 squares between columns
        									//max 50 bombs per column
        env.setSenseRange(Integer.MAX_VALUE);
        
        for(int row = 2; row < getTournament().getNumberOfRounds() + 2; row++){
            env.addBomb(new Point(6, row));
            env.addBomb(new Point(7, row));
        }
       
        for (int b=0; b< noRounds;b++){
        	env.addBomb(new Point(10*(b/bombsPerColumn)+6, b%bombsPerColumn+2));
        	System.out.println(b+"-"+(10*(b/bombsPerColumn)+6)+" "+(b%bombsPerColumn+2));
        	env.addBomb(new Point(10*(b/bombsPerColumn)+7, b%bombsPerColumn+2));
        }
        
        */
        Iterator<AID> players = getTournament().getPlayers().iterator();
        
        AID player1 = players.next();
        AID player2 = players.next();
        
        File f= new File("results.txt");
                
        List<PlayerAction> historyP1 = getTournament().getPlayerHistory(player1);
        List<PlayerAction> historyP2 = getTournament().getPlayerHistory(player2);
        
        try{
        	FileWriter fw = new FileWriter(f);
        	int cc=0;
        	int cd=0;
        	int dc=0;
        	int dd=0; 
        	for (int i=0;i<historyP1.size();i++) {
        		if (historyP1.get(i) instanceof Cooperate) {
        			fw.write("C-");
        			if (historyP1.get(i) instanceof Cooperate) {
        				fw.write("C \n");
        				cc++;        				
        			}
        		}
        //TO COMPLETE			
        		
        	}
        		
    
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally{
        	
        }
  
        
        /*
        env.addAgent(player1.getLocalName());
        env.enter(player1.getLocalName(), 5.0, 2.0, "blue");
        
        env.addAgent(player2.getLocalName());
        env.enter(player2.getLocalName(), 8.0, 2.0, "red");
*/
    }
    
    private AxelrodTournament getTournament(){
        return (AxelrodTournament) myAgent;
    }
}
