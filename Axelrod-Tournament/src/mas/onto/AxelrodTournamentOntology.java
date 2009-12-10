package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;

public class AxelrodTournamentOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Axelrod's-Tournament-ontology";
    
    //vocabulary
    public static final String REGISTER = "REGISTER";
    public static final String COOPERATE = "COOPERATE";
    public static final String DEFECT = "DEFECT";
    public static final String NEXT_ROUND = "NEXT_ROUND";
    
    public static final String ACTION_PLAYER = "player";
    
    
    // The singleton instance of this ontology
    private static Ontology theInstance = new AxelrodTournamentOntology(BasicOntology.getInstance());

    public static Ontology getInstance() {
        return theInstance;
    }

    /**
     * Constructor
     */
    private AxelrodTournamentOntology(Ontology base) {
        super(ONTOLOGY_NAME, base);

        try {
            add(new ConceptSchema("REGISTER"), Register.class);
            add(new ConceptSchema("NEXT_ROUND"), NextRound.class);
            add(new ConceptSchema("COOPERATE"), Cooperate.class);
            add(new ConceptSchema("DEFECT"), Defect.class);
            
            ConceptSchema as = (ConceptSchema) getSchema(COOPERATE);
            as.add(ACTION_PLAYER, (ConceptSchema) getSchema(BasicOntology.AID));
            
            as = (ConceptSchema) getSchema(DEFECT);
            as.add(ACTION_PLAYER, (ConceptSchema) getSchema(BasicOntology.AID));
            
            useConceptSlotsAsFunctions();
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
