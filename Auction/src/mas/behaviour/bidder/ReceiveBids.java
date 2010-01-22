package mas.behaviour.bidder;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Constants;
import mas.agent.Bidder;
import mas.onto.AuctionOntology;
import mas.onto.Bid;

public class ReceiveBids extends SimpleBehaviour {
    private boolean done;
    
    
    public ReceiveBids(Bidder agent){
        super(agent);
    }
    
    @Override
    public void action() {
    	ACLMessage msg = myAgent.blockingReceive(getMessageTemplate());
        
        try {
            ContentElement el = myAgent.getContentManager().extractContent(msg);
            if(el instanceof Action){
                Action action = (Action)el;
                
                if(action.getAction() instanceof Bid){
                    Bid bid = (Bid)action.getAction();
                    
                    //if it was our bid - do nothing
                    if(bid.getAgent().equals(getBidder().getAID())){
                        return;
                    }
                    getBidder().setLastBid(bid);
                    
                    //reschedule the whole parent behaviour
                    this.parent.reset();
                    myAgent.addBehaviour(this.parent);
                    done = true;
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
                        new MessageTemplate(new BidMatchExpression(myAgent.getContentManager()))));
        return template;
    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }
    
    private class BidMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public BidMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
            //accept the Bid announcement only from the Auctioneer
            if(!msg.getSender().equals(getBidder().getAuctioneer())){
                return false;
            }
            
            try {
                ContentElement el = cm.extractContent(msg);
                if(el instanceof Action && ((Action)el).getAction() instanceof Bid){
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

    @Override
    public boolean done() {
        return done;
    }

}
