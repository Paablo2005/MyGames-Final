package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.ApiUtils.Rating;

public class GameApi {
    private int id;
    private String name;
    private Date date;
    private String image;
    private int playtime;
    private int metacritic;
    private String genres;
    private String platforms;
    private Rating rating;
    private boolean byUser;

	public GameApi(String name, String image) {
		this.name = name;
		this.image = image;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPlaytime() {
		return playtime;
	}

	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}

	public int getMetacritic() {
		return metacritic;
	}

	public void setMetacritic(int metacritic) {
		this.metacritic = metacritic;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getPlatforms() {
		return platforms;
	}

	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public boolean isByUser() {
		return byUser;
	}

	public void setByUser(boolean byUser) {
		this.byUser = byUser;
	}

	@Override
	public String toString() {
		return "Game:\n\tid = " + id + "\n\tname = " + name + "\n\tdate = " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\timage = " + image
				+ "\n\tplaytime = " + playtime + "\n\tmetacritic = " + metacritic + "\n\tgenres = " + genres
				+ "\n\tplatforms = " + platforms + "\n\trating = " + rating + "\n\tbyUser = " + byUser;
	}

	
	
}

