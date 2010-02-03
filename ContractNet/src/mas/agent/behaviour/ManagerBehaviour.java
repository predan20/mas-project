package mas.agent.behaviour;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import mas.AgentUtil;
import mas.onto.Configuration;
import mas.onto.Task;

public class ManagerBehaviour extends ContractNetInitiator {

    public ManagerBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        
        try {
            myAgent.getContentManager().fillContent(cfp,  AgentUtil.readManagerTask());
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        //job done
        System.out.println("Agent " + myAgent.getLocalName() + ": Job done by " + inform.getSender().getLocalName());
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        //evaluate all the proposals
        
        AID bestProposer = null;
        ACLMessage accept = null;
        
        Enumeration e = responses.elements();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                
                if (true) {//compare proposals
                    bestProposer = msg.getSender();
                    accept = reply;
                }
            }
        }
        
        // Accept the proposal of the best proposer
        if (accept != null) {
            System.out.println("Accepting proposal from responder " + bestProposer.getName());
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        }
    }
}
