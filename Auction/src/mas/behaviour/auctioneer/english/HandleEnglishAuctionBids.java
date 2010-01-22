package mas.behaviour.auctioneer.english;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.AgentUtil;
import mas.agent.Auctioneer;
import mas.onto.AuctionOntology;
import mas.onto.Bid;
import mas.onto.Good;
import mas.onto.Winner;

public class HandleEnglishAuctionBids extends SimpleBehaviour {
    public static final int AUCTION_END_TIMEOUT = 10000;
    
    private Bid highestBid;
    private long timeOfLastBid = System.currentTimeMillis();
    private boolean winnerAnnounced = false;
    private boolean done = false;
    
    
    public HandleEnglishAuctionBids(Auctioneer agent){
        super(agent);
    }
    
    @Override
    public void action() {
        
        //first check for completion
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeOfLastBid > AUCTION_END_TIMEOUT){
            
            Good theGood = getAuctioneer().getAuctionDescription().getGoods().iterator().next();
            if(highestBid != null && !winnerAnnounced){
                if(theGood.getReservationPrize() >= highestBid.getAmmount()){
                    ACLMessage reply = new ACLMessage(ACLMessage.FAILURE);
                    AgentUtil.addAuctionTopicReceiver(myAgent, reply);
                    reply.setContent("Item not sold (reservation prize not met).");
                    myAgent.send(reply);
                }else{
                    //WE HAVE A WINNER
                    
                    //send winner
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setOntology(AuctionOntology.ONTOLOGY_NAME);
                    msg.setLanguage(new SLCodec().getName());
        
                    AgentUtil.addAuctionTopicReceiver(myAgent, msg);
        
                    try {
                        myAgent.getContentManager().fillContent(
                                msg,
                                new Action(myAgent.getAID(), new Winner(highestBid.getAgent(), highestBid.getNumberOfItems(),
                                        highestBid.getAmmount())));
                    } catch (CodecException e) {
                        throw new RuntimeException(e);
                    } catch (OntologyException e) {
                        throw new RuntimeException(e);
                    }
                    myAgent.send(msg);
                    
                    winnerAnnounced = true;
                }
            }else{
                ACLMessage reply = new ACLMessage(ACLMessage.FAILURE);
                AgentUtil.addAuctionTopicReceiver(myAgent, reply);
                reply.setContent("Item not sold (no bids).");
                myAgent.send(reply);
            }
            done = true;
            return;
        }
        
        //handle bids
    	ACLMessage msg = myAgent.receive(getMessageTemplate());
    	
    	if(msg == null){
    	    block(50);
    	    return;
    	}
        
        try {
            ContentElement el = myAgent.getContentManager().extractContent(msg);
            if(el instanceof Action){
                Action action = (Action)el;
                
                if(action.getAction() instanceof Bid){
                    Bid bid = (Bid)action.getAction();
                    
                    if(highestBid == null || highestBid.getAmmount() + getAuctioneer().getAuctionDescription().getMinStep() <= bid.getAmmount()){
                        highestBid = bid;
                        timeOfLastBid = System.currentTimeMillis();
                        
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
//                        reply.setContent("invalid (the bid must be higher than the last bid with specified minimum step)");
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

    @Override
    public boolean done() {
        return done;
    }

}
