package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

import java.util.ArrayList;
import java.util.List;

import mas.behaviour.player.GetTournamentId;
import mas.behaviour.player.HandleNextRoundRequest;
import mas.behaviour.player.SendRegisterRequest;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.PlayerAction;

public class Player extends Agent {
    
    private AID tournamentId = null;
    private List<PlayerAction> oponentHistory = new ArrayList<PlayerAction>();

    @Override
    protected void setup() {
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        SequentialBehaviour playerBehaviour = new SequentialBehaviour();
        playerBehaviour.addSubBehaviour(new GetTournamentId(this));
        playerBehaviour.addSubBehaviour(new SendRegisterRequest(this));
        playerBehaviour.addSubBehaviour(new HandleNextRoundRequest(this));
        
        addBehaviour(playerBehaviour);
    }
    
    public AID getTournamentAID() {
        return tournamentId;
    }
    
    public void setTournamentAID(AID id){
        this.tournamentId = id;
    }

    public List<PlayerAction> getOponentHistory() {
        return oponentHistory;
    }

}
