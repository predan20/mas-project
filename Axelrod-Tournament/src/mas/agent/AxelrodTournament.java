package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mas.Constants;
import mas.behaviour.tournament.HandleRegisterRequest;
import mas.behaviour.tournament.NotifyForOponentLastAction;
import mas.behaviour.tournament.SendNextRoundRequest;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.PlayerAction;


public class AxelrodTournament extends Agent {
    private Map<AID, List<PlayerAction>> playerHistories = new HashMap<AID, List<PlayerAction>>();

    @Override
    protected void setup() {
        //add the tournament service to the DF
        registerService();
        
        //register the tournament ontology for behaviors which receive messages and use it
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());

        
        SequentialBehaviour tournamentLifecycle = new SequentialBehaviour(this);
        
        //add behavior for the register action
        tournamentLifecycle.addSubBehaviour(new HandleRegisterRequest(this));
        
        //add behaviors for the tournament rounds
        SequentialBehaviour nextRoundBehaviour = new SequentialBehaviour(this);
        nextRoundBehaviour.addSubBehaviour(new NotifyForOponentLastAction(this));
        nextRoundBehaviour.addSubBehaviour(new SendNextRoundRequest(this));
        tournamentLifecycle.addSubBehaviour(nextRoundBehaviour);
        
        addBehaviour(tournamentLifecycle);
    }
    
    public void addPlayer(AID player){
        if(!canAddPlayer()){
            throw new RuntimeException("Only 2 players allowed!");
        }
        playerHistories.put(player, new ArrayList<PlayerAction>());
    }
    
    public boolean canAddPlayer(){
        return playerHistories.size() < 2;
    }
    
    public List<PlayerAction> getPlayerHistory(AID player){
        return playerHistories.get(player);
    }
    
    public void addPlayerAction(AID player, PlayerAction action){
        List<PlayerAction> history = playerHistories.get(player);
        if(history == null){
            throw new RuntimeException("Unknown player: " + player);
        }
        
        history.add(action);
    }
    
    public Set<AID> getPlayers(){
        return playerHistories.keySet();
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

    public void handlePlayerAction(PlayerAction action) {
        playerHistories.get(action.getPlayer()).add(action);
    }
    
    

}
