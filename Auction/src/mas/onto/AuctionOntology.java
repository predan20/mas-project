package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;

/**
 * 
 * JADE ontology used for message exchange by the auction agents.
 * Defines concepts representing actions.
 *
 */
public class AuctionOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Axelrod's-Tournament-ontology";
    
    //vocabulary classes
    public static final String REGISTER = "REGISTER";
    public static final String INITIAL_PRIZE = "INITIAL_PRIZE";
    public static final String BID = "BID";
    public static final String AUCTION_DESCRIPTION = "AUCTION_DESCRIPTION";
    public static final String GOOD = "GOOD";
    
    //vocabulary prop names
    public static final String REGISTER_PROP_AUCTION_DESC = "auctionDesciption";
    public static final String AUCTION_DESC_PROP_GOODS = "goods";
    
    
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
            add(new ConceptSchema(INITIAL_PRIZE), Prize.class);
            add(new ConceptSchema(BID), Bid.class);
            add(new ConceptSchema(AUCTION_DESCRIPTION), AuctionDescription.class);
            add(new ConceptSchema(GOOD), Good.class);
            
            ConceptSchema as = (ConceptSchema) getSchema(REGISTER);
            as.add(REGISTER_PROP_AUCTION_DESC, (ConceptSchema) getSchema(AUCTION_DESCRIPTION), ConceptSchema.MANDATORY);
            
            as = (ConceptSchema) getSchema(AUCTION_DESCRIPTION);
            as.add(AUCTION_DESC_PROP_GOODS, (ConceptSchema) getSchema(GOOD), 1, ObjectSchema.UNLIMITED);
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
