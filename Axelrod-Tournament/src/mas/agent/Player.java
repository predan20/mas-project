package mas.agent;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import mas.Constants;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Register;

public class Player extends Agent {

    @Override
    protected void setup() {
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        // register in the tournament
        addBehaviour(new SimpleAchieveREInitiator(this, null) {

            @Override
            protected void handleAgree(ACLMessage msg) {
                // TODO Auto-generated method stub
                super.handleAgree(msg);
            }

            @Override
            protected void handleFailure(ACLMessage msg) {
                // TODO Auto-generated method stub
                super.handleFailure(msg);
            }

            @Override
            protected void handleRefuse(ACLMessage msg) {
                // TODO Auto-generated method stub
                super.handleRefuse(msg);
            }

            @Override
            protected ACLMessage prepareRequest(ACLMessage msg) {
                // Fill the REQUEST REGISTER message
                msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(getTournamentAID());
                msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                msg.setOntology(AxelrodTournamentOntology.ONTOLOGY_NAME);
                msg.setLanguage(new SLCodec().getName());
                
               /* getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
                getContentManager().registerLanguage(new SLCodec());
*/                
                try {
                    getContentManager().fillContent(msg, new Action(getTournamentAID(), new Register()));
                } catch (CodecException e) {
                    throw new RuntimeException(e);
                } catch (OntologyException e) {
                    throw new RuntimeException(e);
                }
                return super.prepareRequest(msg);
            }

            private AID getTournamentAID() {
                AID result = null;
                
                DFAgentDescription agent = new DFAgentDescription();
                ServiceDescription service = new ServiceDescription();
                service.setName(Constants.TOURNAMENT_SERVICE_NAME);
                
                try {
                    DFAgentDescription[] results = DFService.search(myAgent, agent);
                    if(results.length > 0){
                        result = results[0].getName();
                    }
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }
                
                return result;
            }

        });
    }

}
