package com.fls.irh.main.item.book;

public class Book {
    public String title;
    public int coverColour;
    public int id;
    public int pages;
    public String[] content;
    public static Book[] books = new Book[10];
    public static Book tutBook = new Book(0, "tut", 500, new String[] { "Hello and welcome to IRH." });

    public Book(int id, String title, int coverColour, String[] text) {
        this.id = id;
        if (books[id] != null) {
            throw new RuntimeException("Duplicate Books :" + id);
        }
        books[id] = this;
        setCoverColour(coverColour);
        setPageAmt(text.length);
        setContent(text);
        for (int i = 0; i < text.length; i++) {
            if (text[i].length() > 10) {
                //throw new RuntimeException("Line too long : '" + text[i] + "'");
            }
        }
        setTitle(title);
        if (books[id].getTitle().length() > 9)
            throw new RuntimeException("Book Title too long : '" + title + "'");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCoverColour(int c) {
        this.coverColour = c;
    }

    public int getCoverColour() {
        return this.coverColour;
    }

    public int getId() {
        return this.id;
    }

    public void setPageAmt(int pages) {
        this.pages = pages;
    }

    public int getPageAmt() {
        return this.pages;
    }

    public void setContent(String[] text) {
        this.content = text;
    }

    public String[] getContent() {
        return this.content;
    }
}
