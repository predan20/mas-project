package mas.onto;

import jade.content.Concept;

import java.util.Set;

/**
 * Concept representing the state of an auction.
 * 
 */
public class AuctionDescription implements Concept {

    /**
     * Enumeration of possible auction types.
     */
    public static enum AuctionType {
        ENGLISH, DUTCH, JAPANESE;
    }

    private AuctionType auctionType;
    
    private Set<Good> goods = null;

    public AuctionDescription(){}
    
    public AuctionDescription(AuctionType auctionType, Set<Good> goods) {
        this.auctionType = auctionType;
        this.goods = goods;
    }
    
    public AuctionType getAuctionType() {
        return auctionType;
    }

    public Set<Good> getGoods() {
        return goods;
    }

    public void setGoods(Set<Good> goods) {
        this.goods = goods;
    }
}
