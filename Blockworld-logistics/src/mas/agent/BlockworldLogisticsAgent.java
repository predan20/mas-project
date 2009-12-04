package mas.agent;

import java.awt.Point;

import blockworld.Env;
import jade.core.Agent;

/**
 * Base class for Blockworld logistic agents like explorer and carrier. Provides
 * convenient methods for navigating in the Blockworld and access to the
 * Blockworld agent instance - {@link blockworld.Agent}.
 * 
 */
public abstract class BlockworldLogisticsAgent extends Agent {
	private blockworld.Agent agent;
	
	public blockworld.Agent getBlockworldAgent(){
		return agent;
	}
	
	public void setBlockworldAgent(blockworld.Agent agent){
		this.agent = agent;
	}
	
	/**
	 * Navigate the agent to the given location.
	 * @param location Location in the blockworld environment.
	 */
	public void goTo(Point location){
		Env env = Env.getEnv();
	
		
		Point currentPosition = getBlockworldAgent().getPosition();
		
		while(!location.equals(currentPosition)){
			if(currentPosition.x > location.x){
				env.west(getBlockworldAgent().getName());
			}
			if(currentPosition.x < location.x){
				env.east(getBlockworldAgent().getName());
			}
			
			if(currentPosition.y > location.y){
				env.north(getBlockworldAgent().getName());
			}
			if(currentPosition.y < location.y){
				env.south(getBlockworldAgent().getName());
			}
			
			currentPosition = getBlockworldAgent().getPosition();
			//sleep do that the UI repaints itself
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
