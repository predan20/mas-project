package mas.behaviour.player;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import mas.agent.Player;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Cooperate;
import mas.onto.NextRound;

public class HandleNextRoundRequest extends AchieveREResponder {
    
    public HandleNextRoundRequest(Player a) {
        super(a, null);
        reset(getMessageTemplate());
    }
    
    
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        //get the oponent's last action
        try {
            Action action = (Action)getPlayer().getContentManager().extractContent(request);
            NextRound nextRound = (NextRound) action.getAction();
            
            if(nextRound.getOponentLastAction() != null){
                getPlayer().getOponentHistory().add(nextRound.getOponentLastAction());
            }
        } catch (UngroundedException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        
        //send AGREE reply
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        return agree;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        response.setPerformative(ACLMessage.INFORM);
        try {
            getPlayer().getContentManager().fillContent(response, new Action(getPlayer().getAID(), new Cooperate(getPlayer().getAID())));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        return response;
    }



    private MessageTemplate getMessageTemplate(){
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST), 
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AxelrodTournamentOntology.ONTOLOGY_NAME), 
                        new MessageTemplate(new NextRoundActionMatchExpression(getPlayer().getContentManager()))));
        return template;
    }
    
    private Player getPlayer(){
        return (Player) myAgent;
    }
    
    
    /**
     * Matches a message with {@link NextRound} action as content.
     */
    private static class NextRoundActionMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public NextRoundActionMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
            try {
                ContentElement el = cm.extractContent(msg);
                if(el instanceof Action){
                    Action action = (Action)el;
                    
                    if(action.getAction() instanceof NextRound){
                        return true;
                    }
                }
            } catch (UngroundedException e) {
                throw new RuntimeException(e);
            } catch (CodecException e) {
                throw new RuntimeException(e);
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
    }

}
