package mas.behaviour.tournament;

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
import jade.proto.SimpleAchieveREResponder;
import mas.agent.AxelrodTournament;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Register;

/**
 * Acts as a FIPA-request responder for {@link Register} action.
 * When receiving the request this behavior adds the sender's AID to the list of players in the tournament.
 * This behaviors runs until there are two registered players.
 * In case of there are already 2 players registered sends a refuse.
 */
public class HandleRegisterRequest extends SimpleAchieveREResponder {
    private boolean done = false;
    
    public HandleRegisterRequest(AxelrodTournament a) {
        super(a, null);
        reset(getMessageTemplate());
    }
    
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        if (getAxelrodTournament().canAddPlayer()) {
            ACLMessage agree = request.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            return agree;
        } else {
            throw new RefuseException("only-two-players-allowed");
        }
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
            throws FailureException {
        getAxelrodTournament().addPlayer(request.getSender());
        response.setPerformative(ACLMessage.INFORM);
        return response;
    }
    
    private AxelrodTournament getAxelrodTournament(){
        return (AxelrodTournament) myAgent;
    }
    
    /**
     * The message template used to filter incoming messages to be handled by
     * this behavior. That is FIPA_REQUEST using the
     * {@link AxelrodTournamentOntology} and containing {@link Register}
     * instance as content.
     */
    private MessageTemplate getMessageTemplate(){
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST), 
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AxelrodTournamentOntology.ONTOLOGY_NAME), 
                        new MessageTemplate(new RegisterActionMatchExpression(getAxelrodTournament().getContentManager()))));
        return template;
    }
    
    @Override
    public boolean done() {
        return super.done() || done;
    }

    @Override
    public void reset() {
        super.reset();
        done = !getAxelrodTournament().canAddPlayer();
    }

    /**
     * Matches a message with {@link Register} action as content.
     */
    private static class RegisterActionMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public RegisterActionMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
            try {
                ContentElement el = cm.extractContent(msg);
                if(el instanceof Action){
                    Action action = (Action)el;
                    
                    if(action.getAction() instanceof Register){
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
