package mas.agent;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

import java.util.ArrayList;
import java.util.List;

import mas.Constants;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Register;

public class AxelrodTournament extends Agent {
    private List<AID> players = new ArrayList<AID>();

    @Override
    protected void setup() {
        //add the tournament service to the DF
        registerService();
        
        //add behaviour for the register action
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.and (MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST), 
                                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST)), 
                MessageTemplate.and (MessageTemplate.MatchOntology(AxelrodTournamentOntology.ONTOLOGY_NAME), 
                                    new MessageTemplate(new MessageTemplate.MatchExpression(){

                                        @Override
                                        public boolean match(ACLMessage msg) {
                                            try {
                                                ContentElement el = getContentManager().extractContent(msg);
                                                if(el instanceof Action){
                                                    Action action = (Action)el;
                                                    
                                                    if(action.getAction() instanceof Register){
                                                        return true;
                                                    }
                                                }
                                            } catch (UngroundedException e) {
                                                throw new RuntimeException(e);
                                            } catch (CodecException e) {
                                                throw new RuntimeException(e);
                                            } catch (OntologyException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return false;
                                        }
                                    })));

        addBehaviour(new SimpleAchieveREResponder(this, template) {
            protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
                if (players.size() < 2) {
                    ACLMessage agree = request.createReply();
                    agree.setPerformative(ACLMessage.AGREE);
                    return agree;
                } else {
                    throw new RefuseException("only-two-players-allowed");
                }
            }

            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
                    throws FailureException {
                getPlayers().add(request.getSender());
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                return inform;
            }
        });
    }

    public List<AID> getPlayers() {
        return players;
    }
    
    private void registerService() {
        // Register the Axelrod's Tournament in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("game");
        sd.setName(Constants.TOURNAMENT_SERVICE_NAME);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            throw new RuntimeException(fe);
        }
    }

}
