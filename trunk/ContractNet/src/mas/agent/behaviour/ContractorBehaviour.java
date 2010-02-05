package mas.agent.behaviour;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mas.AgentUtil;
import mas.onto.Component;
import mas.onto.ConfigTender;
import mas.onto.Configuration;
import mas.onto.GraphicsCard;
import mas.onto.Motherboard;
import mas.onto.Processor;
import mas.onto.Task;
import mas.onto.Tender;

public class ContractorBehaviour extends ContractNetResponder {

    public ContractorBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
    }
    
    protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
        Task task = null;
        try {
            task = (Task) myAgent.getContentManager().extractContent(cfp);
            System.out.println("Agent " + myAgent.getLocalName()+": CFP received.");
        } catch (UngroundedException e1) {
            throw new RuntimeException(e1);
        } catch (CodecException e1) {
            throw new RuntimeException(e1);
        } catch (OntologyException e1) {
            throw new RuntimeException(e1);
        }
        
        Tender tender = this.createTender(task);
        if (tender != null) {//check if capable of providing configuration
            System.out.println("Agent " + myAgent.getLocalName()+": Proposing ");
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            
            try {
                myAgent.getContentManager().fillContent(propose, tender);
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
    
    private Tender createTender(Task task){
        List<ConfigTender> result = new ArrayList<ConfigTender>();
        
        List<Component> inventory = AgentUtil.readInventory(myAgent.getLocalName());
        
        Collections.sort(inventory, new Comparator<Component>(){
            @Override
            public int compare(Component o1, Component o2) {
                return o1.getPrice() - o2.getPrice();
            }
            
        });
        
        for(Configuration config : task.getConfigurations()){
            for(Component c : config.getComponents()){
                if(c !=  null){
                    c.setCount(config.getRequiredNumber());
                }
            }
            
            
            List<Component> proposal = new ArrayList<Component>();
            
            for(Component component : inventory){
                Component match = null;
                
                Component matched = matches(component, config);
                
                if(matched != null){
                    match = component;
                    
                    if(matched == component){
                        matched = null;
                    }
                }
                
                
                //add something to the proposal only if we matched the requirement
                if(match != null){
                    match = match.clone();
                    int matchedCount = matched == null ? config.getRequiredNumber() : matched.getCount();
                    if(component.getCount() > matchedCount){
                        match.setCount(matchedCount);
                        
                        //reduce the available count
                        component.setCount(component.getCount() - matchedCount);
                        
                        if(matched != null){
                            matched.setCount(0);
                        }
                    }else if(component.getCount() != 0){
                        //reduce the available to zero
                        if(matched !=null){
                            matched.setCount(matched.getCount() - component.getCount());
                        }
                        
                        component.setCount(0);
                    }
                    
                    proposal.add(match);
                    
                    //search until the configuration is completed
                    if(config.isProposalComlete(proposal)){
                        break;
                    }
                }
            }
            
            //add the proposal to the tender only if complete
            if(config.isProposalComlete(proposal)){
                result.add(new ConfigTender(config, proposal));
            }
        }
        
        if(result.isEmpty()){
            return null;
        }
        return new Tender(result);
    }
    
    
    
    private Component matches(Component component, Configuration config){
        Component res = null;
        Component com = config.getProcessor();
       
        if(matches(component, com = config.getProcessor(), Processor.class)
                || matches(component, com = config.getMotherBoard(), Motherboard.class)
                || matches(component, com = config.getGraphicsCard(), GraphicsCard.class)){
            
            if(com == null){
                res = component;
            }else{
                res = com;
            }
        }
        
        return res;
    }
    
    private boolean matches(Component component, Component requiredComponent, Class<? extends Component> requiredType){

        // if not specified in the task we immediately match
        if (requiredComponent == null) {
            return component.getClass().equals(requiredType);
        }

        if (requiredComponent.getCount() == 0) {
            return false;
        }

        if (!component.getClass().equals(requiredComponent.getClass())) {
            return false;
        }

        boolean priceMatches = requiredComponent.getPrice() == 0
                || requiredComponent.getPrice() >= component.getPrice();
        boolean manufacturerMatches = requiredComponent.getManufacturer() == null
                || requiredComponent.getManufacturer().length() == 0
                || requiredComponent.getManufacturer().equals(component.getManufacturer());
        boolean qualityMatches = requiredComponent.getQuality() == null || requiredComponent.getQuality().length() == 0
                || requiredComponent.getQuality().equals(component.getQuality());

        // match
        if (priceMatches && manufacturerMatches && qualityMatches) {
            return true;
        }

        return false;
    }

}
