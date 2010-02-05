package mas.agent.behaviour;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.Vector;

import mas.AgentUtil;
import mas.onto.ConfigTender;
import mas.onto.Task;
import mas.onto.Tender;

public class ManagerBehaviour extends ContractNetInitiator {
    private Task task = null;
    
    public ManagerBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
        
        try {
            myAgent.getContentManager().fillContent(cfp,  getTask());
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (OntologyException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Task getTask(){
        if(task == null){
            task = AgentUtil.readManagerTask();
        }
        return task;
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
        int bestPrice = Integer.MAX_VALUE;
        
        Enumeration e = responses.elements();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                //prepare he reply
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                
                //extract the tender from the message
                Tender tender = null;
                try {
                    tender = (Tender) myAgent.getContentManager().extractContent(msg);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
                
                if(!validate(tender, getTask())){
                    msg.setContent("Incomplete tender!");
                    continue;
                }
                
                //compare proposals based on total price
                if (tender.getTotalPrice() < bestPrice) {
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
    
    private boolean validate(Tender tender, Task task){
        if(tender == null){
            return false;
        }
        
        if(task.getConfigurations().size() > tender.getSubtenders().size()){
            return false;
        }
        
        for(ConfigTender ct : tender.getSubtenders()){
            if(!ct.getConfig().isProposalComlete(ct.getComponents())){
                return false;
            }
        }
        return true;
    }
}
