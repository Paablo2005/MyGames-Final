package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imagePK;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Game game;

    @Column(nullable = false, length = 255)
    private String imageURL;
    
    public Images() {}

	public int getImagePK() {
		return imagePK;
	}

	public void setImagePK(int imagePK) {
		this.imagePK = imagePK;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

    
}
