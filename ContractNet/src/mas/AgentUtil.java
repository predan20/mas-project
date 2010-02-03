package mas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import mas.onto.Component;
import mas.onto.Configuration;
import mas.onto.GraphicsCard;
import mas.onto.Motherboard;
import mas.onto.Processor;
import mas.onto.Task;

public class AgentUtil {
    
    private static final String CONFIG_FILE = "config.properties";
    
    /**
     * Constant strings forming the configuration keys.
     */
    private static final String CONFIG_TASK = "task";
    private static final String CONFIG_PROCESSOR = "processor";
    private static final String CONFIG_MOTHERBOARD = "motherboard";
    private static final String CONFIG_GRAPHICS = "graphics";
    
    private static final String CONFIG_PRICE = "price";
    private static final String CONFIG_QUALITY = "quality";
    private static final String CONFIG_MANAFACT = "manufacturer";
    
    
    
    public static Task readManagerTask(){
        //load the properties file
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(CONFIG_FILE));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        //the task configurations mapped by ID (as found in the config file)
        Map<String, Configuration> configurations = new HashMap<String, Configuration>();
        
        String taskPrefix = CONFIG_TASK + ".";
        
        //iterate over the keys and read the corresponding values
        for (Object key : props.keySet()){
            String k = (String)key;
            
            if(k.startsWith(taskPrefix)){
               String rest = k.substring(taskPrefix.length());
               
               //
               String configId = rest.substring(0, rest.indexOf("."));
               Configuration conf = configurations.get(configId);
               if(conf == null){
                   conf = new Configuration();
                   configurations.put(configId, conf);
               }
               
               
               //
               rest = rest.substring(rest.indexOf(".") + 1);
               
               Component component = null;
               int takeOffLength = 0;
               if(rest.startsWith(CONFIG_PROCESSOR)){
                   takeOffLength = CONFIG_PROCESSOR.length();
                   
                   if(conf.getProcessor() == null){
                       conf.setProcessor(new Processor());
                   }
                   component = conf.getProcessor();
                   
                   
               }else if(rest.startsWith(CONFIG_MOTHERBOARD)){
                   takeOffLength = CONFIG_GRAPHICS.length();
                   
                   if(conf.getMotherBoard() == null){
                       conf.setMotherBoard(new Motherboard());
                   }
                   component = conf.getMotherBoard();
                   
                   
               }else if(rest.startsWith(CONFIG_GRAPHICS)){
                   takeOffLength = CONFIG_MOTHERBOARD.length();
                   
                   if(conf.getGraphicsCard() == null){
                       conf.setGraphicsCard(new GraphicsCard());
                   }
                   component = conf.getGraphicsCard();
               }
               
               
               //
               rest = rest.substring(takeOffLength + 1);
               
               String value = props.getProperty(k);
               
               if(rest.startsWith(CONFIG_PRICE)){
                   component.setPrice(Long.parseLong(value));
               }else if(rest.startsWith(CONFIG_QUALITY)){
                   component.setQuality(value);
               }else if(rest.startsWith(CONFIG_MANAFACT)){
                   component.setManufacturer(value);
               }
            }
        }
        
        return new Task(new ArrayList<Configuration>(configurations.values()));
    }
}
