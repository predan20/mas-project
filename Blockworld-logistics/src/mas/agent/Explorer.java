package mas.agent;

import mas.behaviour.ExplorerBehaviour;
import blockworld.Env;

/**
 * Explorer logistic agent. Has only one behavior - {@link ExplorerBehaviour}.
 * 
 */
public class Explorer extends BlockworldLogisticsAgent {
	@Override
	protected void setup() {
		Env env = Env.getEnv();
		env.addAgent(getLocalName());
		env.enter(getLocalName(), 0.0, 0.0, "green");

		blockworld.Agent me = env.getAgent(getLocalName());
		setBlockworldAgent(me);

		addBehaviour(new ExplorerBehaviour(this));
	}
}
