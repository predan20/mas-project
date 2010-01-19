package mas.onto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jade.content.Concept;

/**
 * Concept representing the auction to be held.
 * 
 */
public class AuctionDescription implements Concept {

    public static enum AuctionType {
        ENGLISH, DUTCH, JAPANESE;
    }

    private AuctionType auctionType;

    public AuctionDescription(AuctionType auctionType, Map<Class<? extends Good>, Integer> goods) {
        this.auctionType = auctionType;
        this.goods = goods;
    }

    private Map<Class<? extends Good>, Integer> goods = new HashMap<Class<? extends Good>, Integer>();

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public Set<Class<? extends Good>> getGoodTypes() {
        return goods.keySet();
    }

    public int getNumberOfGoods(Class<? extends Good> goodType) {
        return goods.get(goodType);
    }

}
