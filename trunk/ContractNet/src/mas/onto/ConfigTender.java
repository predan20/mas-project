package mas.onto;

import java.util.List;

import jade.content.Concept;

public class ConfigTender implements Concept {
    private Configuration config;
    private List<Component> components;
    
    public ConfigTender(){}
    
    public ConfigTender(Configuration config, List<Component> components) {
        this.config = config;
        this.components = components;
    }
    
    public Configuration getConfig() {
        return config;
    }
    public void setConfig(Configuration config) {
        this.config = config;
    }
    public List<Component> getComponents() {
        return components;
    }
    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
