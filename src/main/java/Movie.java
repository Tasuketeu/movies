

public class Movie implements Comparable<Movie> {

    String imdb,
            filmType,
            title,
            genre,
            date,
            rating,
            description;
    String[] dateVars;
    double ratingToDouble;

    public Movie(String imdb,String filmType,String title,
                 String genre,String date,String rating,String description){
        this.imdb=imdb;
        this.filmType=filmType;
        this.title=title;
        this.genre=genre;
        this.date=date;
        this.rating=rating;
        this.description=description;
        dateVars=this.date.split(" ");
        ratingToDouble=Double.parseDouble(this.rating);
    }

    @Override
    public int compareTo(Movie movie) {
        if(ratingToDouble<movie.ratingToDouble)
            return 1;
        else if(movie.ratingToDouble<ratingToDouble)
            return -1;
        return 0;
    }

}
