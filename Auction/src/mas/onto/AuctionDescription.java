package mas.onto;

import jade.content.Concept;

import java.util.Set;

/**
 * Concept representing the state of an auction.
 * 
 */
public class AuctionDescription implements Concept {
    /**
     * Possible auction types.
     */
    public static final String ENGLISH_AUCTION = "english";
    public static final String DUTCH_AUCTION = "dutch";
    public static final String JAPANESE_AUCTION = "japanese";

    private String auctionType;

	private int minStep;
    
    private Set<Good> goods = null;

    public AuctionDescription(){}
    
    public AuctionDescription(String auctionType, Set<Good> goods) {
        this.auctionType = auctionType;
        this.goods = goods;
    }
    
    public AuctionDescription(String auctionType, int minStep, Set<Good> goods) {
		this.auctionType = auctionType;
		this.minStep = minStep;
		this.goods = goods;
	}
    
    public String getAuctionType() {
        return auctionType;
    }
    
    public void setAuctionType(String type){
        this.auctionType = type;
    }

    public Set<Good> getGoods() {
        return goods;
    }

    public void setGoods(Set<Good> goods) {
        this.goods = goods;
    }

	public int getMinStep() {
		return minStep;
	}

	public void setMinStep(int minStep) {
		this.minStep = minStep;
	}
}
