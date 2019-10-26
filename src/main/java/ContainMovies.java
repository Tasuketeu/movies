
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContainMovies implements Runnable,IContainMovies  {

    static List<Movie> moviesList=new ArrayList<>();
    static List<Review> reviewsList=new ArrayList<>();
    static String searchResult="";

    static Pattern pattern;
    static Matcher titleMatcher;
    static Matcher yearMatcher;
    static boolean wroteReview=false;
    static boolean ended=false;

    static String activeUser=null;

    private static ContainMovies instance = null;

    private ContainMovies() {} // приватный конструктор

    @Override
    public ContainMovies getInstance() { // ленивая загрузка - lazy loading
        if(instance == null){
            synchronized (ContainMovies.class) {
                if(instance == null){
                    instance = new ContainMovies();
                }
            }
        }
        return instance;
    }

    public static void getMoviesFromCSV(String uri) throws java.io.IOException{
            List<String> lines = Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] temp=line.split(";");
                    moviesList.add(new Movie(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]));
            }

            Collections.sort(moviesList);
    }



    public static boolean searchFilmToReview(String search) {
        searchResult=search;
        for (int i = 0; i < moviesList.size(); i++) {

            if (searchResult.equals(moviesList.get(i).imdb)) {
                return true;
            }
        }
        return false;
    }


    public static void getFilmInfo(String search) {

            searchResult = search;

        pattern = Pattern.compile(searchResult.toLowerCase() + ".++"); //{search}.++  greedy matching

        new Thread().start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        ended=true;
                for(int i=0;i<moviesList.size();i++) {

                    titleMatcher=pattern.matcher(moviesList.get(i).title.toLowerCase());
                    yearMatcher=pattern.matcher(moviesList.get(i).dateVars[0]);

                    if (searchResult.equals(moviesList.get(i).imdb) || titleMatcher.matches() || yearMatcher.matches()
                    || searchResult.toLowerCase().equals(moviesList.get(i).title.toLowerCase()) ||searchResult.equals(moviesList.get(i).dateVars[0]))

                    {

                            System.out.println("Фильм найден");
                            System.out.println(moviesList.get(i).filmType); //film type
                            System.out.println(moviesList.get(i).title); //title
                            System.out.println(moviesList.get(i).genre); //genre
                            System.out.println(moviesList.get(i).imdb); //imdb
                            System.out.println(moviesList.get(i).rating); //rating

                            System.out.println("\n");


                        if(searchResult.equals(moviesList.get(i).imdb)) {

                            System.out.println(moviesList.get(i).date); //date
                            System.out.println(moviesList.get(i).description); //movie description
                            if (!reviewsList.isEmpty()) {
                                for(int j=0;j<reviewsList.size();j++) {

                                        System.out.println(reviewsList.get(j).date); //date

                                        System.out.println(reviewsList.get(j).login); //login
                                        System.out.println(reviewsList.get(j).review); //review

                                        System.out.println(reviewsList.get(j).rating); //rating

                                        System.out.println("\n");
                                }
                            }
                        }
                    }
                }
    }



    public static void setActiveUser(String activeUser) {
        ContainMovies.activeUser=activeUser;
    }

    public static void addReview(String imdb,String review,String rating) {
        LocalDate date = LocalDate.now();

        for(int i=0;i<reviewsList.size();i++) {
            if (activeUser.equals(reviewsList.get(i).login)) {
                wroteReview=true;
                System.out.println("Вы уже написали обзор!");
            }
        }

        if(!wroteReview) {
            reviewsList.add(new Review(imdb, review, rating, activeUser, date));
            System.out.println("Обзор добавлен!");
        }

        wroteReview=false;

    }

    public static void editReview(String imdb,String review,String rating) {

        LocalDate date = LocalDate.now();

                    for (int i = 0; i < reviewsList.size(); i++) {
                        if (activeUser.equals(reviewsList.get(i).login) && imdb.equals(reviewsList.get(i).imdb)) {
                            reviewsList.set(i, new Review(imdb, review, rating, activeUser, date));
                            return;
                        }
                    }
        }

        public static void editReview(String imdb,String review,String rating,String login) {

        LocalDate date = LocalDate.now();

                    for (int i = 0; i < reviewsList.size(); i++) {
                        if (login.equals(reviewsList.get(i).login) && imdb.equals(reviewsList.get(i).imdb)) {
                            reviewsList.set(i, new Review(imdb, review, rating, login, date));
                            return;
                        }
                    }
        }

    public static void deleteReview(String imdb) {

        for (int i = 0; i < reviewsList.size(); i++) {
            if (activeUser.equals(reviewsList.get(i).login) && imdb.equals(reviewsList.get(i).imdb)) {
                reviewsList.remove(i);
                return;
            }
        }
    }
        public static void deleteReview(String imdb,String login){

            for (int i = 0; i < reviewsList.size(); i++) {
                if (login.equals(reviewsList.get(i).login) && imdb.equals(reviewsList.get(i).imdb)) {
                    reviewsList.remove(i);
                    return;
                }
            }
        }

    @Override
    public void run() {
        String temp = "";
        while(!ended) {
            temp += ".";
            if (temp.length() == 6) {
                temp = ".";
            }
            System.out.println("Пожалуйста, подождите, выполняется поиск" + temp);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

