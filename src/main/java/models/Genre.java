package models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genrePK;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Game> games;

    public Genre() {}
    
    public Genre(int genrePK, String name) {
    	this.genrePK = genrePK;
    	this.name = name;
    }
    
	public int getGenrePK() {
		return genrePK;
	}

	public void setGenrePK(int genrePK) {
		this.genrePK = genrePK;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Game> getGames() {
		return games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}
}