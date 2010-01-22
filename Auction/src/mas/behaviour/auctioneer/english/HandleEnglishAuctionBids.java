package mas.behaviour.auctioneer.english;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.AgentUtil;
import mas.agent.Auctioneer;
import mas.onto.AuctionOntology;
import mas.onto.Bid;

public class HandleEnglishAuctionBids extends CyclicBehaviour {
    private Bid highestBid;
    
    
    public HandleEnglishAuctionBids(Auctioneer agent){
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
                    
                    if(highestBid == null || highestBid.getAmmount() < bid.getAmmount()){
                        highestBid = bid;
                        
                        //announce the valid bid to the auction topic
                        ACLMessage topicMsg = new ACLMessage(ACLMessage.INFORM);
                        AgentUtil.addAuctionTopicReceiver(myAgent, topicMsg);
                        topicMsg.setOntology(AuctionOntology.ONTOLOGY_NAME);
                        topicMsg.setLanguage(new SLCodec().getName());

                        // add the Bid instance using the content manager
                        try {
                            myAgent.getContentManager().fillContent(topicMsg, new Action(myAgent.getAID(), bid));
                        } catch (CodecException e) {
                            throw new RuntimeException(e);
                        } catch (OntologyException e) {
                            throw new RuntimeException(e);
                        }
                        myAgent.send(topicMsg);
                    }else{
//                        ACLMessage reply = msg.createReply();
//                        msg.setPerformative(ACLMessage.FAILURE);
//                        reply.setContent("invalid");
//                        myAgent.send(reply);
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
    
    
    private MessageTemplate getMessageTemplate() {
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
                        MessageTemplate.MatchReceiver(new AID[]{myAgent.getAID()})), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AuctionOntology.ONTOLOGY_NAME), 
                        new MessageTemplate(new BidMatchExpression(myAgent.getContentManager()))));
        return template;
    }
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }
    
    private class BidMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public BidMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
            if (msg.getSender().equals(getAuctioneer().getAID())){
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
                return false;
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
    }

}
