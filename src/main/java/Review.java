

import java.time.LocalDate;
import java.util.*;

public class Review {

    String review,
            imdb,
            rating,
            login;
    LocalDate date;

    public Review(String imdb,String review,String rating,String login,LocalDate date){
        this.review=review;
        this.imdb=imdb;
        this.rating=rating;
        this.login=login;
        this.date=date;
    }



}
