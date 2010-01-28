package mas.agent;

import mas.agent.behaviour.ContractorBehaviour;
import mas.onto.ComputerConfigurationOntology;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Contractor extends Agent {

    @Override
    protected void setup() {
        getContentManager().registerOntology(ComputerConfigurationOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and(MessageTemplate.MatchOntology(ComputerConfigurationOntology.ONTOLOGY_NAME),
                                    MessageTemplate.MatchLanguage(new SLCodec().getName())),
                MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                                    MessageTemplate.MatchPerformative(ACLMessage.CFP) ));
        
        addBehaviour(new ContractorBehaviour(this, template));
        
        System.out.println("Agent "+getLocalName()+" waiting for CFP...");
    }
    
}
