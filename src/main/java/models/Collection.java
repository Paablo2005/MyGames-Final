package models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "collections")
public class Collection {

    @EmbeddedId
    private CollectionId id;
    
    @ManyToOne
    @JoinColumn(name = "gameId", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(length = 250)
    private String review;

    @Column(name = "stars", nullable = false)
    private int stars;
    
    @Column(name = "finishDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Column(name = "played", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean played;

    public Collection() {
		this.id = new CollectionId(1, 1);
		this.review = "";
		this.stars = 0;
		this.played = false;
		this.finishDate = new Date();
    }
    
	public Collection(int userId, int gameId, String review, Byte score, Boolean played) {
		this.id = new CollectionId(userId, gameId);
		this.review = review;
		this.stars = score;
		this.played = played;
		this.finishDate = new Date();
	}
	
	public Collection(int userId, int gameId) {
		this.id = new CollectionId(userId, gameId);
		this.played = false;
	}

	public CollectionId getId() {
		return id;
	}

	public void setId(CollectionId id) {
		this.id = id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getScore() {
		return stars;
	}

	public void setScore(int score) {
		this.stars = score;
	}

	public Boolean getPlayed() {
		return played;
	}

	public void setPlayed(Boolean played) {
		this.played = played;
	}

	@Override
	public String toString() {
		return "Collection: \n\tid = " + id + "\n\treview = " + review + "\n\tscore = " + stars
				+ "\n\tplayed = " + played + "\n";
	}
}
