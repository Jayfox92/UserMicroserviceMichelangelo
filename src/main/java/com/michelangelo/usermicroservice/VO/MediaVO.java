package com.michelangelo.usermicroservice.VO;

import java.time.LocalDate;
import java.util.List;

public class MediaVO {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private List<GenreVO> genres;
    private List<ArtistVO> artists;
    private TypeOfMediaVO typeOfMedia;

    public MediaVO() {
    }

    public MediaVO(Long id, String title, LocalDate releaseDate, List<GenreVO> genres, List<ArtistVO> artists) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.artists = artists;
    }
    public MediaVO(Long id, String title, LocalDate releaseDate, List<GenreVO> genres) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }
    public MediaVO(Long id, String title){
        this.id = id;
        this.title = title;
    }


    public List<GenreVO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreVO> genres) {
        this.genres = genres;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<ArtistVO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistVO> artists) {
        this.artists = artists;
    }
}
