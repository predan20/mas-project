package mas.behaviour.bidder;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import mas.AgentUtil;
import mas.Constants;
import mas.agent.Bidder;
import mas.onto.AuctionDescription;
import mas.onto.AuctionOntology;
import mas.onto.Register;

/**
 * Acts as a FIPA-request responder for auctioneer's {@link Register} request.
 */
public class HandleAuctionRequest extends SimpleAchieveREResponder {
    
    public HandleAuctionRequest(Bidder a) {
        super(a, null);
        reset(getMessageTemplate());
    }
    
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        return agree;
    }

    /**
     * Extracts the auction description from the message and adds appropriate
     * for the auction type behavior to the agent.
     */
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
            throws FailureException {
        try {
            ContentElement el = getBidder().getContentManager().extractContent(request);
            if(el instanceof Action){
                Action action = (Action)el;
                
                if(action.getAction() instanceof Register){
                    Register reg = (Register) action.getAction();
                    AuctionDescription desc = reg.getAuctionDesciption();
                    if(desc != null){
                        getBidder().setAuction(desc);
                        getBidder().setAuctioneer(request.getSender());
                        myAgent.addBehaviour(AgentUtil.createBidderBehaviour(desc, getBidder()));
                    }
                }
            }
        } catch (UngroundedException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        response.setPerformative(ACLMessage.INFORM);
        return response;
    }
    
    private Bidder getBidder(){
        return (Bidder) myAgent;
    }
    
    /**
     * The message template used to filter incoming messages to be handled by
     * this behavior. That is FIPA_REQUEST using the
     * {@link AuctionOntology} and containing {@link Register}
     * instance as content.
     */
    private MessageTemplate getMessageTemplate(){
        TopicManagementHelper topicHelper;
        try {
            topicHelper = (TopicManagementHelper) myAgent
                    .getHelper(TopicManagementHelper.SERVICE_NAME);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        AID jadeTopic = topicHelper.createTopic(Constants.AUCTION_TOPIC);
        
        
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST), 
                        MessageTemplate.MatchTopic(jadeTopic)), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AuctionOntology.ONTOLOGY_NAME), 
                        new MessageTemplate(new RegisterActionMatchExpression(getBidder().getContentManager()))));
        return template;
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
