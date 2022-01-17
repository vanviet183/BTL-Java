package models;


import controller.FileController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    protected int id;
    protected static int idAutoAdmin = 1;
    protected static int idAutoUser = 1;
    protected String username;
    protected String password;
    protected String rePassword;
    protected String phoneNumber;
    protected String email;
    protected String createAt;

    private String fullName;
    private int age;
    private String userAddress;
    private String gender;
    private String userType;
    private double currentMoney;
    private List<Audio> audioBought = new ArrayList<>();
    private String vipTerm;

    static FileController file = new FileController();

    static List<Account> accountAdmins = file.ReadAccountFromFile("AccountAdmins.dat");
    static List<Account> accountUsers = file.ReadUserFromFile("AccountUsers.dat");

    private String regexUsername = "[\\w]{6,}$";
    private String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*-_=+])[A-Za-z0-9!@#$%^&*-_=+]{8,}$";
    private String regexPhone = "[0-9\\+]{9,}";
    private String regexEmail = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";

    static Scanner sc = new Scanner(System.in);
    public Account() {

    }

    public Account(int id, String username, String password, String phoneNumber, String email, String createAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createAt = createAt;
    }

    public Account(int id, String username, String password, String fullName, int age, String phoneNumber, String email, String userAddress, String gender, double currentMoney, String vipTerm, String createAt, List<Audio> audioBought) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userAddress = userAddress;
        this.gender = gender;
        this.currentMoney = currentMoney;
        this.vipTerm = vipTerm;
        this.createAt = createAt;
        this.audioBought = audioBought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        this.currentMoney = currentMoney;
    }

    public List<Audio> getAudioBought() {
        return audioBought;
    }

    public void setAudioBought(List<Audio> audioBought) {
        this.audioBought = audioBought;
    }

    public String getVipTerm() {
        return vipTerm;
    }

    public void setVipTerm(String vipTerm) {
        this.vipTerm = vipTerm;
    }

    public void createAccUser() throws ParseException {
        do {
            id = idAutoUser++;
        } while (checkIdAcc(accountUsers, id));
        inputUsername();
        inputPassword();
        inputFullName();
        inputAge();
        inputPhone();
        inputEmail();
        inputAddress();
        inputGender();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        createAt = dateFormat.format(date);
        Date vipTermDate = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").parse(createAt);
        long timeVipTermDate = vipTermDate.getTime() + 1000 * 3600 * 24;
        vipTerm = dateFormat.format(new Date(timeVipTermDate));
        Audio audio = new Audio(1, "audio1", 0.0, "begin", "free");
        audioBought.add(audio);
    }

    public void inputVipTerm() {
        System.out.print("Enter vip term: ");
        vipTerm = sc.nextLine();
    }

    public void inputCurrentMoney() {
        System.out.print("Enter the amount you want to top up: ");
        currentMoney = sc.nextDouble();
        sc.nextLine();
    }

    public void inputGender() {
        System.out.print("Enter gender: ");
        gender = sc.nextLine();
    }

    public void inputAddress() {
        System.out.print("Enter address user: ");
        userAddress = sc.nextLine();
    }

    public void inputAge() {
        System.out.print("Enter age: ");
        age = sc.nextInt();
        sc.nextLine();
    }

    public void inputFullName() {
        System.out.print("Enter full name: ");
        fullName = sc.nextLine();
    }


    public void outputUser() {
        System.out.println("id = " + id +
                ", username = " + username +
                ", phoneNumber = " + phoneNumber +
                ", email = " + email +
                ", createAt = " + createAt +
                ", fullName = " + fullName +
                ", age = " + age +
                ", userAddress = " + userAddress +
                ", gender = " + gender +
                ", userType = " + userType +
                ", currentMoney = " + currentMoney + "$" +
                ", vipTerm = " + vipTerm);
    }



    public void createAccAdmin() {
        do {
            id = idAutoAdmin++;
        } while (checkIdAcc(accountAdmins, id));
        inputUsername();
        inputPassword();
        inputEmail();
        inputPhone();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        createAt = dateFormat.format(date);
    }

    public void inputUsername() {
        do {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            if(checkUsername(username)) {
                System.out.println("user exist already");
            }
        } while(!checkValidate(regexUsername, username) || checkUsername(username));
    }

    public void inputPassword() {
        do {
            System.out.print("Enter password (Example1@): ");
            password = sc.nextLine();
        } while (!checkValidate(regexPassword, password));

        do {
            System.out.print("Enter rePassword: ");
            rePassword = sc.nextLine();
        } while (!rePassword.equals(password));
    }

    public void inputEmail() {
        do {
            System.out.print("Enter email: ");
            email = sc.nextLine();
        } while (!checkValidate(regexEmail, email));
    }

    public void inputPhone() {
        do {
            System.out.print("Enter phone: ");
            phoneNumber = sc.nextLine();
        } while (!checkValidate(regexPhone, phoneNumber));
    }

    public void changeCurrentMoney(int money) {
        currentMoney = money;
    }


    public boolean checkValidate(String regex, String element) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(element);
        return matcher.find();
    }

    public boolean checkUsername(String username) {
        for (Account acc : accountAdmins) {
            if(acc.getUsername().equals(username)) {
                return true;
            }
        }

        for (Account acc : accountUsers) {
            if(acc.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIdAcc(List<Account> accounts, int id) {
        for(Account account : accounts) {
            if(account.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
