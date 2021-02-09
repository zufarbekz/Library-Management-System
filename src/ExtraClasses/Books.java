package ExtraClasses;

public class Books {
    private String bookID;
    private String takenBy;
    private String title;
    private String author;
    private String isbn;
    private String publishDate;

    public Books(String bookID, String takenBy, String title, String author, String isbn, String publishDate) {
        this.bookID = bookID;
        this.takenBy = takenBy;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
    }


    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String state) {
        this.takenBy = state;
    }
}

