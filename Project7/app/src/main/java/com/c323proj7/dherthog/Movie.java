package com.c323proj7.dherthog;

/**
 * Stores all information pertaining to a single Movie.
 */
public class Movie {

    private String title;
    private String language;
    private String genre;
    private String releaseDate;
    private String description;
    private String imagePath;

    /**
     * Create a Movie.
     * @param title The title of the movie (any String)
     * @param language The original language of the movie (any String)
     * @param genre The genre of the movie (any String)
     * @param releaseDate The release date of the movie (any String)
     * @param description A description of the movie (any String)
     * @param imagePath The path to the poster of the movie on TMDB (any String)
     */
    public Movie(String title, String language, String genre, String releaseDate, String description, String imagePath) {
        this.title = title;
        this.language = language;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.description = description;
        this.imagePath = imagePath;
    }

    /**
     * @return The title of the movie (any String)
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The original language of the movie (any String)
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return The genre of the movie (any String)
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @return The release date of the movie (any String)
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return A description of the movie (any String)
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The path to the poster of the movie on TMDB (any String)
     */
    public String getImagePath() {
        return imagePath;
    }
}
