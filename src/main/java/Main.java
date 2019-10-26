
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {
    static Connection con = null;
    static PreparedStatement preparedStatement = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static int userId=1;
    static int count;
    static boolean created=false;

    public static void main(String[] args) throws IOException {
        ContainUsers.registerUsers("admin","admin","admin");

        ContainMovies.getMoviesFromCSV("E:/movies.csv");

        String notInSystem = "Вы не в системе";
        System.out.println(notInSystem);
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите команду\n" +
                "register -- регистрация\n" +
                "login -- логин\n" +
                "logout -- выход из аккаунта\n" +
                "search -- поиск фильма\n" +
                "addReview -- добавить отзыв\n" +
                "editReview -- редактировать отзыв(для пользователей и админов)\n"+
                "deleteReview -- удалить отзыв(для пользователей и админов)\n" +
                "exit -- выход из программы:");

        String commands=sc.next();
        String login;


        while (!(commands.equals("exit"))) {
            if (ContainUsers.inSystem) {

                if (commands.equals("logout")) {
                    ContainUsers.inSystem=false;
                    if(ContainUsers.adminMode){
                        ContainUsers.adminMode=false;
                    }
                    System.out.println("Вы не в системе");
                }

                if (commands.equals("search")) {
                        System.out.println("Поиск фильмов:");
                        ContainMovies.getFilmInfo(sc.next());
                        System.out.println("Введите imdb идентификатор, чтобы получить детали фильма:");
                        commands=sc.next();
                        ContainMovies.getFilmInfo(commands);
                }

                    if (commands.equals("addReview")) {
                        System.out.println("Введите imdb фильма, на который хотите написать обзор:");
                        commands=sc.next();
                        String myReview;
                        String myRating;
                        if(ContainMovies.searchFilmToReview(commands))
                        {
                            System.out.println("Фильм найден! Напишите отзыв:");
                            myReview=sc.next();
                            System.out.println("Дайте оценку:");
                            myRating=sc.next();
                            ContainMovies.addReview(commands,myReview,myRating);
                        }
                        else{
                            System.out.println("Некорректный imdb!");
                        }
                    }

                    if (commands.equals("myReviews")) {
                        //review.getMyReviews();
                    }

                    if (commands.equals("editReview")) {
                        if(!ContainUsers.adminMode) {
                            System.out.println("Введите imdb фильма, на который вы написали отзыв и хотите отредактировать:");
                            commands = sc.next();
                            String myReview;
                            String myRating;
                            if (ContainMovies.searchFilmToReview(commands)) {
                                System.out.println("Фильм найден! Отредактируйте отзыв:");
                                myReview = sc.next();
                                System.out.println("Дайте оценку:");
                                myRating = sc.next();
                                ContainMovies.editReview(commands,myReview,myRating);
                                System.out.println("Обзор изменён!");
                            }
                            else{
                                System.out.println("Некорректный imdb!");
                            }
                        }
                        else {
                            System.out.println("Введите imdb фильма, для которого хотите отредактировать отзыв:");
                            commands=sc.next();
                            String myReview;
                            String myRating;
                            if(ContainMovies.searchFilmToReview(commands))
                            {
                                System.out.println("Фильм найден! Выберите пользователя, чей отзыв хотите отредактировать:");
                                login=sc.next();
                                System.out.println("Отредактируйте отзыв:");
                                myReview=sc.next();
                                System.out.println("Дайте оценку:");
                                myRating=sc.next();
                                ContainMovies.editReview(commands,myReview,myRating,login);
                                System.out.println("Обзор изменён!");
                            }
                            else{
                                System.out.println("Некорректный imdb!");
                            }
                        }
                    }

                    if (commands.equals("deleteReview")) {
                        if(!ContainUsers.adminMode) {
                            System.out.println("Введите imdb фильма, на который вы написали отзыв и хотите удалить:");
                            commands = sc.next();
                            if (ContainMovies.searchFilmToReview(commands)) {
                                ContainMovies.deleteReview(commands);

                                System.out.println("Фильм найден!Обзор удалён!");
                            }
                            else{
                                System.out.println("Некорректный imdb!");
                            }
                        }
                        else {
                            System.out.println("Введите imdb фильма, для которого хотите удалить отзыв:");
                            commands = sc.next();
                            if (ContainMovies.searchFilmToReview(commands)) {
                                System.out.println("Фильм найден! Выберите пользователя, чей отзыв хотите удалить:");
                                login = sc.next();
                                ContainMovies.deleteReview(commands,login);
                                System.out.println("Фильм найден!Обзор удалён!");
                            }
                            else{
                                System.out.println("Некорректный imdb!");
                            }
                        }
                    }

            }
            else {

                if (commands.equals("register")) {



                    System.out.println("Зарегистрируйтесь:");
                    System.out.println("Введите имя:");
                    String regName= sc.next();
                    System.out.println("Введите логин:");
                    String regLogin = sc.next();
                    System.out.println("Введите пароль:");
                    String regPassword = sc.next();
                    ContainUsers.registerUsers(regName,regLogin,regPassword);
                    try {
                        DriverManager.registerDriver(new org.h2.Driver());
                        con = DriverManager.getConnection("jdbc:h2:tcp://localhost/./test", "sa", "1");
                        if(!created) {
                            stmt = con.createStatement();
                            stmt.execute(" create table USERS ( " +
                                    " ID number(100) primary key," +
                                    " NAME varchar(100)," +
                                    " LOGIN varchar(100)," +
                                    " PASSWORD varchar(100)) ");
                            created=true;
                        }
                        if(created) {
                            preparedStatement = con.prepareStatement("insert into USERS(ID,NAME,LOGIN,PASSWORD) values(?,?,?,?)");


                            preparedStatement.setInt(1, userId);
                            preparedStatement.setString(2, regName);
                            preparedStatement.setString(3, regLogin);
                            preparedStatement.setString(4, regPassword);
                            userId++;

                            System.out.println("User added");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try { if (stmt != null) stmt.close(); } catch (Exception e) {};
                        try { if (con != null) con.close(); } catch (Exception e) {};
                    }

                }

                if (commands.equals("login")) {
                    System.out.println("Залогиньтесь:");
                    System.out.println("Введите имя:");
                    String Name= sc.next();
                    System.out.println("Введите логин:");
                    String Login = sc.next();
                    System.out.println("Введите пароль:");
                    String Password = sc.next();
                    ContainUsers.LoginOldUsers(Name,Login,Password);
                }

            }
            commands = sc.next();
        }

    }


}
