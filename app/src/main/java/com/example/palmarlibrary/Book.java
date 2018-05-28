package com.example.palmarlibrary;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Book {
    private String indexId;
    private String bookName;
    private String author;
    private String publisher;
    private String isbn;
    private Double price;
    private String shape;
    private String series;
    private String location;
    private String imgUrl;
    private Integer hot;

    public String getIndexId() { return indexId;}

    public void setIndexId(String indexId) { this.indexId = indexId;}

    public String getPublisher() {  return publisher;}

    public void setPublisher(String publisher) {    this.publisher = publisher;}

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getPrice() { return price;}

    public void setPrice(Double price) { this.price = price;}

    public String getShape() { return shape;}

    public void setShape(String shape) { this.shape = shape;}

    public String getSeries() { return series;}

    public void setSeries(String series) { this.series = series;}

    public String getLocation() { return location;}

    public void setLocation(String location) { this.location = location;}

    public Integer getHot() { return hot;}

    public void setHot(Integer hot) { this.hot = hot;}

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return imgUrl;
    }

    public void setImage(String image) {
        this.imgUrl = image;
    }

}
