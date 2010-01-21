package mas.behaviour.auctioneer.dutch;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.Constants;
import mas.agent.Auctioneer;
import mas.behaviour.auctioneer.AnnouncePrize;
import mas.behaviour.auctioneer.AnnounceWinner;
//import mas.behaviour.bidder.ReceiveInitialPrize.PrizeMatchExpression;
import mas.onto.AuctionDescription;
import mas.onto.AuctionOntology;
import mas.onto.Bid;
import mas.onto.Prize;

public class ListenForDutchBids extends OneShotBehaviour {
    //private final AuctionDescription auctionDescription;
    
    private int price;
    private int numberOfGoods;
    public ListenForDutchBids(Auctioneer agent, int price, int numberOfGoods){
        super(agent);
        //auctionDescription = desc;
        this.price = price;
        this.numberOfGoods = numberOfGoods;
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
                    AID bidder = bid.getAgent();
                    int offeredAmmount = bid.getAmmount();
                    int requestedItems = bid.getNumberOfItems();
                    if (offeredAmmount==price*requestedItems && requestedItems <= numberOfGoods){
                    	//if offer is made for this price and it's realizable
                    	//TODO check if it's OK
                    	//update numberOfGoods
                    	//send message to the topic with updates
                    	//send message for the winner
                    	SequentialBehaviour b = new SequentialBehaviour();
                        b.addSubBehaviour(new AnnouncePrize(getAuctioneer(), price, numberOfGoods-requestedItems));
                        b.addSubBehaviour(new AnnounceWinner(getAuctioneer(), bidder, requestedItems, offeredAmmount));
                        myAgent.addBehaviour(b);
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
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }
    
    private static class BidMatchExpression implements MessageTemplate.MatchExpression{
        private ContentManager cm = null;
        
        public BidMatchExpression(ContentManager cm){
            this.cm = cm;
        }
        
        
        @Override
        public boolean match(ACLMessage msg) {
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

}
