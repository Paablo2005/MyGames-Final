package models;

import java.util.ArrayList;
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
    @Column(name = "gameId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "releaseDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    
    @Column(name = "apiId", nullable = false)
    private int apiId;
    
    @Column(name = "image", nullable = true, length = 255)
    private String image;
    
    @Column(name = "image1", nullable = true, length = 255)
    private String image1;

    @Column(name = "image2", nullable = true, length = 255)
    private String image2;
    
    @Column(name = "image3", nullable = true, length = 255)
    private String image3;
    
    @ManyToMany
    @JoinTable(
        name = "Game_Genres",
        joinColumns = @JoinColumn(name = "gameId"),
        inverseJoinColumns = @JoinColumn(name = "genrePK")
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
        name = "Game_Platforms",
        joinColumns = @JoinColumn(name = "gameId"),
        inverseJoinColumns = @JoinColumn(name = "platformPK")
    )
    private Set<Platforms> platforms;

    @Column(name = "developers", nullable = false)
    private String developers;

    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Collection> collections = new HashSet<>();

	public Game() {
		this.name = "";
		this.releaseDate = null;
		this.description = "";
		this.apiId = 0;
		this.image = "";
		this.image1 = "";
		this.image2 = "";
		this.image3 = "";
	  	this.developers = ""; 
    	this.genres = new HashSet<>();
    	this.platforms = new HashSet<>();
	}

	public int getId() {
		return gameId;
	}

	public void setId(int id) {
		this.gameId = id;
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

	public String getDevelopers() {
		return developers;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}
	
	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public ArrayList<String> platformsToString() {
		ArrayList<String> result = new ArrayList<String>();
		for (Platforms pl : platforms)
			result.add(pl.getName());
		return result;
	}
	
	public ArrayList<String> genresToString() {
		ArrayList<String> result = new ArrayList<String>();
		for (Genre gen : genres)
			result.add(gen.getName());
		return result;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", name=" + name + ", releaseDate=" + releaseDate + ", description="
				+ description + ", image=" + image + ", apiId=" + apiId + ", image1=" + image1 + ", image2=" + image2
				+ ", image3=" + image3 + ", genres=" + genres + ", platforms=" + platforms + ", developers="
				+ developers + ", collections=" + collections + "]";
	}
	
	
}
