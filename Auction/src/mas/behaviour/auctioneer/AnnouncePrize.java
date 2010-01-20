package mas.behaviour.auctioneer;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import mas.AgentUtil;
import mas.agent.Auctioneer;
import mas.onto.AuctionOntology;
import mas.onto.Prize;

public class AnnouncePrize extends OneShotBehaviour {

    private final int prize;

    public AnnouncePrize(Auctioneer agent, int prize) {
        super(agent);
        this.prize = prize;
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
            myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), new Prize(prize)));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(msg);

    }

}