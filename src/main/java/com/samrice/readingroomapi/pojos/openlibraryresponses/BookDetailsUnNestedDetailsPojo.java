package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public class BookDetailsUnNestedDetailsPojo extends BookDetailsPojo {
    String description;

    public BookDetailsUnNestedDetailsPojo() {
        super();
    }

    public BookDetailsUnNestedDetailsPojo(String title,
                                          List<Integer> covers,
                                          String first_publish_date,
                                          List<BasicAuthorPojo> authors,
                                          List<String> subjects) {
        super(title, covers, first_publish_date, authors, subjects);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}