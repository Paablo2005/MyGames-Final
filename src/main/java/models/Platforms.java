package models;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Platforms")
public class Platforms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int platformPK;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "platforms")
    private Set<Game> games;
    
    public Platforms() {}

	public int getPlatformPK() {
		return platformPK;
	}

	public void setPlatformPK(int platformPK) {
		this.platformPK = platformPK;
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

	@Override
	public String toString() {
		return "Platforms [platformPK=" + platformPK + ", name=" + name + ", games=" + games + "]";
	}
	
	public static Platforms getPlatformByName(String name, Session session) {
		if (!session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
			session.getTransaction().begin();
		}
		
		try {
		    return session.createQuery("SELECT p FROM Platforms p WHERE p.name = :platformName", Platforms.class)
                    .setParameter("platformName", name)
                    .getSingleResult(); 
		} catch (Exception e) {
			return null;
		}
	}
}
