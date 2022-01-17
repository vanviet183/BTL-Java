package models;

import controller.FileController;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class Bill {
    private int billID;
    private String dateOfPurchase;
    private String usernameOfBill;
    private double totalMoney;
    private int timeVip;
    private List<Audio> audioBought;

    static int idBillAuto = 1;

    static FileController file = new FileController();
    static List<Bill> billList = file.ReadBillsFromFile("Bills.dat");
    static List<Bill> billVipList = file.ReadBillsVipFromFile("BillsVip.dat");
    static List<Bill> billRechargeList = file.ReadBillsRechargeFromFile("BillsRecharge.dat");
    public Bill() {

    }

    public Bill(int billID, String dateOfPurchase, String usernameOfBill, double totalMoney, List<Audio> audioBought) {
        this.billID = billID;
        this.dateOfPurchase = dateOfPurchase;
        this.usernameOfBill = usernameOfBill;
        this.totalMoney = totalMoney;
        this.audioBought = audioBought;
    }

    public Bill(int billID, String dateOfPurchase, String userNameOfBill, int timeVip, double totalMoney) {
        this.billID = billID;
        this.dateOfPurchase = dateOfPurchase;
        this.usernameOfBill = userNameOfBill;
        this.timeVip = timeVip;
        this.totalMoney = totalMoney;
    }

    public Bill(int billID, String dateOfPurchase, String userNameOfBill, double totalMoney) {
        this.billID = billID;
        this.dateOfPurchase = dateOfPurchase;
        this.usernameOfBill = userNameOfBill;
        this.totalMoney = totalMoney;
    }


    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getUsernameOfBill() {
        return usernameOfBill;
    }

    public void setUsernameOfBill(String usernameOfBill) {
        this.usernameOfBill = usernameOfBill;
    }

    public int getTimeVip() {
        return timeVip;
    }

    public void setTimeVip(int timeVip) {
        this.timeVip = timeVip;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<Audio> getAudioBought() {
        return audioBought;
    }

    public void setAudioBought(List<Audio> audioBought) {
        this.audioBought = audioBought;
    }

    public void createBill(Account user) {
        do {
            billID = idBillAuto++;
        } while (checkIdBill(billID, billList));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        dateOfPurchase = dateFormat.format(date);
        usernameOfBill = user.getUsername();
        totalMoney = 0;
        for (Audio audio : audioBought) {
            totalMoney += audio.getPrice();
        }
    }

    public void createBillVip(Account user, double price, int time) {
        do {
            billID = idBillAuto++;
        } while (checkIdBill(billID, billVipList));
        Date date =  new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        dateOfPurchase = dateFormat.format(date);
        usernameOfBill = user.getUsername();
        totalMoney = price;
        timeVip = time;
    }

    public void createBillUser(Account user, double price) {
        do {
            billID = idBillAuto++;
        } while (checkIdBill(billID, billRechargeList));
        Date date =  new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        dateOfPurchase = dateFormat.format(date);
        usernameOfBill = user.getUsername();
        totalMoney = price;
    }

    public void outputBillRecharge() {
        System.out.println("\t\t\t\tBILL TOP UP MONEY");
        System.out.println("User buy: " + usernameOfBill + "        " + "Date: " + dateOfPurchase);
        System.out.println("Add: +" + totalMoney);
        System.out.println("Total money: " + totalMoney + "$");
    }

    public void output() {
        System.out.println("\t\t\t\tBILL BUY AUDIO");
        System.out.println("User buy: " + usernameOfBill + "        " + "Date: " + dateOfPurchase);
        System.out.println("List audio bought: ");
        for (Audio audio : audioBought) {
            audio.output();
        }
        System.out.println("Quantity: " + audioBought.size());
        System.out.println("Total money: " + totalMoney + "$");
    }

    public void outputBillVip() {
        System.out.println("\t\t\t\tBILL BUY VIP");
        System.out.println("User buy: " + usernameOfBill + "        " + "Date: " + dateOfPurchase);
        System.out.println("Buy time vip: " + timeVip + " days");
        System.out.println("Total money: " + totalMoney + "$");
    }


    public boolean checkIdBill(int billId, List<Bill> list) {
        for (Bill bill : list) {
            if(bill.getBillID() == billId) {
                return true;
            }
        }
        return false;
    }

}
