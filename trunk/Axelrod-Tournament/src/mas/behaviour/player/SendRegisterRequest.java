package mas.behaviour.player;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import mas.agent.Player;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Register;

/**
 * Acts as a FIPA-request initiator requesting a {@link Register} action.
 */
public class SendRegisterRequest extends SimpleAchieveREInitiator {
    public SendRegisterRequest(Player a) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
    }

    @Override
    protected ACLMessage prepareRequest(ACLMessage msg) {
        // Fill the REQUEST REGISTER message
        msg.addReceiver(((Player)myAgent).getTournamentAID());
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setOntology(AxelrodTournamentOntology.ONTOLOGY_NAME);
        msg.setLanguage(new SLCodec().getName());

        // simply add an instance of Register concept using the content manager
        try {
            myAgent.getContentManager()
                    .fillContent(msg, new Action(((Player)myAgent).getTournamentAID(), new Register()));
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
        return super.prepareRequest(msg);
    }
}
