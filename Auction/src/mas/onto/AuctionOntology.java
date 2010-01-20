package mas.onto;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * 
 * JADE ontology used for message exchange by the auction agents.
 * Defines concepts representing actions.
 *
 */
public class AuctionOntology extends BeanOntology {
    public static final String ONTOLOGY_NAME = "Auction-ontology";
    
    // The singleton instance of this ontology
    private static Ontology theInstance = new AuctionOntology(ONTOLOGY_NAME);

    public static Ontology getInstance() {
        return theInstance;
    }

    /**
     * Constructor
     */
    private AuctionOntology(String name) {
        super(name);

        try {
            add("mas.onto");
//            add(Prize.class);
//            add(Bid.class);
//            add(AuctionDescription.class);
//            add(Good.class);
//            add(Book.class);
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
