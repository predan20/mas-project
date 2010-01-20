package mas.onto;

import jade.content.Concept;

/**
 * Concept representing the REGISTER action send as a request by the auctioneer.<br>
 * Contains description of the auction to be held.
 * 
 */
public class Register implements Concept {
	private AuctionDescription auctionDesciption;

	public Register(){}
	
	public Register(AuctionDescription auctionDesciption) {
		this.auctionDesciption = auctionDesciption;
	}

	public AuctionDescription getAuctionDesciption() {
		return auctionDesciption;
	}

    public void setAuctionDesciption(AuctionDescription auctionDesciption) {
        this.auctionDesciption = auctionDesciption;
    }
}
