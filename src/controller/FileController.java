package controller;

import models.*;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileController{
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    Scanner sc = new Scanner(System.in);

    public void OpenFileToWrite(String fileName) {
        try {
            fileWriter = new FileWriter(fileName, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
        } catch(IOException e) { // catch all IOExceptions not handled by previous catch blocks
            e.printStackTrace();
        }
    }

    public void CloseFileAfterWrite(String fileName) {
        try {
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OpenFileToRead(String fileName) {
        try {
            sc = new Scanner(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CloseFileAfterRead(String fileName) {
        sc.close();
    }

    // Account
    public void WriteAccountToFile(String fileName, Account acc) {
        OpenFileToWrite(fileName);
        printWriter.println(
                acc.getId() + "|" + acc.getUsername() + "|" +
                acc.getPassword() + "|" + acc.getEmail() + "|" +  acc.getPhoneNumber()+ "|" +
                acc.getCreateAt()
        );
        CloseFileAfterWrite(fileName);
    }

    public Account CreateAccountFromData(String data) {
        String[] arr = data.split("\\|");
        Account acc = new Account(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], arr[4], arr[5]);
        return acc;
    }

    public List<Account> ReadAccountFromFile(String fileName) {
        OpenFileToRead(fileName);
        List<Account> list = new ArrayList<>();
        while (sc.hasNext()) { // hasNext(): có phần tử tiếp theo hay không
            String data = sc.nextLine();
            Account acc = CreateAccountFromData(data);
            list.add(acc);
        }
        CloseFileAfterRead(fileName);
        return list;
    }

    public void UpdateAccountFile(List<Account> list, String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }
        for (Account acc : list) {
            WriteAccountToFile(fileName, acc);
        }
    }

    //User
    public void WriteUserToFile(String fileName, Account acc) {
        OpenFileToWrite(fileName);
        String tmpStr = "";
        tmpStr += acc.getId() + "*" + acc.getUsername() + "*" + acc.getPassword() + "*" + acc.getFullName() +
                "*" + acc.getAge() + "*" + acc.getEmail() + "*" +  acc.getPhoneNumber()+ "*" + acc.getUserAddress() +
                "*" + acc.getGender() + "*" + acc.getCurrentMoney() + "*" + acc.getVipTerm() + "*" + acc.getCreateAt();
        for(Audio audio : acc.getAudioBought()) {
            String tmp = audio.getAudioId() + "|" + audio.getAudioName() + "|" + audio.getPrice() + "|" + audio.getAudioCategory() + "|" + audio.getAudioType();
            tmpStr += "*" + tmp;
        }
        printWriter.println(tmpStr);
        CloseFileAfterWrite(fileName);
    }

    public Account CreateUserFromData(String data) {
        String[] arr = data.split("\\*");
        List<Audio> audioList = new ArrayList<>();
        for (int i = 12; i < arr.length; i++) {
            Audio audio = CreateAudioFromData(arr[i]);
            audioList.add(audio);
        }
        Account acc = new Account(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], Integer.parseInt(arr[4]), arr[5], arr[6], arr[7], arr[8], Double.parseDouble(arr[9]), arr[10], arr[11], audioList);
        return acc;
    }

    public List<Account> ReadUserFromFile(String fileName) {
        OpenFileToRead(fileName);
        List<Account> list = new ArrayList<>();
        while (sc.hasNext()) { // hasNext(): có phần tử tiếp theo hay không
            String data = sc.nextLine();
            Account acc = CreateUserFromData(data);
            list.add(acc);
        }
        CloseFileAfterRead(fileName);
        return list;
    }

    public void UpdateUserFile(List<Account> list, String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }
        for (Account user : list) {
            WriteUserToFile(fileName, user);
        }
    }

    // Audio
    public void WriteAudioToFile(String fileName, Audio audio) {
        OpenFileToWrite(fileName);
        printWriter.println(audio.getAudioId() + "|" + audio.getAudioName() + "|" + audio.getPrice() +
                        "|" + audio.getAudioCategory() + "|" + audio.getAudioType()
        );
        CloseFileAfterWrite(fileName);
    }

    public Audio CreateAudioFromData(String data) {
        String[] arr = data.split("\\|");
        Audio audio = new Audio(Integer.parseInt(arr[0]), arr[1], Double.parseDouble(arr[2]), arr[3], arr[4]);
        return audio;
    }

    public List<Audio> ReadAudiosFromFile(String fileName){
        List<Audio> list = new ArrayList<>();
        OpenFileToRead(fileName);
        while(sc.hasNext()){
            String data = sc.nextLine();
            Audio audio = CreateAudioFromData(data);
            list.add(audio);
        }
        CloseFileAfterRead(fileName);
        return list;
    }

    public void UpdateAudioFile(List<Audio> list, String fileName){
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        for(Audio audio : list){
            WriteAudioToFile(fileName, audio);
        }
    }

    // Bill
    public void WriteBillToFile(String fileName, Bill bill) {
        OpenFileToWrite(fileName);
        String tmpBill = "";
        tmpBill += bill.getBillID() + "*" + bill.getDateOfPurchase() + "*" + bill.getUsernameOfBill() + "*" + bill.getTotalMoney();
        List<Audio> audioBuy = bill.getAudioBought();
        for(Audio audio : audioBuy) {
            String tmp = audio.getAudioId() + "|" + audio.getAudioName() + "|" + audio.getPrice() + "|" + audio.getAudioCategory() + "|" + audio.getAudioType();
            tmpBill += "*" + tmp;
        }
        printWriter.println(tmpBill);
        CloseFileAfterWrite(fileName);
    }

    public Bill CreateBillFormData(String data) {
        String [] arr = data.split("\\*");
        List<Audio> audioList = new ArrayList<>();
        for (int i = 4; i < arr.length; i++) {
            Audio audio = CreateAudioFromData(arr[i]);
            audioList.add(audio);
        }
        Bill bill = new Bill(Integer.parseInt(arr[0]), arr[1], arr[2], Double.parseDouble(arr[3]), audioList);
        return bill;
    }

    public List<Bill> ReadBillsFromFile(String fileName) {
        List<Bill> billList = new ArrayList<>();
        OpenFileToRead(fileName);
        while (sc.hasNext()) {
            String data = sc.nextLine();
            Bill bill = CreateBillFormData(data);
            billList.add(bill);
        }
        CloseFileAfterRead(fileName);
        return billList;
    }

    public void UpdateBillFile(List<Bill> list, String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        for (Bill bill : list) {
            WriteBillToFile(fileName, bill);
        }
    }

    // Bill Vip
    public void WriteBillVipToFile(String fileName, Bill bill) {
        OpenFileToWrite(fileName);
        String tmpBill = "";
        tmpBill += bill.getBillID() + "*" + bill.getDateOfPurchase() + "*" + bill.getUsernameOfBill() + "*" + bill.getTimeVip() + "*" + bill.getTotalMoney();
        printWriter.println(tmpBill);
        CloseFileAfterWrite(fileName);
    }

    public Bill CreateBillVipFromData(String data) {
        String[] arr = data.split("\\*");
        Bill bill = new Bill(Integer.parseInt(arr[0]), arr[1], arr[2], Integer.parseInt(arr[3]), Double.parseDouble(arr[4]));
        return bill;
    }

    public List<Bill> ReadBillsVipFromFile(String fileName) {
        List<Bill> billVipList = new ArrayList<>();
        OpenFileToRead(fileName);
        while (sc.hasNext()) {
            String data = sc.nextLine();
            Bill bill = CreateBillVipFromData(data);
            billVipList.add(bill);
        }
        CloseFileAfterRead(fileName);
        return billVipList;
    }

    public void UpdateBillVipFile(List<Bill> list, String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        for (Bill bill : list) {
            WriteBillVipToFile(fileName, bill);
        }
    }

    // Bill top up money
    public void WriteBillRechargeToFile(String fileName, Bill bill) {
        OpenFileToWrite(fileName);
        String tmpBill = "";
        tmpBill += bill.getBillID() + "*" + bill.getDateOfPurchase() + "*" + bill.getUsernameOfBill() + "*" + bill.getTotalMoney();
        printWriter.println(tmpBill);
        CloseFileAfterWrite(fileName);
    }

    public Bill CreateBillRechargeFromData(String data) {
        String[] arr = data.split("\\*");
        Bill bill = new Bill(Integer.parseInt(arr[0]), arr[1], arr[2], Double.parseDouble(arr[3]));
        return bill;
    }

    public List<Bill> ReadBillsRechargeFromFile(String fileName) {
        List<Bill> billRechargeList = new ArrayList<>();
        OpenFileToRead(fileName);
        while (sc.hasNext()) {
            String data = sc.nextLine();
            Bill bill = CreateBillRechargeFromData(data);
            billRechargeList.add(bill);
        }
        CloseFileAfterRead(fileName);
        return billRechargeList;
    }

    public void UpdateBillRechargeFile(List<Bill> list, String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        for (Bill bill : list) {
            WriteBillRechargeToFile(fileName, bill);
        }
    }

    // Notification
    public void WriteMessageToFile(String fileName, Message mess) {
        OpenFileToWrite(fileName);
        printWriter.println(mess.getIdMessage() + "*" + mess.getMessage() + "*" + mess.getCreateAt() + "*" + mess.getStatus());
        CloseFileAfterWrite(fileName);
    }

    public Message CreateMessageFromData(String data) {
        String[] arr = data.split("\\*");
        Message mess = new Message(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3]);
        return mess;
    }

    public List<Message> ReadMessageFromFile(String fileName) {
        List<Message> messageList = new ArrayList<>();
        OpenFileToRead(fileName);
        while (sc.hasNext()) {
            String data = sc.nextLine();
            Message message = CreateMessageFromData(data);
            messageList.add(message);
        }
        CloseFileAfterRead(fileName);
        return messageList;
    }

    public void UpdateMessageToFile(List<Message> messList, String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        for (Message mess : messList) {
            WriteMessageToFile(fileName, mess);
        }
    }



}

