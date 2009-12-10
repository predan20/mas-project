package mas.behaviour.tournament;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Set;

import mas.agent.AxelrodTournament;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.PlayerAction;

public class NotifyForOponentLastAction extends OneShotBehaviour {

    public NotifyForOponentLastAction(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        Set<AID> players = getTournament().getPlayers();
        
        for ( AID player : players){
            for(AID oponent : players){
                if(!oponent.equals(player)){
                    sendOponentHistory(player, oponent);
                }
            }
        }

    }
    
    private void sendOponentHistory(AID player, AID oponent){
        List<PlayerAction> history = getTournament().getPlayerHistory(oponent);
        
        //don't send anything if there is no history.
        //this happens on the first turn
        if(!history.isEmpty()){
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(player);
            message.setOntology(AxelrodTournamentOntology.ONTOLOGY_NAME);
            
            PlayerAction lastAction = history.get(history.size() - 1);
            try {
                getTournament().getContentManager().fillContent(message, new Action(lastAction.getPlayer(), lastAction));
            } catch (CodecException e) {
                throw new RuntimeException(e);
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
            
            getTournament().send(message);
        }
    }
    
    private AxelrodTournament getTournament(){
        return (AxelrodTournament) myAgent;
    }
}
