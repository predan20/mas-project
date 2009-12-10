package mas.behaviour.player;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.Constants;
import mas.agent.Player;

public class GetTournamentId extends SimpleBehaviour {
    
    public GetTournamentId(Player player){
        super(player);
    }
    
    @Override
    public void action() {
        DFAgentDescription agent = new DFAgentDescription();
        ServiceDescription service = new ServiceDescription();
        service.setName(Constants.TOURNAMENT_SERVICE_NAME);
        agent.addServices(service);

        try {
            DFAgentDescription[] results = DFService.search(myAgent, agent);
            if (results.length > 0) {
                AID tournamentId = results[0].getName();
                getPlayer().setTournamentAID(tournamentId);
            }else{
                //if the tournament agent is not registered yet - wait
                block(1000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    private Player getPlayer(){
        return (Player) myAgent;
    }

    @Override
    public boolean done() {
        return getPlayer().getTournamentAID() != null;
    }

}
