public interface IContainUsers {

    IContainUsers getInstance();
    public static void registerUsers(String regName,String regLogin,String regPassword){};
    public static void LoginOldUsers(String Name, String Login, String Password){};

}
