package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mas.Constants;
import mas.behaviour.tournament.HandleRegisterRequest;
import mas.behaviour.tournament.InitBlockwolrd;
import mas.behaviour.tournament.SendNextRoundRequest;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.Cooperate;
import mas.onto.Defect;
import mas.onto.PlayerAction;
import blockworld.Env;

/**
 * JADE agent representing the Axelrod's Tournament.
 * It has single sequential behavior representing its lifecycle.
 * <ol>
 *  <li> Wait and handle REGISTER requests from players.
 *  <li> Initialize the Blockworld UI.
 *  <li> Send NEXT_ROUND requests.
 * </ol>
 * 
 * As an init param this agent accepts the number of rounds to be played.
 */
public class AxelrodTournament extends Agent {
    /**
     * Number of rounds to be played.
     * Defaults to 200.
     */
    private int numberOfRounds = 6;
    
    /**
     * Player to player history map.
     */
    private Map<AID, List<PlayerAction>> playerHistories = new HashMap<AID, List<PlayerAction>>();

    @Override
    protected void setup() {
        // read argument for number of rounds if provided
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            numberOfRounds = Integer.parseInt((String)args[0]);
        }
        
        //add the tournament service to the DF
        registerService();
        
        //register the tournament ontology for behaviors which receive messages and use it
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());

        //add one sequential behavior
        SequentialBehaviour tournamentLifecycle = new SequentialBehaviour(this);
        tournamentLifecycle.addSubBehaviour(new HandleRegisterRequest(this));
        tournamentLifecycle.addSubBehaviour(new InitBlockwolrd(this));
        
        //add behaviors for the tournament rounds
        for(int round = 1; round <= getNumberOfRounds(); round ++){
            tournamentLifecycle.addSubBehaviour(new SendNextRoundRequest(this));
        }
        
        addBehaviour(tournamentLifecycle);
    }
    
    /**
     * Adds the given player to the list of participants.
     * @param player
     */
    public void addPlayer(AID player){
        if(!canAddPlayer()){
            throw new RuntimeException("Only 2 players allowed!");
        }
        playerHistories.put(player, new ArrayList<PlayerAction>());
    }
    
    /**
     * Checks if the number of required players was already riched.
     * @return boolean
     */
    public boolean canAddPlayer(){
        return playerHistories.size() < 2;
    }
    
    /**
     * Retrieves the given player's list of previous actions.
     * @param player
     * @return history
     */
    public List<PlayerAction> getPlayerHistory(AID player){
        return playerHistories.get(player);
    }
    
    /**
     * Adds the give action to the history of the given player.
     * @param player
     * @param action
     */
    public void addPlayerAction(AID player, PlayerAction action){
        List<PlayerAction> history = playerHistories.get(player);
        if(history == null){
            throw new RuntimeException("Unknown player: " + player);
        }
        
        history.add(action);
    }
    
    /**
     * Get the participants.
     * @return list of players.
     */
    public Set<AID> getPlayers(){
        return playerHistories.keySet();
    }

    /**
     * Register this agent in JADE's directory using the DFService.
     */
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

    /**
     * Handles new player action. This means adding it to the history and
     * updating the Blockworld UI.
     * 
     * @param action
     */
    public void handlePlayerAction(PlayerAction action) {
        //add to history
        playerHistories.get(action.getPlayer()).add(action);
        
        Env env = Env.getEnv();
        
        //handle cooperate or defect by moving or not a bomb
        if(action instanceof Cooperate){
            Point pos = env.getAgent(action.getPlayer().getLocalName()).getPosition();
            
            Point onTheLeft = new Point(pos.x - 1, pos.y);
            if(env.isBomb(onTheLeft) != null){
                moveBombFromTheLeft(action.getPlayer().getLocalName());
            }else{
                moveBombFromTheRight(action.getPlayer().getLocalName());
            }
        }else if(action instanceof Defect){
            //do not move the bomb
        }
        
        //move the agent south
        env.south(action.getPlayer().getLocalName());
    }

    private void moveBombFromTheLeft(String agentName) {
        Env env = Env.getEnv();
        
        env.west(agentName);
        env.pickup(agentName);
        
        env.east(agentName);
        env.east(agentName);
        
        env.drop(agentName);
        env.west(agentName);
    }
    
    private void moveBombFromTheRight(String agentName) {
        Env env = Env.getEnv();
        
        env.east(agentName);
        env.pickup(agentName);
        
        env.west(agentName);
        env.west(agentName);
        
        env.drop(agentName);
        env.east(agentName);
    }

    /**
     * Retrieves the number of rounds that are going to be played.
     * @return int
     */
    public int getNumberOfRounds() {
        return numberOfRounds;
    }
    
    

}
