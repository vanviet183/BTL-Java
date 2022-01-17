package models;

import controller.FileController;

import java.util.List;
import java.util.Scanner;

public class Audio {
    private int audioId;
    private String audioName;
    private double price;
    private String audioCategory;
    private String audioType;
    static int idAuto = 1;

    static FileController file = new FileController();
    static List<Audio> audioWarehouse = file.ReadAudiosFromFile("AudioWarehouse.dat");
    Scanner sc = new Scanner(System.in);

    public Audio() {

    }

    public Audio(int audioId, String audioName, double price, String audioCategory, String audioType) {
        this.audioId = audioId;
        this.audioName = audioName;
        this.price = price;
        this.audioCategory = audioCategory;
        this.audioType = audioType;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public String getAudioCategory() {
        return audioCategory;
    }

    public void setAudioCategory(String audioCategory) {
        this.audioCategory = audioCategory;
    }

    public void createAudio() {
        do {
            audioId = idAuto++;
        } while (checkIdAudio(audioId));

        do {
            System.out.print("Enter audio name: ");
            audioName = sc.nextLine();
        } while (checkNameAudio(audioName));
        System.out.print("Enter price audio: ");
        price = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter audio category: ");
        audioCategory = sc.nextLine();
        System.out.print("Enter audio type: ");
        audioType = sc.nextLine();
    }

    public void output() {
        System.out.println("audioId = " + audioId +
                            ", audioName = " + audioName +
                            ", price = " + price + "$" +
                            ", audioCategory = " + audioCategory +
                            ", audioType = " + audioType);
    }

    public boolean checkIdAudio(int audioId) {
        for(Audio audio : audioWarehouse) {
            if(audio.getAudioId() == audioId){
                return true;
            }
        }
        return false;
    }

    public boolean checkNameAudio(String name) {
        for(Audio audio : audioWarehouse) {
            if(audio.getAudioName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
