package mas.onto;

import jade.content.Predicate;

public class Tender implements Predicate {
    public Tender(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration configuration;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
