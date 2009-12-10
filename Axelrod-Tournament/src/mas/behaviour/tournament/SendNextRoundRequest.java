package mas.behaviour.tournament;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import mas.agent.AxelrodTournament;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.NextRound;
import mas.onto.PlayerAction;

/**
 * Acts as a FIPA-request initiator requesting a {@link NextRound} action.
 */
public class SendNextRoundRequest extends AchieveREInitiator {
    
    public SendNextRoundRequest(AxelrodTournament a) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
    }

    @Override
    protected Vector<ACLMessage> prepareRequests(ACLMessage msg) {
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setOntology(AxelrodTournamentOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());
        
        long currentTime = System.currentTimeMillis();
        msg.setReplyByDate(new Date(currentTime + 10000));
        
        //all messages (2)
        Vector<ACLMessage> result = new Vector<ACLMessage>();
        
        Iterator<AID> players = getTournament().getPlayers().iterator();
        AID player1 = players.next();
        AID player2 = players.next();
        
        //player1 message
        result.add(newMessage(msg, player1, player2));
        //player1 message
        result.add(newMessage(msg, player2, player1));
        
        return result;
    }
    
    private ACLMessage newMessage(ACLMessage baseMessage, AID receiver, AID oponent){
        ACLMessage message =  (ACLMessage) baseMessage.clone();
        message.addReceiver(receiver);
        
        //get oponent's last action
        PlayerAction oponentLastAction = getPlayerLastAction(oponent);
        
        //add an instance of NextRound concept using the content manager
        try {
            myAgent.getContentManager()
                    .fillContent(message, new Action(getTournament().getAID(), new NextRound(oponentLastAction)));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        
        return message;
    }
    
    @Override
    protected void handleInform(ACLMessage inform) {
        getTournament().getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        try {
            ContentElement content = myAgent.getContentManager().extractContent(inform);
            if(content instanceof Action){
                Action act = (Action) content;
                getTournament().handlePlayerAction((PlayerAction)act.getAction());
                
                //block so that the UI gets updated
                block(100);
            }
        } catch (UngroundedException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        super.handleInform(inform);
    }

    private AxelrodTournament getTournament(){
        return (AxelrodTournament) myAgent;
    }
    
    private PlayerAction getPlayerLastAction(AID player){
        List<PlayerAction> history = getTournament().getPlayerHistory(player);
        if(history.isEmpty()){
            return null;
        }
        return history.get(history.size() - 1);
    }
    
}
