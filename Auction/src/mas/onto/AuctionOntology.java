package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;

/**
 * 
 * JADE ontology used for message exchange by the auction agents.
 * Defines concepts representing actions.
 *
 */
public class AuctionOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Axelrod's-Tournament-ontology";
    
    //vocabulary
    public static final String REGISTER = "REGISTER";
    public static final String INITIAL_PRIZE = "INITIAL_PRIZE";
    public static final String BID = "BID";
    
    // The singleton instance of this ontology
    private static Ontology theInstance = new AuctionOntology(BasicOntology.getInstance());

    public static Ontology getInstance() {
        return theInstance;
    }

    /**
     * Constructor
     */
    private AuctionOntology(Ontology base) {
        super(ONTOLOGY_NAME, base);

        try {
            add(new ConceptSchema(REGISTER), Register.class);
            add(new ConceptSchema(INITIAL_PRIZE), InitialPrize.class);
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
