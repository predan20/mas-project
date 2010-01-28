package mas.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * 
 * JADE ontology used for message exchange by the agents.
 *
 */
public class ComputerConfigurationOntology extends BeanOntology {
    public static final String ONTOLOGY_NAME = "PC-Configuration-ontology";
    
    // The singleton instance of this ontology
    private static Ontology theInstance = new ComputerConfigurationOntology(ONTOLOGY_NAME, BasicOntology.getInstance());

    public static Ontology getInstance() {
        return theInstance;
    }

    /**
     * Constructor
     */
    private ComputerConfigurationOntology(String name, Ontology base) {
        super(name, base);

        try {
            add("mas.onto");
        } catch (OntologyException oe) {
            throw new RuntimeException(oe);
        }
    }

}
