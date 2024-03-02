package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public class BookDetailsPojo
 {
        String title;
        List<Integer> covers;
        String first_publish_date;
        List<BasicAuthorPojo> authors;
        List<String> subjects;

        public BookDetailsPojo() {

        }

        public BookDetailsPojo(String title,
                               List<Integer> covers,
                               String first_publish_date,
                               List<BasicAuthorPojo> authors,
                               List<String> subjects) {
               this.title = title;
               this.covers = covers;
               this.first_publish_date = first_publish_date;
               this.authors = authors;
               this.subjects = subjects;
        }

        public String getTitle() {
               return title;
        }

        public void setTitle(String title) {
               this.title = title;
        }

        public List<Integer> getCovers() {
               return covers;
        }

        public void setCovers(List<Integer> covers) {
               this.covers = covers;
        }

        public String getFirst_publish_date() {
               return first_publish_date;
        }

        public void setFirst_publish_date(String first_publish_date) {
               this.first_publish_date = first_publish_date;
        }

        public List<BasicAuthorPojo> getAuthors() {
               return authors;
        }

        public void setAuthors(List<BasicAuthorPojo> authors) {
               this.authors = authors;
        }

        public List<String> getSubjects() {
               return subjects;
        }

        public void setSubjects(List<String> subjects) {
               this.subjects = subjects;
        }
 }
