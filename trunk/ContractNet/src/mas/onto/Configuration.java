package mas.onto;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Configuration implements Concept {
    private Processor processor;
    private Motherboard motherBoard;
    private GraphicsCard graphicsCard;
    private int requiredNumber;
    
    public Processor getProcessor() {
        return processor;
    }
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
    public Motherboard getMotherBoard() {
        return motherBoard;
    }
    public void setMotherBoard(Motherboard motherBoard) {
        this.motherBoard = motherBoard;
    }
    public GraphicsCard getGraphicsCard() {
        return graphicsCard;
    }
    public void setGraphicsCard(GraphicsCard graphicsCard) {
        this.graphicsCard = graphicsCard;
    }
    public int getRequiredNumber() {
        return requiredNumber;
    }
    public void setRequiredNumber(int requiredNumber) {
        this.requiredNumber = requiredNumber;
    }
    
    public List<Component> getComponents(){
        List<Component> res = new ArrayList<Component>();
        res.add(processor);
        res.add(graphicsCard);
        res.add(motherBoard);
        return res;
    }
    
    public boolean isProposalComlete(List<Component> proposal){
        int componentCount = 0;
        for(Component comp : proposal){
            componentCount += comp.getCount();
        }
        
        int expectedComponentCount = this.getRequiredNumber() * 3; //3 is the number of components in one computer configuration
        
        return componentCount == expectedComponentCount;
    }
    
    
    
}
