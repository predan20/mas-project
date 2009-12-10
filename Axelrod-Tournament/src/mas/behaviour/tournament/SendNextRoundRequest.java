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
    protected Vector prepareRequests(ACLMessage msg) {
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setOntology(AxelrodTournamentOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());
        
        long currentTime = System.currentTimeMillis();
        msg.setReplyByDate(new Date(currentTime + 10000));
        
        //add an instance of NextRound concept using the content manager
        try {
            myAgent.getContentManager()
                    .fillContent(msg, new Action(getTournament().getAID(), new NextRound()));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        
        Vector result = new Vector();
        for (AID player : getTournament().getPlayers()){
            ACLMessage message =  (ACLMessage) msg.clone();
            message.addReceiver(player);
            result.add(message);
        }
        return result;
    }
    
    @Override
    protected void handleInform(ACLMessage inform) {
        getTournament().getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        try {
            ContentElement content = myAgent.getContentManager().extractContent(inform);
            if(content instanceof Action){
                Action act = (Action) content;
                getTournament().handlePlayerAction((PlayerAction)act.getAction());
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
    
}
