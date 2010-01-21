package mas.behaviour.bidder;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Constants;
import mas.agent.Bidder;
import mas.onto.AuctionOntology;
import mas.onto.BidderConfig;
import mas.onto.Prize;
import mas.onto.Register;
import mas.onto.Winner;

public class GetWinner extends CyclicBehaviour {
    
    public GetWinner(Bidder agent){
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.blockingReceive(getMessageTemplate());
        
        try {
            ContentElement el = myAgent.getContentManager().extractContent(msg);
            if(el instanceof Action){
                Action action = (Action)el;
                
                if(action.getAction() instanceof Winner){
                    Winner wBidder = (Winner)action.getAction();
                    AID winner = wBidder.getWinner();
                    if (winner==myAgent.getAID())
                    {
                    	int soldItems = wBidder.getSoldItems();
                    	int soldPrice = wBidder.getSoldPrice();
                    	//TODO update items and price for bidder - done?
                    	Bidder myBidder = (Bidder) myAgent;
                    	BidderConfig myBidderState= myBidder.getBidderState();
                    	myBidderState.setBudget(myBidderState.getBudget()-wBidder.getSoldPrice());
                    	myBidderState.setItemsWanted(myBidderState.getItemsWanted()-wBidder.getSoldItems());
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
                        new MessageTemplate(new WinnerMatchExpression(myAgent.getContentManager()))));
        return template;
    }
    
    /**
     * Matches a message with {@link Register} action as content.
     */
    private static class WinnerMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public WinnerMatchExpression(ContentManager cm){
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
