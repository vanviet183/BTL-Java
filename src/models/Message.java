package models;

import controller.FileController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Message {
    private int idMessage;
    private String message;
    private String createAt;
    private String status;
    static int idAuto = 1;
    static FileController file = new FileController();
    static List<Message> notifyList = file.ReadMessageFromFile("Notify.dat");


    static Scanner sc = new Scanner(System.in);
    public Message() {

    }

    public Message(int idMessage, String message, String createAt, String status) {
        this.idMessage = idMessage;
        this.message = message;
        this.createAt = createAt;
        this.status = status;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void createMessage() {
        do {
            idMessage = idAuto++;
        } while (checkIdMessage(idMessage));
        System.out.print("Enter message: ");
        message = sc.nextLine();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        createAt = dateFormat.format(date);
        status = "not seen";
    }

    public void output() {
        System.out.println(idMessage + " Notify: " + message + " (" + createAt +")");
    }

    public boolean checkIdMessage(int idMess) {
        for (Message mess : notifyList) {
            if(mess.getIdMessage() == idMess) {
                return true;
            }
        }
        return false;
    }
}
