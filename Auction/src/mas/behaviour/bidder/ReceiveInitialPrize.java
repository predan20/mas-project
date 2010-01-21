package mas.behaviour.bidder;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Constants;
import mas.agent.Bidder;
import mas.onto.AuctionOntology;
import mas.onto.Good;
import mas.onto.Prize;
import mas.onto.Register;

public class ReceiveInitialPrize extends OneShotBehaviour {
    
    public ReceiveInitialPrize(Bidder agent){
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.blockingReceive(getMessageTemplate());
        
        try {
            ContentElement el = myAgent.getContentManager().extractContent(msg);
            if(el instanceof Action){
                Action action = (Action)el;
                
                if(action.getAction() instanceof Prize){
                    Prize prize = (Prize)action.getAction();
                    int initialPrize = prize.getAmmount();
                    
                    //
                    Bidder b = ((Bidder)myAgent);
                    //TODO: make it generic and not expect only one good
                    Good theGood = b.getAuction().getGoods().iterator().next();
                    b.setLastPrize(theGood, initialPrize);
                }
            }
        } catch (UngroundedException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * The message template used to filter incoming messages to be handled by
     * this behavior. That is INFORM message using the {@link AuctionOntology}
     * and containing {@link Prize} instance as content.
     */
    private MessageTemplate getMessageTemplate() {
        TopicManagementHelper topicHelper;
        try {
            topicHelper = (TopicManagementHelper) myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        AID jadeTopic = topicHelper.createTopic(Constants.AUCTION_TOPIC);
        
        
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
                        MessageTemplate.MatchTopic(jadeTopic)), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AuctionOntology.ONTOLOGY_NAME), 
                        new MessageTemplate(new PrizeMatchExpression(myAgent.getContentManager()))));
        return template;
    }
    
    /**
     * Matches a message with {@link Register} action as content.
     */
    private static class PrizeMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public PrizeMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
            try {
                ContentElement el = cm.extractContent(msg);
                if(el instanceof Action && ((Action)el).getAction() instanceof Prize){
                    return true;
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
