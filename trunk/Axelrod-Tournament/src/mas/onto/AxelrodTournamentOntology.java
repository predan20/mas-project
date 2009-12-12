package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;

/**
 * 
 * JADE ontology used for message exchange by the Axelrod's Tournament agents.
 * Defines concepts representing actions.
 *
 */
public class AxelrodTournamentOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Axelrod's-Tournament-ontology";
    
    //vocabulary
    public static final String REGISTER = "REGISTER";
    public static final String NEXT_ROUND = "NEXT_ROUND";
    public static final String PLAYER_ACTION = "PLAYER_ACTION";
    public static final String COOPERATE = "COOPERATE";
    public static final String DEFECT = "DEFECT";
    
    
    public static final String ACTION_PLAYER = "player";
    public static final String NEXT_ROUND_OponentLastAction = "oponentLastAction";
    
    
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
            add(new ConceptSchema(REGISTER), Register.class);
            add(new ConceptSchema(NEXT_ROUND), NextRound.class);
            add(new ConceptSchema(PLAYER_ACTION), PlayerAction.class);
            add(new ConceptSchema(COOPERATE), Cooperate.class);
            add(new ConceptSchema(DEFECT), Defect.class);
            
            ConceptSchema as = (ConceptSchema) getSchema(NEXT_ROUND);
            as.add(NEXT_ROUND_OponentLastAction, (ConceptSchema) getSchema(PLAYER_ACTION), ConceptSchema.OPTIONAL);
            
            as = (ConceptSchema) getSchema(PLAYER_ACTION);
            as.add(ACTION_PLAYER, (ConceptSchema) getSchema(BasicOntology.AID));
            
            as = (ConceptSchema) getSchema(COOPERATE);
            as.addSuperSchema((ConceptSchema)getSchema(PLAYER_ACTION));
            
            as = (ConceptSchema) getSchema(DEFECT);
            as.addSuperSchema((ConceptSchema)getSchema(PLAYER_ACTION));
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
