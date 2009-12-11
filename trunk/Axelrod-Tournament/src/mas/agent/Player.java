package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mas.agent.strategy.AlwaysDefectStrategy;
import mas.agent.strategy.Strategy;
import mas.behaviour.player.GetTournamentId;
import mas.behaviour.player.HandleNextRoundRequest;
import mas.behaviour.player.SendRegisterRequest;
import mas.onto.AxelrodTournamentOntology;
import mas.onto.PlayerAction;

public class Player extends Agent {
    
    private AID tournamentId = null;
    private List<PlayerAction> oponentHistory = new ArrayList<PlayerAction>();
    private List<PlayerAction> ownHistory = new ArrayList<PlayerAction>();
    private Strategy currentStrategy;
    
    public static final Map<String, Class<? extends Strategy>> strategies = new HashMap<String, Class<? extends Strategy>>();
    static{
        strategies.put("ALLD", AlwaysDefectStrategy.class);
    }

    @Override
    protected void setup() {
        //init strategy
        initStrategy();
        
        
        getContentManager().registerOntology(AxelrodTournamentOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        SequentialBehaviour playerBehaviour = new SequentialBehaviour();
        playerBehaviour.addSubBehaviour(new GetTournamentId(this));
        playerBehaviour.addSubBehaviour(new SendRegisterRequest(this));
        playerBehaviour.addSubBehaviour(new HandleNextRoundRequest(this));
        
        addBehaviour(playerBehaviour);
    }
    
    private void initStrategy() {
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            String strategyName = (String)args[0];
            Class<? extends Strategy> strategyClass = strategies.get(strategyName);
            
            try {
                Constructor<? extends Strategy> constr = strategyClass.getConstructor(Player.class);
                this.currentStrategy = constr.newInstance(this);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else{
            this.currentStrategy = new AlwaysDefectStrategy(this);
        }
        
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

    public Strategy getCurrentStrategy() {
        return currentStrategy;
    }

    public void setCurrentStrategy(Strategy currentStrategy) {
        this.currentStrategy = currentStrategy;
    }

    public List<PlayerAction> getOwnHistory() {
        return ownHistory;
    }

}
