package com.michelangelo.usermicroservice.VO;

import java.time.LocalDate;
import java.util.List;

public class MediaVO {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private List<GenreVO> genres;

    public MediaVO() {
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
}
