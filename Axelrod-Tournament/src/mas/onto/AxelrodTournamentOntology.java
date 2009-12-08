package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;

public class AxelrodTournamentOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Axelrod's-Tournament-ontology";
    
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
            add(new AgentActionSchema("REGISTER"), Register.class);
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
