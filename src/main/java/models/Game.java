package models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @Column(name = "game_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "principal_img", nullable = false)
    private String principalImg;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameImage> images;

    //  Relación con plataformas en la base de datos
    @ManyToMany
    @JoinTable(
        name = "game_platform",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private List<Platform> platformEntities; // ✅ Se mantiene como lista para la base de datos

    @Transient
    private String platforms1;  // ✅ Se usa solo para la API

    //  Relación con géneros en la base de datos
    @ManyToMany
    @JoinTable(
        name = "game_genre",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genreEntities; // Se mantiene como lista para la base de datos

    @Transient
    private String genres1;  //  Se usa solo para la API


    @ManyToMany
    @JoinTable(
        name = "game_developer",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "developer_id")
    )
    private List<Developer> developers;

    @ManyToMany(mappedBy = "games")
    private List<User> users;

    // 🔹 Atributos transitorios para datos obtenidos de la API
    @Transient
    private String platforms; // Ahora String para evitar error en APIUtils.java
    @Transient
    private String genres; // Ahora String para evitar error en APIUtils.java
    @Transient
    private String screenshot1;
    @Transient
    private String screenshot2;
    @Transient
    private String screenshot3;

    // Getters y Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrincipalImg() { return principalImg; }
    public void setPrincipalImg(String principalImg) { this.principalImg = principalImg; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<GameImage> getImages() { return images; }
    public void setImages(List<GameImage> images) { this.images = images; }

    public List<Platform> getPlatformEntities() { return platformEntities; }
    public void setPlatformEntities(List<Platform> platformEntities) { this.platformEntities = platformEntities; }

    public List<Genre> getGenreEntities() { return genreEntities; }
    public void setGenreEntities(List<Genre> genreEntities) { this.genreEntities = genreEntities; }

    public List<Developer> getDevelopers() { return developers; }
    public void setDevelopers(List<Developer> developers) { this.developers = developers; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    // 🔹 Métodos para API (sin afectar base de datos)
    public String getPlatforms() { return platforms1; }
    public void setPlatforms(String platforms) { this.platforms1 = platforms; }

    public String getGenres() { return genres1; }
    public void setGenres(String genres) { this.genres1 = genres; }

    public String getScreenshot1() { return screenshot1; }
    public void setScreenshot1(String screenshot1) { this.screenshot1 = screenshot1; }

    public String getScreenshot2() { return screenshot2; }
    public void setScreenshot2(String screenshot2) { this.screenshot2 = screenshot2; }

    public String getScreenshot3() { return screenshot3; }
    public void setScreenshot3(String screenshot3) { this.screenshot3 = screenshot3; }
}
