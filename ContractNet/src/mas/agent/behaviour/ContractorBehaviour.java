package mas.agent.behaviour;

import mas.onto.Configuration;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class ContractorBehaviour extends ContractNetResponder {

    public ContractorBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
    }
    
    protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
        Configuration conf = null;
        try {
            conf = (Configuration) myAgent.getContentManager().extractContent(cfp);
            System.out.println("Agent " + myAgent.getLocalName()+": CFP received.");
        } catch (UngroundedException e1) {
            throw new RuntimeException(e1);
        } catch (CodecException e1) {
            throw new RuntimeException(e1);
        } catch (OntologyException e1) {
            throw new RuntimeException(e1);
        }
        
        if (true) {//check if capable of providing configuration
            System.out.println("Agent " + myAgent.getLocalName()+": Proposing ");
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            
            Configuration proposal = new Configuration();
            try {
                myAgent.getContentManager().fillContent(propose, proposal);
            } catch (CodecException e) {
                throw new RuntimeException(e);
            } catch (OntologyException e) {
                throw new RuntimeException(e);
            }
            return propose;
        }
        else {
            // We refuse to provide a proposal
            System.out.println("Agent " + myAgent.getLocalName()+": Refuse");
            throw new RefuseException("no-suitable-configuration");
        }
    }
    
    protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            return inform;
    }
    
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println("Agent " + myAgent.getLocalName()+": Proposal rejected");
    }

}
