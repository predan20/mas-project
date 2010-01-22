package mas.behaviour.bidder.english;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import mas.agent.Bidder;
import mas.agent.strategy.MUDutch;
import mas.agent.strategy.MUDutchAllOrNothing;
import mas.agent.strategy.Strategy;
import mas.behaviour.bidder.GetWinner;
import mas.onto.AuctionOntology;

public class Bid extends OneShotBehaviour {

    public Bid(Bidder bidder){
        super(bidder);
    }
    
    @Override
    public void action() {
        //use the bidder's strategy to determine the next bid
        Strategy strategy = getBidder().getCurrentStrategy();
        
        if(strategy == null){
            throw new RuntimeException("Missing strategy for bidder " + myAgent.getName());
        }
                
        mas.onto.Bid bid = strategy.getNextBid();
        if(bid == null){
        	return;
        }
        
        //construct the bid message
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(getBidder().getAuctioneer());
        msg.setOntology(AuctionOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());

        try {
            myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), bid));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(msg);
    }
    
    public Bidder getBidder(){
        return (Bidder) myAgent;
    }

}
