package mas.behaviour.tournament;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import mas.agent.AxelrodTournament;
import mas.onto.Cooperate;
import mas.onto.PlayerAction;

/**
 * One shot behavior for logging the results of the match in a results.txt file.
 */
public class EndOfTournament extends OneShotBehaviour {

    public EndOfTournament(AxelrodTournament a) {
        super(a);
    }

    @Override
    public void action() {
       
        Iterator<AID> players = getTournament().getPlayers().iterator();
        
        AID player1 = players.next();
        AID player2 = players.next();
        
        
        
        File f= new File("results.txt");
                
        List<PlayerAction> historyP1 = getTournament().getPlayerHistory(player1);
        List<PlayerAction> historyP2 = getTournament().getPlayerHistory(player2);
        
        FileWriter fw = null;
        try{
        	fw = new FileWriter(f);
        	int cc=0;
        	int cd=0;
        	int dc=0;
        	int dd=0; 
        	fw.write(player1.getLocalName()+" VS. "+player2.getLocalName()+"\n");
        	for (int i=0;i<historyP1.size();i++) {
        		if (historyP1.get(i) instanceof Cooperate) {
        			fw.write("C-");
        			if (historyP2.get(i) instanceof Cooperate) {
        				fw.write("C \n");
        				cc++;        				
        			}
        			else {
        				fw.write("D \n");
        				cd++;        				
        			}
        			fw.flush();
        		}
        		else {
        			fw.write("D-");
        			if (historyP2.get(i) instanceof Cooperate) {
        				fw.write("C \n");
        				dc++;        				
        			}
        			else {
        				fw.write("D \n");
        				dd++;        				
        			}
        			fw.flush();
        		}
        //TO COMPLETE	
        		
        		
        		
        	}
        	fw.write("----------------\nNumber of rounds of type\n");
        	fw.write("C-C: "+cc+"\nC-D: "+cd+"\nD-C: "+dc+"\nD-D: "+dd+"\n");
        	fw.write("----------------\nScores\n");
        	fw.write(player1.getLocalName()+": "+(3*cc+5*dc+dd)+"\n");
        	fw.write(player2.getLocalName()+": "+(3*cc+5*cd+dd)+"\n");
        } 
        catch (IOException e) {
			if(fw != null){
			    try {
                    fw.close();
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
			}
            throw new RuntimeException(e);
		}
        finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
    
    private AxelrodTournament getTournament(){
        return (AxelrodTournament) myAgent;
    }
}
