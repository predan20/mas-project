package mas.behaviour.auctioneer;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

import java.util.Date;

import mas.AgentUtil;
import mas.agent.Auctioneer;
import mas.onto.AuctionDescription;
import mas.onto.AuctionOntology;
import mas.onto.Register;

/**
 * Acts as a FIPA-request initiator requesting a {@link Register} action from the bidder agent.
 */
public class SendAuctionRequest extends SimpleAchieveREInitiator {
	private static final int REPLY_TIMEOUT = 1000;
	private AuctionDescription desc;
	
    public SendAuctionRequest(Auctioneer a, AuctionDescription desc) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
        this.desc = desc;
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        // Fill the REQUEST REGISTER message
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setOntology(AuctionOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + REPLY_TIMEOUT));
        
        AgentUtil.addAuctionTopicReceiver(myAgent, msg);

        // simply add an instance of Register concept using the content manager
        try {
            myAgent.getContentManager()
                    .fillContent(msg, new Action(myAgent.getAID(), new Register(desc)));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        return super.prepareRequest(msg);
    }
}
