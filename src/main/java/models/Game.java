package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "releaseDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    
    @Column(name = "image", nullable = false, length = 100)
    private String image;
    
    @Column(name = "apiId", nullable = false)
    private int apiId;
    
    @ManyToMany
    @JoinTable(
        name = "Game_Genres",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "genrePK")
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
        name = "Game_Platforms",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "platformPK")
    )
    private Set<Platforms> platforms;

    @ManyToMany
    @JoinTable(
        name = "Game_Developers",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "developerPK")
    )
    private Set<Developer> developers;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Images> screenshots;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Collection> collections = new HashSet<>();

    public Game() {
    	this.genres = new HashSet<>();
    	this.platforms = new HashSet<>();
    	this.developers = new HashSet<>();
    	this.screenshots = new HashSet<>();
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrincipalImg() {
		return image;
	}

	public void setPrincipalImg(String image) {
		this.image = image;
	}

	public int getApiId() {
		return apiId;
	}

	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public Set<Platforms> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Set<Platforms> platforms) {
		this.platforms = platforms;
	}

	public Set<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<Developer> developers) {
		this.developers = developers;
	}

	public Set<Images> getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(Set<Images> screenshots) {
		this.screenshots = screenshots;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + ", releaseDate=" + releaseDate + ", image="
				+ image + ", apiId=" + apiId + ", genres=" + genres + ", platforms="
				+ platforms + ", developers=" + developers + ", screenshots=" + screenshots + "]";
	}

}
