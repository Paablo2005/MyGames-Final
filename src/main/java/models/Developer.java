package models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Developers")
public class Developer {

    @Id
    private int developerPK;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "developers")
    private Set<Game> games;

    public Developer() {}
    
	public int getDeveloperPK() {
		return developerPK;
	}

	public void setDeveloperPK(int developerPK) {
		this.developerPK = developerPK;
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
