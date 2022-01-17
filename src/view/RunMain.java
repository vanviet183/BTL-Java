package view;

import controller.FileController;
import models.Account;
import models.Audio;
import models.Bill;
import models.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RunMain {
    static Scanner sc = new Scanner(System.in);
    static FileController file = new FileController();
    static List<Account> accountAdmins = file.ReadAccountFromFile("AccountAdmins.dat");
    static List<Account> accountUsers = file.ReadUserFromFile("AccountUsers.dat");
    static List<Audio> audioWarehouse = file.ReadAudiosFromFile("AudioWarehouse.dat");
    static List<Bill> billList = file.ReadBillsFromFile("Bills.dat");
    static List<Bill> billVipList = file.ReadBillsVipFromFile("BillsVip.dat");
    static List<Bill> billRechargeList = file.ReadBillsRechargeFromFile("BillsRecharge.dat");
    static List<Message> notifyList = file.ReadMessageFromFile("Notify.dat");
    static int accountType = 0;
    static boolean isMaintenance = false;
    public static void main(String[] args) throws ParseException {
        option();
    }

    public static void option() throws ParseException {
        do {
            System.out.println("1. Log in\n" +
                    "2. Sign up\n" +
                    "3. Exit");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    logIn();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    System.exit(1);
            }
        } while (true);
    }

    private static void logIn() throws ParseException {
        int dem = 0;
        accountUsers = file.ReadUserFromFile("AccountUsers.dat");
        accountAdmins = file.ReadAccountFromFile("AccountAdmins.dat");
        int cnt2 = 0;
        sc.nextLine();
        do {
            if (cnt2 < 5) {
                String username;
                String password;
                System.out.print("Enter username: ");
                username = sc.nextLine();
                System.out.print("Enter password: ");
                password = sc.nextLine();
                for (Account acc : accountAdmins) {
                    if (username.equals(acc.getUsername()) && password.equals(acc.getPassword())) {
                        dem++;
                        adminOptions(acc);
                    }
                }

                for (Account user : accountUsers) {
                    if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                        dem++;
                        if (isMaintenance) {
                            System.out.println("404 The system is under maintenance, please try again!");
                        } else {
                            userOptions(user);
                        }
                    }
                }

                if(dem == 0) {
                    System.out.println("The username or password you entered is incorrect, please try again.");

                }
            }
            cnt2++;
        } while (dem == 0 && cnt2 <= 5);
        if(cnt2 > 5) {
            System.out.println("1. Forget password\n"
                                + "2. Create account");
            System.out.print("Enter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1: forgetPassword();
                    break;
                case 2: signUp();
                    break;
                default: break;
            }
        }
    }

    private static void forgetPassword() throws ParseException {
        accountUsers = file.ReadUserFromFile("accountUsers.dat");
        sc.nextLine();
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        int dem = 0;
        int cnt2 = 0;
        for (Account user : accountUsers) {
            if (user.getUsername().equals(username)) {
                dem++;
                cnt2 = 0;
                do {
                    if (cnt2 < 5) {
                        System.out.print("Enter phone number: ");
                        String phone = sc.nextLine();
                        if (user.getPhoneNumber().equals(phone)) {
                            changePassword(user);
                            logIn();
                        } else {
                            System.out.println("Wrong phone number!");
                        }
                    }
                    cnt2++;
                } while (cnt2 <= 5);
            }
        }
        if (cnt2 > 5) {
            System.out.println("Calm remember and try again!");
        }
        if (dem == 0) {
            System.out.println("User invalid, please try again!");
        }
    }

    private static void userOptions(Account user) throws ParseException {
        notifyList = file.ReadMessageFromFile("Notify.dat");
        int cnt = 0;
        for (Message mess : notifyList) {
            if (mess.getStatus().equals("not seen")) {
                cnt++;
            }
        }
        do {
            System.out.println("1. Show info warehouse\n"
                    +  "2. Notify (" + cnt + ")\n"
                    +  "3. Buy audio\n"
                    +  "4. Show audio bought\n"
                    +  "5. Search audio\n"
                    +  "6. Delete audio bought\n"
                    +  "7. Show bill bought\n"
                    +  "8. Handle bill\n"
                    +  "9. Change info\n"
                    +  "10. Show info vip\n"
                    +  "11. Buy vip\n"
                    +  "12. Recharge\n"
                    +  "13. Log out\n"
                    +  "14. Exit");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1: showWarehouse();
                    break;
                case 2: showNotifyNotSeen(user);
                    break;
                case 3: buyAudio(user);
                    break;
                case 4: showAudioBought(user);
                    break;
                case 5: searchAudio();
                    break;
                case 6: deleteAudioBought(user);
                    break;
                case 7: showBillBought(user);
                    break;
                case 8: handleBillUser(user);
                    break;
                case 9: changeUser();
                    break;
                case 10: showInfoVip(user);
                    break;
                case 11: buyVip(user);
                    break;
                case 12: recharge(user);
                    break;
                case 13:
                    option();
                case 14: System.exit(1);
                default: break;
            }
        } while (true);
    }

    public static void showNotifyNotSeen(Account user) throws ParseException {
        int dem = 0;
        for (Message mess : notifyList) {
            if (mess.getStatus().equals("not seen")) {
                dem++;
                mess.output();
                mess.setStatus("Seen");
            }
        }
        if (dem > 0) {
            file.UpdateMessageToFile(notifyList, "Notify.dat");
            System.out.println("================================================================================");
            userOptions(user);
        }
        if (dem == 0) {
            System.out.println("=== No have new notify ===");
            showNotifications();
        }
    }

    private static void recharge(Account user) {
        System.out.print("Enter the amount you want to top up: ");
        double moneyRecharge = sc.nextDouble();
        Random generator = new Random();
        int otp = generator.nextInt((9999 - 1000) + 1) + 1000;
        int codeOtp = 0;
        int dem = 0;
        do {
            if(dem < 5) {
                System.out.print("Enter code OTP: " + otp + " to top up money: ");
                codeOtp = sc.nextInt();
            }
            dem++;
        } while (codeOtp != otp && dem <= 5);
        if(dem > 5) {
            System.out.println("Recharge money failure!");
        } else {
            Bill bill = new Bill();
            bill.createBillUser(user, moneyRecharge);
            file.WriteBillRechargeToFile("BillsRecharge.dat", bill);
            user.setCurrentMoney((double) (user.getCurrentMoney() + moneyRecharge));
            file.UpdateUserFile(accountUsers, "AccountUsers.dat");
            System.out.println("Recharge money success!");
        }
    }

    private static void buyVip(Account user) throws ParseException {
        System.out.println("1. One week - 9$\n" +
                            "2. One month - 29$\n" +
                            "3. One year - 329$");
        System.out.print("\tEnter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1: buyVipTime(user, 7, 9);
                break;
            case 2: buyVipTime(user, 30, 29);
                break;
            case 3: buyVipTime(user, 365, 329);
                break;
            default: break;
        }
    }

    private static void buyVipTime(Account user, int addTime, int price) throws ParseException {
        if(user.getCurrentMoney() >= price) {
            System.out.println("Buy vip success!");
            double moneyBack = (double) (user.getCurrentMoney() - price);
            user.setCurrentMoney(moneyBack);
            Bill bill = new Bill();
            bill.createBillVip(user, price, addTime);
//            Date timeBuy = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").parse(bill.getDateOfPurchase());
//            long expirationDate = timeBuy.getTime() + (addTime * 24 * 3600 * 1000);
            Date currentVipTerm = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").parse(user.getVipTerm());
            long totalVipTerm = currentVipTerm.getTime() / 1000 + (addTime * 24 * 3600);
            String vipTerm = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").format(new Date(totalVipTerm * 1000));
            user.setVipTerm(vipTerm);
            file.UpdateUserFile(accountUsers, "AccountUsers.dat");
            file.WriteBillVipToFile("BillsVip.dat", bill);
        } else {
            System.out.println("Not enough money!");
        }
    }

    private static void showInfoVip(Account user) throws ParseException {
        accountUsers = file.ReadUserFromFile("AccountUsers.dat");
        long now = System.currentTimeMillis();
        Date vipTerm = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").parse(user.getVipTerm());
        long distance = vipTerm.getTime() - now;
        int days = (int) (distance / (24 * 3600 * 1000));
        int hours = (int) (distance % (24 * 3600 * 1000)) / (3600 * 1000);
        if (hours < 0) {
            hours = 0;
        }
        System.out.println("Time remaining: " + days + " days " + hours + " hours");
        System.out.println("Vip term of user: " + user.getVipTerm());
    }

    private static void handleBillUser(Account user) {
        System.out.println("1. Delete bill\n" +
                            "2. Sort bill");
        System.out.print("\tEnter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1: deleteBill();
                break;
            case 2: sortBill();
                break;
            default: break;
        }
    }

    private static void showBillBought(Account user) {
        billList = file.ReadBillsFromFile("Bills.dat");
        billVipList = file.ReadBillsVipFromFile("BillsVip.dat");
        billRechargeList = file.ReadBillsRechargeFromFile("BillsRecharge.dat");
        int cnt = 0;
        if (billList.size() > 0) {
            for(Bill bill : billList) {
                if(bill.getUsernameOfBill().equals(user.getUsername())) {
                    cnt++;
                    bill.output();
                }
                System.out.println("================================================================================");
            }
        }

        if (billVipList.size() > 0) {
            for(Bill bill : billVipList) {
                if(bill.getUsernameOfBill().equals(user.getUsername())) {
                    cnt++;
                    bill.outputBillVip();
                }
                System.out.println("================================================================================");
            }
        }

        if (billRechargeList.size() > 0) {
            for(Bill bill : billRechargeList) {
                if(bill.getUsernameOfBill().equals(user.getUsername())) {
                    cnt++;
                    bill.outputBillRecharge();
                }
                System.out.println("================================================================================");
            }
        }

        if (cnt == 0) {
            System.out.println("No transactions have been made yet!");
        }
    }

    private static void deleteAudioBought(Account user) {
        int dem = 0;
        System.out.print("Enter id audio want delete: ");
        int idAudioDelete = sc.nextInt();
        List<Audio> audioUserBought = user.getAudioBought();
        for (Audio audio : audioUserBought) {
            if (audio.getAudioId() == idAudioDelete) {
                dem++;
                audioUserBought.remove(audio);
            }
        }
        if(dem > 0) {
            user.setAudioBought(audioUserBought);
            System.out.println("Delete audio success!");
        }
        if (dem == 0) {
            System.out.println("Id audio invalid!");
        }
        user.setAudioBought(audioUserBought);
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void searchAudio() {
        System.out.println("1. Search audio by id\n" +
                            "2. Search audio by name\n" +
                            "3. Search audio by price");
        System.out.print("\tEnter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1: searchAudioById();
                break;
            case 2: searchAudioByName();
                break;
            case 3: searchAudioByPrice();
                break;
            default: break;
        }
    }

    private static void searchAudioByPrice() {
        int dem = 0;
        do {
            System.out.print("\tEnter price audio want search: ");
            int priceAudio = sc.nextInt();
            for (Audio audio : audioWarehouse) {
                if (audio.getPrice() > (priceAudio - 10) && audio.getPrice() < (priceAudio + 10)) {
                    dem++;
                    audio.output();
                }
            }
            if (dem == 0) {
                System.out.println("Price audio invalid, please try again.");
            }
        } while (dem == 0);
    }

    private static void searchAudioByName() {
        int dem = 0;
        System.out.print("\tEnter name audio want search: ");
        sc.nextLine();
        String nameAudio = sc.nextLine();
        for (Audio audio : audioWarehouse) {
            if (cutName(audio.getAudioName()).equals(nameAudio)) {
                dem++;
                audio.output();
            }
        }
        if (dem == 0) {
            System.out.println("Name audio invalid, please try again.");
        }
    }

    private static void searchAudioById() {
        int dem = 0;
        do {
            System.out.print("\tEnter id audio want search: ");
            int idAudio = sc.nextInt();
            for (Audio audio : audioWarehouse) {
                if (audio.getAudioId() == idAudio) {
                    dem++;
                    audio.output();
                }
            }
            if (dem == 0) {
                System.out.println("Id audio invalid, please try again.");
            }
        } while (dem == 0);
    }

    private static void showAudioBought(Account user) {
        List<Audio> audioUserBought = user.getAudioBought();
        System.out.println("Quantity audio bought: " + user.getAudioBought().size());
        for (Audio audio : audioUserBought) {
            audio.output();
        }
    }

    public static void buyAudio(Account user) throws ParseException {
        Bill billBuy = new Bill();
        List<Audio> audioBuy = createBuyAudio(user);
        billBuy.setAudioBought(audioBuy);
        billBuy.createBill(user);
        billBuy.output();
        System.out.printf("%20s %20s", "1. Confirm", "2. Cancel\n");
        System.out.println("Current money: " + user.getCurrentMoney() + "$");
        System.out.print("Enter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1:
                if(user.getCurrentMoney() >= billBuy.getTotalMoney()) {
                    System.out.println("Buy success!");
                    user.setCurrentMoney((double) (user.getCurrentMoney() - billBuy.getTotalMoney()));
                    List<Audio> audioUserBought = user.getAudioBought();
                    for (Audio audio : audioBuy) {
                        audioUserBought.add(audio);
                    }
                    user.setAudioBought(audioUserBought);
                    file.UpdateUserFile(accountUsers, "AccountUsers.dat");
                    file.WriteBillToFile("Bills.dat", billBuy);
                } else {
                    System.out.println("Not enough money, please recharge money to perform transaction");
                }
                break;
            case 2:
                audioBuy = null;
                System.out.println("Cancel success!");
                break;
            default: break;
        }
    }

    private static List<Audio> createBuyAudio(Account user) throws ParseException {
        List<Audio> audioBuy = new ArrayList<>();
        int idAudioBuy = 0;
        List<Integer> idAudioInputted = new ArrayList<>();
        do {
            int dem = 0;
            showWarehouse();
            do {
                System.out.print("Enter id audio to buy: ");
                idAudioBuy = sc.nextInt();
            } while(checkIdInputted(idAudioInputted, idAudioBuy));
            idAudioInputted.add(idAudioBuy);
            for (Audio audio : audioWarehouse) {
                if(audio.getAudioId() == idAudioBuy && !checkIdAudioBought(user, audio.getAudioId())) {
                    if (audio.getAudioType().equals("vip")) {
                        if (checkUserVip(user)) {
                            dem++;
                            audioBuy.add(audio);
                            System.out.println("Audio has been added to cart!");
                        } else {
                            System.out.println("Vip expiration, please buy vip to buy audios vip!");
                        }
                    } else {
                        dem++;
                        audioBuy.add(audio);
                        System.out.println("Audio has been added to cart!");
                    }

                }
            }
            if(checkIdAudioBought(user ,idAudioBuy)) {
                System.out.println("Audio bought!");
            }
            System.out.println("===== Enter 0 to payment =====");
        } while (idAudioBuy != 0);
        return audioBuy;
    }

    public static boolean checkUserVip(Account user) throws ParseException {
        long now = System.currentTimeMillis();
        Date vipTerm = new SimpleDateFormat("dd-M-yyyy HH:mm:ss").parse(user.getVipTerm());
        if (vipTerm.getTime() >= now) {
            return true;
        }
        return false;
    }

    public static boolean checkIdInputted(List<Integer> arr, int id) {
        for (int i : arr) {
            if (i == id)
                return true;
        }
        return false;
    }

    private static boolean checkIdAudioBought(Account user, int idAudio) {
        List<Audio> audioUserBought = user.getAudioBought();
        for(Audio audio : audioUserBought) {
            if(audio.getAudioId() == idAudio) {
                return true;
            }
        }
        return false;
    }

    // Admin
    private static void adminOptions(Account admin) throws ParseException {
        do {
            System.out.println("1. Show info warehouse\n"
                    + "2. Handle audios\n"
                    + "3. Show info all bill\n"
                    + "4. Handle bill\n"
                    + "5. Show info users\n"
                    + "6. Handle users\n"
                    + "7. Change info\n"
                    + "8. Show revenue\n"
                    + "9. Create notification\n"
                    + "10. Show notification\n"
                    + "11. Handle notification\n"
                    + "12. Handle maintenance\n"
                    + "13. Log out");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    showWarehouse();
                    break;
                case 2:
                    handleAudio(admin);
                    break;
                case 3:
                    showBill();
                    break;
                case 4:
                    handleBill(admin);
                    break;
                case 5:
                    showUsers();
                    break;
                case 6:
                    handleUser(admin);
                    break;
                case 7:
                    changeInfo(admin);
                    break;
                case 8:
                    showRevenue();
                    break;
                case 9:
                    createNotification();
                    break;
                case 10:
                    showNotifications();
                    break;
                case 11:
                    handleNotification(admin);
                    break;
                case 12:
                    handleMaintenance(admin);
                    break;
                case 13: option();
                default: break;
            }
        } while (true);
    }

    private static void handleMaintenance(Account admin) {
        if (isMaintenance == false) {
            isMaintenance = true;
            System.out.println("Start maintenance!");
        } else {
            isMaintenance = false;
            System.out.println("Stop maintenance!");
        }
    }

    private static void showNotifications() {
        notifyList = file.ReadMessageFromFile("Notify.dat");
        if (notifyList.size() == 0) {
            System.out.print("No notify!");
        } else {
            for (Message mess : notifyList)
                mess.output();
        }
    }

    private static void handleNotification(Account admin) throws ParseException {
        System.out.println("1. Change notify\n"
                            + "2. Delete notify\n"
                            + "3. Come back");
        System.out.print("Enter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1: changeMessage(admin);
                break;
            case 2: deleteMessage(admin);
                break;
            case 3: adminOptions(admin);
                break;
            default: break;
        }
    }

    private static void deleteMessage(Account admin) throws ParseException {
        int cnt = 0;
        System.out.print("Enter id message want delete: ");
        int id = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < notifyList.size(); i++) {
            if (notifyList.get(i).getIdMessage() == id) {
                cnt++;
                notifyList.remove(notifyList.get(i));
                file.UpdateMessageToFile(notifyList, "Notify.dat");
                System.out.println("Delete notify success!");
                showNotifications();
            }
        }
        if (cnt == 0) {
            System.out.print("Id notify invalid!");
            handleNotification(admin);
        }
    }

    private static void changeMessage(Account admin) throws ParseException {
        int cnt = 0;
        System.out.print("Enter id message want change: ");
        int id = sc.nextInt();
        sc.nextLine();
        for (Message mess : notifyList) {
            if (mess.getIdMessage() == id) {
                cnt++;
                System.out.print("Enter message new: ");
                String messNew = sc.nextLine();
                mess.setMessage(messNew);
                file.UpdateMessageToFile(notifyList, "Notify.dat");
                System.out.println("Change notify success!");
                showNotifications();
            }
        }
        if (cnt == 0) {
            System.out.print("Id notify invalid!");
            handleNotification(admin);
        }
    }

    private static void createNotification() {
        Message notify = new Message();
        notify.createMessage();
        file.WriteMessageToFile("Notify.dat", notify);
        System.out.println("Send notify success!");
    }

    private static void showRevenue() {
        billList = file.ReadBillsFromFile("Bills.dat");
        billVipList = file.ReadBillsVipFromFile("BillsVip.dat");
        int revenue = 0;
        for (Bill bill : billRechargeList) {
            revenue += bill.getTotalMoney();
        }
        System.out.println("Revenue of shop: " + revenue + "$");
    }

    public static void changeInfo(Account admin) throws ParseException {
        System.out.print("Enter id admin need change: ");
        int idAdmin = sc.nextInt();
        int dem = 0;
        for (Account acc : accountAdmins) {
            if(acc.getId() == idAdmin) {
                dem++;
                do {
                    System.out.println("1. Change username\n" +
                            "2. Change password\n" +
                            "3. Change email\n" +
                            "4. Change phone number\n" +
                            "5. Come back");
                    System.out.print("Enter select: ");
                    int choose = sc.nextInt();
                    switch (choose) {
                        case 1: changeUsernameAdmin(admin);
                            break;
                        case 2: changePasswordAdmin(admin);
                            break;
                        case 3: changeEmailAdmin(admin);
                            break;
                        case 4: changePhoneNumberAdmin(admin);
                            break;
                        case 5: adminOptions(admin);
                            break;
                        default: break;
                    }
                } while (dem == 0);
            }
        }
    }

    private static void changePhoneNumberAdmin(Account admin) {
        admin.inputPhone();
        System.out.println("Change phone number success!");
        file.UpdateAccountFile(accountAdmins, "AccountAdmins.dat");
    }

    private static void changeEmailAdmin(Account admin) {
        admin.inputEmail();
        System.out.println("Change email success!");
        file.UpdateAccountFile(accountAdmins, "AccountAdmins.dat");
    }

    private static void changePasswordAdmin(Account admin) {
        admin.inputPassword();
        System.out.println("Change password success!");
        file.UpdateAccountFile(accountAdmins, "AccountAdmins.dat");
    }

    private static void changeUsernameAdmin(Account admin) {
        admin.inputUsername();
        System.out.println("Change username success!");
        file.UpdateAccountFile(accountAdmins, "AccountAdmins.dat");
    }

    private static void handleUser(Account admin) throws ParseException {
        do {
            System.out.println("1. Add user\n" +
                    "2. Change user\n" +
                    "3. Delete user\n" +
                    "4. Search user\n" +
                    "5. Come back");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    createAccUser();
                    break;
                case 2:
                    changeUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    searchUser(admin);
                    break;
                case 5:
                    adminOptions(admin);
                    break;
                default: break;
            }
        } while (true);
    }

    private static void searchUser(Account admin) {
        System.out.println("1.Search user by id\n" +
                "2. Search user by name" +
                "3. Search user by phone");
        System.out.print("\tEnter select search: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1:
                searchUserById();
                break;
            case 2:
                searchUserByName();
                break;
            case 3:
                searchUserByPhone();
                break;
            case 4:
                System.exit(1);
            default:
                break;
        }

    }

    private static void searchUserByPhone() {
        int dem = 0;
        System.out.print("\tEnter phone user need search: ");
        String phoneUser = sc.nextLine();
        for (Account user : accountUsers) {
            if (cutName(user.getPhoneNumber()).equals(phoneUser)) {
                dem++;
                user.outputUser();
            }
        }
        if (dem == 0) {
            System.out.println("Phone user invalid, please try again.");
        }
    }

    private static void searchUserByName() {
        int dem = 0;
        System.out.print("\tEnter name user need search: ");
        String nameUser = sc.nextLine();
        for (Account user : accountUsers) {
            if (cutName(user.getFullName()).equals(nameUser)) {
                dem++;
                user.outputUser();
            }
        }
        if (dem == 0) {
            System.out.println("Name user invalid, please try again.");
        }
    }

    public static String cutName(String str) {
        String a[] = str.split(" ");
        return a[a.length - 1];
    }

    private static void searchUserById() {
        int dem = 0;
        do {
            System.out.print("\tEnter id user need search: ");
            int idUser = sc.nextInt();
            for (Account user : accountUsers) {
                if (user.getId() == idUser) {
                    dem++;
                    user.outputUser();
                }
            }
            if (dem == 0) {
                System.out.println("Id user invalid, please try again.");
            }
        } while (dem == 0);

    }

    private static void deleteUser() {
        int dem = 0;
        System.out.print("\tEnter id user delete: ");
        int idUserDelete = sc.nextInt();
        for (Account user : accountUsers) {
            if (user.getId() == idUserDelete) {
                dem++;
                file.UpdateUserFile(accountUsers, "AccountUsers.dat");
            }
        }

        if (dem == 0) {
            System.out.println("id bill invalid");
        }
    }

    private static void changeUser() {
        int dem = 0;
        do {
            showUsers();
            System.out.print("Enter id user change: ");
            int idUserChange = sc.nextInt();
            for (Account user : accountUsers) {
                if (user.getId() == idUserChange) {
                    dem++;
                    do {
                        System.out.println("1. Change username \n" +
                                "2. Change user password\n" +
                                "3. Change user full name\n" +
                                "4. Change user age\n" +
                                "5. Change user address\n" +
                                "6. Change user gender\n" +
                                "7. Change user type\n" +
                                "8. Change user current money\n" +
                                "9. Change user vip term\n" +
                                "10. Change user phone number\n" +
                                "11. Change user email\n" +
                                "12. Exit");
                        System.out.print("\tEnter select: ");
                        int choose = sc.nextInt();
                        switch (choose) {
                            case 1:
                                changeUsername(user);
                                break;
                            case 2:
                                changePassword(user);
                                break;
                            case 3:
                                changeFullName(user);
                                break;
                            case 4:
                                changeAge(user);
                                break;
                            case 5:
                                changeAddress(user);
                                break;
                            case 6:
                                changeGender(user);
                                break;
                            case 7:
                                changeCurrentMoney(user);
                                break;
                            case 8:
                                changeVipTerm(user);
                                break;
                            case 9:
                                changePhoneNumber(user);
                                break;
                            case 10:
                                changeEmail(user);
                                break;
                            case 11:
                                System.exit(1);
                        }
                    } while (true);
                }
            }

            if (dem == 0) {
                System.out.println("id bill invalid");
            }
        } while (dem == 0);
    }

    private static void changeEmail(Account user) {
        user.inputEmail();
        System.out.println("Change email success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changePhoneNumber(Account user) {
        user.inputPhone();
        System.out.println("Change phone number success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeVipTerm(Account user) {
        user.inputVipTerm();
        System.out.println("Change vip term success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeCurrentMoney(Account user) {
        user.inputCurrentMoney();
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
        System.out.println("Recharge money success!");
    }

    private static void changeGender(Account user) {
        user.inputGender();
        System.out.println("Change gender success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeAddress(Account user) {
        user.inputAddress();
        System.out.println("Change address success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeAge(Account user) {
        user.inputAge();
        System.out.println("Change age success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeFullName(Account user) {
        user.inputFullName();
        System.out.println("Change full name success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changePassword(Account user) {
        user.inputPassword();
        System.out.println("Change password success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void changeUsername(Account user) {
        user.inputUsername();
        System.out.println("Change username success!");
        file.UpdateUserFile(accountUsers, "AccountUsers.dat");
    }

    private static void showUsers() {
        accountUsers = file.ReadUserFromFile("AccountUsers.dat");
        for (Account user : accountUsers) {
            user.outputUser();
        }
    }

    private static void handleBill(Account admin) throws ParseException {
        do {
            System.out.println("1. Change bill\n" +
                    "2. Delete bill\n" +
                    "3. Sort bill\n" +
                    "4. Come back\n" +
                    "5. Exit");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    changeBill(admin);
                    break;
                case 2:
                    deleteBill();
                    break;
                case 3:
                    sortBill();
                    break;
                case 4:
                    adminOptions(admin);
                    break;
                case 5:
                    System.exit(1);
                default: break;
            }
        } while (true);
    }

    private static void sortBill() {
        System.out.println("1. Sort bill by id\n" +
                "2. Sort bill by quantity bought\n" +
                "2. Sort bill by total money\n" +
                "3. Sort bill by date bought\n" +
                "4. Exit");
        System.out.print("\tEnter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1:
                sortBillById();
                break;
            case 2:
                sortBillByQuantity();
                break;
            case 3:
                sortBillByTotalMoney();
                break;
            case 4:
                sortBillByDate();
                break;
            case 5:
                System.exit(1);
            default:
                break;
        }
    }

    private static void sortBillByDate() {
        Collections.sort(billList, new Comparator<Bill>() {
            @Override
            public int compare(Bill bill1, Bill bill2) {
                return bill1.getDateOfPurchase().compareTo(bill2.getDateOfPurchase());
            }
        });
        for (Bill bill : billList) {
            bill.output();
        }
    }

    private static void sortBillByTotalMoney() {
        Collections.sort(billList, new Comparator<Bill>() {
            @Override
            public int compare(Bill bill1, Bill bill2) {
                return (int)(bill1.getTotalMoney() - bill2.getTotalMoney());
            }
        });
        for (Bill bill : billList) {
            bill.output();
        }
    }

    private static void sortBillByQuantity() {
        Collections.sort(billList, new Comparator<Bill>() {
            @Override
            public int compare(Bill bill1, Bill bill2) {
                return bill1.getBillID() - bill2.getBillID();
            }
        });
        for (Bill bill : billList) {
            bill.output();
        }
    }

    private static void sortBillById() {
        Collections.sort(billList, new Comparator<Bill>() {
            @Override
            public int compare(Bill bill1, Bill bill2) {
                return bill1.getBillID() - bill2.getBillID();
            }
        });
        for (Bill bill : billList) {
            bill.output();
        }
    }

    private static void deleteBill() {
        int dem = 0;
        do {
            System.out.print("\tEnter id bill delete: ");
            int idBillDelete = sc.nextInt();
            for (int i = 0; i < billVipList.size(); i++) {
                if (billVipList.get(i).getBillID() == idBillDelete) {
                    dem++;
                    billVipList.remove(billVipList.get(i));
                    file.UpdateBillFile(billList, "Bills.dat");
                }
            }

            if (dem == 0) {
                System.out.println("id bill invalid");
            }
        } while (dem == 0);
    }

    private static void changeBill(Account admin) throws ParseException {
        int dem = 0;
        do {
            System.out.print("Enter id bill change: ");
            int idBillChange = sc.nextInt();
            for (Bill bill : billList) {
                if (bill.getBillID() == idBillChange) {
                    dem++;
                    do {
                        System.out.println("1. Change bill dateOfPurchase\n" +
                                "2. Change bill usernameOfBill\n" +
                                "3. Come back\n" +
                                "4. Exit");
                        System.out.print("Enter select: ");
                        int choose = sc.nextInt();
                        switch (choose) {
                            case 1:
                                changeDateOfPurchase(bill);
                                break;
                            case 2:
                                changeUsernameOfBill(bill);
                                break;
                            case 3:
                                handleBill(admin);
                                break;
                            case 4:
                                System.exit(1);
                        }
                    } while (true);
                }
            }

            if (dem == 0) {
                System.out.println("id bill invalid");
            }
        } while (dem == 0);
    }

    private static void changeUsernameOfBill(Bill bill) {
        sc.nextLine();
        System.out.print("Enter new username of bill: ");
        bill.setUsernameOfBill(sc.nextLine());
        file.UpdateBillFile(billList, "Bills.dat");
    }

    private static void changeDateOfPurchase(Bill bill) {
        sc.nextLine();
        System.out.print("Enter new date of purchase: ");
        bill.setDateOfPurchase(sc.nextLine());
        file.UpdateBillFile(billList, "Bills.dat");
    }

    private static void showBill() {
        billList = file.ReadBillsFromFile("Bills.dat");
        billVipList = file.ReadBillsVipFromFile("BillsVip.dat");
        billRechargeList = file.ReadBillsRechargeFromFile("BillsRecharge.dat");
        int dem = 0;
        if(billList.size() > 0) {
            dem++;
            for (Bill bill : billList) {
                bill.output();
            }
            System.out.println("================================================================================");
        }

        if(billVipList.size() > 0) {
            dem++;
            for (Bill bill : billVipList) {
                bill.outputBillVip();
            }
            System.out.println("================================================================================");
        }
        if(billRechargeList.size() > 0) {
            dem++;
            for (Bill bill : billRechargeList) {
                bill.outputBillRecharge();
            }
            System.out.println("================================================================================");
        }
        if(dem == 0) {
            System.out.println("Bill null!");
        }
    }

    private static void handleAudio(Account admin) throws ParseException {
        do {
            System.out.println("1. Add audio\n" +
                    "2. Change audio\n" +
                    "3. Delete audio\n" +
                    "4. Sort audio\n" +
                    "5. Come back\n" +
                    "6. Exit");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    addAudio();
                    break;
                case 2:
                    changeAudio(admin);
                    break;
                case 3:
                    deleteAudio();
                    break;
                case 4:
                    sortAudio(admin);
                    break;
                case 5:
                    adminOptions(admin);
                    break;
                case 6: System.exit(1);
                default: break;
            }
        } while (true);

    }

    private static void sortAudio(Account admin) throws ParseException {
        System.out.println("1. Sort audio by id\n" +
                "2. Sort audio by name\n" +
                "3. Sort audio by type\n" +
                "4. Sort audio by price\n" +
                "5. Come back\n" +
                "6.Exit");
        System.out.print("\tEnter select: ");
        int choose = sc.nextInt();
        switch (choose) {
            case 1:
                sortAudioById();
                break;
            case 2:
                sortAudioByName();
                break;
            case 3:
                sortAudioByType();
                break;
            case 4:
                sortAudioByPrice();
                break;
            case 5:
                handleAudio(admin);
                break;
            case 6: System.exit(1);
            default:
                break;
        }
    }

    private static void sortAudioById() {
        Collections.sort(audioWarehouse, new Comparator<Audio>() {
            @Override
            public int compare(Audio audio1, Audio audio2) {
                return audio1.getAudioId() - audio2.getAudioId();
            }
        });
        for (Audio audio : audioWarehouse) {
            audio.output();
        }
    }

    private static void sortAudioByPrice() {
        Collections.sort(audioWarehouse, new Comparator<Audio>() {
            @Override
            public int compare(Audio audio1, Audio audio2) {
                return (int) (audio1.getPrice() - audio2.getPrice());
            }
        });
        for (Audio audio : audioWarehouse) {
            audio.output();
        }
    }

    private static void sortAudioByType() {
        Collections.sort(audioWarehouse, new Comparator<Audio>() {
            @Override
            public int compare(Audio audio1, Audio audio2) {
                return audio1.getAudioType().compareTo(audio2.getAudioType());
            }
        });
        for (Audio audio : audioWarehouse) {
            audio.output();
        }
    }

    private static void sortAudioByName() {
        Collections.sort(audioWarehouse, new Comparator<Audio>() {
            @Override
            public int compare(Audio audio1, Audio audio2) {
                return audio1.getAudioName().compareTo(audio2.getAudioName());
            }
        });
        for (Audio audio : audioWarehouse) {
            audio.output();
        }
    }

    private static void deleteAudio() {
        int dem = 0;
        do {
            System.out.print("Enter id audio delete: ");
            int idAudioDelete = sc.nextInt();
            for (int i = 0; i < audioWarehouse.size(); i++) {
                if (audioWarehouse.get(i).getAudioId() == idAudioDelete) {
                    dem++;
                    audioWarehouse.remove(audioWarehouse.get(i));
                    file.UpdateAudioFile(audioWarehouse, "AudioWarehouse.dat");
                    System.out.println("Delete success!!");
                }
            }

            if (dem == 0) {
                System.out.println("id audio invalid");
            }
        } while (dem == 0);
    }

    private static void changeAudio(Account admin) throws ParseException {
        int dem = 0;
        do {
            showWarehouse();
            System.out.print("Enter id audio change: ");
            int idAudioChange = sc.nextInt();
            for (Audio audio : audioWarehouse) {
                if (audio.getAudioId() == idAudioChange) {
                    dem++;
                    do {
                        System.out.println("1. Change audio name\n" +
                                "2. Change audio price\n" +
                                "3. Change audio type\n" +
                                "4. Come back\n" +
                                "5. Exit");
                        System.out.print("\tEnter select: ");
                        sc.nextLine();
                        int choose = sc.nextInt();
                        switch (choose) {
                            case 1:
                                changeAudioName(audio);
                                break;
                            case 2:
                                changeAudioPrice(audio);
                                break;
                            case 3:
                                changeAudioType(audio);
                                break;
                            case 4:
                                handleAudio(admin);
                                break;
                            case 5: System.exit(1);
                            default: break;
                        }
                    } while (true);
                }
            }

            if (dem == 0) {
                System.out.println("id audio invalid");
            }
        } while (dem == 0);
    }

    private static void changeAudioType(Audio audio) {
        sc.nextLine();
        System.out.print("Enter new audio type: ");
        String newAudioType = sc.nextLine();
        audio.setAudioType(newAudioType);
        file.UpdateAudioFile(audioWarehouse, "AudioWarehouse.dat");
    }

    private static void changeAudioPrice(Audio audio) {
        sc.nextLine();
        System.out.print("Enter new audio price: ");
        double newAudioPrice = sc.nextDouble();
        audio.setPrice(newAudioPrice);
        file.UpdateAudioFile(audioWarehouse, "AudioWarehouse.dat");
    }

    private static void changeAudioName(Audio audio) {
        sc.nextLine();
        System.out.print("Enter new audio name: ");
        String newAudioName = sc.nextLine();
        audio.setAudioName(newAudioName);
        file.UpdateAudioFile(audioWarehouse, "AudioWarehouse.dat");
    }

    private static void addAudio() {
        Audio audio = new Audio();
        audio.createAudio();
        file.WriteAudioToFile("AudioWarehouse.dat", audio);
    }

    private static void showWarehouse() {
        audioWarehouse = file.ReadAudiosFromFile("AudioWarehouse.dat");
        for (Audio audio : audioWarehouse) {
            audio.output();
        }
    }

    private static void signUp() throws ParseException {
            System.out.println("1. Admin\n"
                    + "2. User\n"
                    + "3. Come back");
            System.out.print("\tEnter select: ");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    createAccAdmin();
                    break;
                case 2:
                    createAccUser();
                    break;
                case 3: option();
                default: break;
            }
            option();
    }

    private static void createAccUser() throws ParseException {
        Account user = new Account();
        user.createAccUser();
        file.WriteUserToFile("AccountUsers.dat", user);
    }

    private static void createAccAdmin() {
        Account account = new Account();
        account.createAccAdmin();
        file.WriteAccountToFile("AccountAdmins.dat", account);
    }

    public static boolean checkAccount(String username, String password) {
        for (Account acc : accountAdmins) {
            if (username.equals(acc.getUsername()) && password.equals(acc.getPassword())) {
                accountType = 1;
                return true;
            }
        }
        for (Account acc : accountUsers) {
            if (username.equals(acc.getUsername()) && password.equals(acc.getPassword())) {
                accountType = 2;
                return true;
            }
        }
        return false;
    }
}
