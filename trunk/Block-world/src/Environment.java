import jade.core.Agent;
import java.util.Iterator;
import java.lang.Thread;
import java.awt.Point;
import java.util.LinkedList;
import blockworld.*;

public class Environment extends Agent
{
	protected void setup() 
	{
		Env e = Env.getEnv();
	}
}