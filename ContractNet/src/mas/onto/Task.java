package mas.onto;

import java.util.List;

import jade.content.Predicate;

public class Task implements Predicate {
    
    public Task(){}
    public Task(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    private List<Configuration> configurations;

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

}
