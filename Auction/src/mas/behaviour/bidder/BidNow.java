package mas.behaviour.bidder;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import mas.AgentUtil;
import mas.agent.Bidder;
import mas.agent.strategy.MUDutch;
import mas.agent.strategy.MUDutchAllOrNothing;
import mas.agent.strategy.Strategy;
import mas.onto.AuctionOntology;

public class BidNow extends OneShotBehaviour {

    public BidNow(Bidder bidder){
        super(bidder);
    }
    
    @Override
    public void action() {
        //use the bidder's strategy to determine the next bid
        Strategy strategy = getBidder().getCurrentStrategy();
                
        mas.onto.Bid bid = strategy.getNextBid();
        if(bid == null){
        	return;
        }
        
        //construct the bid message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AgentUtil.addAuctionTopicReceiver(myAgent, msg);
        msg.setOntology(AuctionOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());

        // add the Bid instance using the content manager
        try {
            myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), bid));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(msg);
        
        if ((getBidder().getCurrentStrategy() instanceof MUDutch )||(getBidder().getCurrentStrategy() instanceof MUDutchAllOrNothing ))
        	myAgent.addBehaviour(new GetWinner(getBidder()));
    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }

}
