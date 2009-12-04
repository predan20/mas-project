package mas.agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import mas.behaviour.CarrierBehaviour;
import blockworld.Env;
import blockworld.TypeObject;
import blockworld.lib.ObsVectListener;

/**
 * Carrier logistic agent. Keeps track of available traps to use for dropping bombs.
 * Has only one behavior - {@link CarrierBehaviour}. 
 *
 */
public class Carrier extends BlockworldLogisticsAgent {
	private List<Point> traps = new ArrayList<Point>();

	
	@Override
	protected void setup() {
		Env env = Env.getEnv();
		env.addAgent(getLocalName());		
		env.enter(getLocalName(), 1.0, 1.0, "red");
		
		env.addTrapsListener(new ObsVectListener(){
			public void onAdd(int index, Object element) {
				TypeObject o = (TypeObject)element;
				Carrier.this.traps.add(o.getPosition());
			}

			public void onRemove(int index, Object element) {
				TypeObject o = (TypeObject)element;
				Carrier.this.traps.remove(o.getPosition());
			}
		});		
		
		blockworld.Agent me = env.getAgent(getLocalName());
		setBlockworldAgent(me);
		
		addBehaviour(new CarrierBehaviour(this));
	}


	public List<Point> getTraps() {
		return traps;
	}
}
