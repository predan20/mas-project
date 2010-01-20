package mas;

/**
 * String constants used by the application.
 */
public class Constants {
    private Constants(){}
    
    /**
     * Name of the JADE service for the auction.
     */
    public static final String AUCTION_SERVICE_NAME = "Jade-Auction";
    
    /**
	 * Topic name for broadcasting auction information.
	 */
	public static final String AUCTION_TOPIC = "AUCTION";
	
	public static final String CONFIG_AUCTION_TYPE = "auction.type";
	public static final String CONFIG_AUCTION_GOOD_TYPES = "acution.good.type";
	public static final String CONFIG_AUCTION_GOOD_COUNTS = "auction.good.count";
	public static final String CONFIG_AUCTION_GOOD_INITIAL_PRIZE = "auction.good.initial_prize";
	public static final String CONFIG_AUCTION_GOOD_RESERVATION_PRIZE = "auction.good.reservation_prize";
	
	
	public static final String CONFIG_BIDDER_PREFIX = "bidder.";
	public static final String CONFIG_BIDDER_BUDGET = "budget";
	public static final String CONFIG_BIDDER_STRATEGY = "strategy";
	
}
