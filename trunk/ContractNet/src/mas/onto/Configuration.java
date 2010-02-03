package mas.onto;

import jade.content.Concept;

public class Configuration implements Concept {
    private Processor processor;
    private Motherboard motherBoard;
    private GraphicsCard graphicsCard;
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
    
    
}
