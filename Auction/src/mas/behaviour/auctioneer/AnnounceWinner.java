package mas.behaviour.auctioneer;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import mas.AgentUtil;
import mas.agent.Auctioneer;
import mas.onto.AuctionOntology;
import mas.onto.Prize;
import mas.onto.Winner;

public class AnnounceWinner extends OneShotBehaviour {

    private final int soldPrice;
    private final int soldItems;
    private final AID winner;

    public AnnounceWinner(Auctioneer agent, AID winner, int soldItems, int soldPrice) {
        super(agent);
        this.winner = winner;
        this.soldPrice=soldPrice;
        this.soldItems=soldItems;
    }

    @Override
    public void action() {
        // send initial prize
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setOntology(AuctionOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());

        AgentUtil.addAuctionTopicReceiver(myAgent, msg);

        // simply add an instance of the prize concept using the content
        // manager
        try {
        	System.out.println(myAgent.getLocalName()+": Announces winner "+this.winner.getLocalName()+" with items "+soldItems+" and price "+soldPrice);
            myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), new Winner(this.winner, soldItems,soldPrice)));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(msg);

    }

}
