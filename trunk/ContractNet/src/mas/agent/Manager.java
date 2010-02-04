package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Date;

import mas.agent.behaviour.ManagerBehaviour;
import mas.onto.ComputerConfigurationOntology;

public class Manager extends Agent {
    public static final long RESPOND_TIMEOUT = 5000;
    @Override
    protected void setup() {
        try {
            Thread.currentThread().sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            // Fill the CFP message
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            for (int i = 0; i < args.length; ++i) {
                System.out.println(args[i]);
                msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
            }
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            msg.setOntology(ComputerConfigurationOntology.ONTOLOGY_NAME);
            msg.setLanguage(new SLCodec().getName());
            msg.setReplyByDate(new Date(System.currentTimeMillis() + RESPOND_TIMEOUT));
            
            getContentManager().registerOntology(ComputerConfigurationOntology.getInstance());
            getContentManager().registerLanguage(new SLCodec());
            
            addBehaviour(new ManagerBehaviour(this, msg));
        }
    }
    
}
