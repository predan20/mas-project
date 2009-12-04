README for the Blockworld-Logistics assignment:

The archive contains:
1. run.bat executable - starts the multi-agent scenario.
2. run-single-agent.bat - starts the single agent scenario.
2. Elipse project with the source files that can be imported by creating a new project and selecting the "Create project from existing source"
3. lib folder with all needed binaries.

The solution:

NOTE: For communication between the agents we use the TopicManagement support of JADE (started as additional service). 
        So that the Explorer agent can send messages to a topic instead to a list of agents.
NOTE: In all cases the user is expected to place traps and bombs using the GUI after running the application. 
        There are no traps or bombs set by default.

A. Single Agent:
    - Implemented by the mas.agent.Sapper class
    - It has single behaviour - SapperBehaviour which is a sublass of TickerBehaviour. 
        It takes care of sensing the bombs and taking them to the traps.

B. Two Agents:
    - The Explorer is implemented by the mas.agent.Explorer class. It has a single behaviour - mas.behaviour.ExplorerBehaviour.
    - ExplorerBehaviour is a TickerBehaviour and takes care of sensing the bombs and notifying the carriers by sending messages to the BOMB topic.
        It also takes care of the walking in case there are no sensed bombs. The walking is based on iterative DFS algorythm.
    - The Carrier is implemented by the mas.agent.Carrier class. It has a single behaviour - mas.behaviour.CarrierBehaviour. 
        The carrier agent also takes care of keeping track of the trap positions by registering listener to the Env class.
    - CarrierBehaviour is a CyclicBehaviour for receiving messages with bomb locations, taking the bomb and droping it in the closest trap.
        It uses blockingReceive() to read the message as it is the only behaviour of the agent.

C. Multi-agents:
    - Due to the fact that the Explorer agent sends message to a topic and not to a list of agents, we can add carriers by simply adding them to JADE.
    - This is the scenario started by run.bat.