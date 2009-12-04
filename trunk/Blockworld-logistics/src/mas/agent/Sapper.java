package mas.agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import mas.behaviour.SapperBehaviour;
import blockworld.Env;
import blockworld.TypeObject;
import blockworld.lib.ObsVectListener;
import jade.core.Agent;

public class Sapper extends Agent {
	public static final String AGENT_NAME = "Sapper";
	
	private List<Point> traps = new ArrayList<Point>();

	
	@Override
	protected void setup() {
		Env env = Env.getEnv();
		env.addAgent(AGENT_NAME);		
		env.enter(AGENT_NAME, 0.0, 0.0, "green");
		
		env.addTrapsListener(new ObsVectListener(){
			public void onAdd(int index, Object element) {
				TypeObject o = (TypeObject)element;
				Sapper.this.traps.add(o.getPosition());
			}

			public void onRemove(int index, Object element) {
				TypeObject o = (TypeObject)element;
				Sapper.this.traps.remove(o.getPosition());
			}
		});		
		
		blockworld.Agent me = env.getAgent(AGENT_NAME);
		
		addBehaviour(new SapperBehaviour(this, me));
	}


	public List<Point> getTraps() {
		return traps;
	}
}
