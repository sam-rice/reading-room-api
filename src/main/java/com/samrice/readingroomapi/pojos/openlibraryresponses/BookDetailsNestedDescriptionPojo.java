package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;
import java.util.Map;

public class BookDetailsNestedDescriptionPojo extends BookDetailsPojo {
    Map<String, String> description;

    public BookDetailsNestedDescriptionPojo() {
        // Default constructor
    }

    public BookDetailsNestedDescriptionPojo(String title,
                                            List<Integer> covers,
                                            String first_publish_date,
                                            List<BasicAuthorPojo> authors,
                                            List<String> subjects) {
        super(title, covers, first_publish_date, authors, subjects);
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }
}
