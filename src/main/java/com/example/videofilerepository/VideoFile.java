package com.example.videofilerepository;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/* Клас для запису мультимедійної інформації про відео файл */
public class VideoFile {
    // Створення полів-змінних класу
    private String name;
    private String filePath;
    private String format;
    private double durationInSeconds;
    private String videoCodec;
    private String audioCodec;
    private Boolean subtitles;
    private double size;
    private String player;

    // Реалізація конструкторів

    // Конструктор за замовчуванням
    public VideoFile() {
        this.name = "Name";
        this.filePath = "C:/";
        this.format = "MP4";
        this.durationInSeconds = 0;
        this.videoCodec = "H.264/AVC";
        this.audioCodec = "G.711";
        this.subtitles = false;
        this.size = 0;
        this.player = "Kodi";
    }

    // Конструктор з параметрами
    public VideoFile(String nam, String path, String form, double dur, String vCod, String vAud, Boolean sub,
                     double siz, String play) {
        this.name = nam;
        this.filePath = path;
        this.format = form;
        this.durationInSeconds = dur;
        this.videoCodec = vCod;
        this.audioCodec = vAud;
        this.subtitles = sub;
        this.size = siz;
        this.player = play;
    }

    // Конструктор копій
    public VideoFile(VideoFile other) {
        this.name = other.name;
        this.filePath = other.filePath;
        this.format = other.format;
        this.durationInSeconds = other.durationInSeconds;
        this.videoCodec = other.videoCodec;
        this.audioCodec = other.audioCodec;
        this.subtitles = other.subtitles;
        this.size = other.size;
        this.player = other.player;
    }

    // Реалізація гетерів
    public String getName() {
        return name;
    }

    public String getPath() {
        return filePath;
    }

    public String getFormat() {
        return format;
    }

    public double getDuration() {
        return durationInSeconds;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public Boolean getSubtitles() {
        return subtitles;
    }

    public String getStrBoolSubtitles() {
        if (this.subtitles) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public double getSize() {
        return size;
    }

    public String getPlayer() {
        return player;
    }

    // Реалізація сеттерів
    public void setName(String name) {
        this.name = name;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDurationInSeconds(double durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public void setSubtitles(Boolean subtitles) {
        this.subtitles = subtitles;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    // Метод вводу інформації з консолі
    public static VideoFile inputFromConsole() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Name: ");
            String nameInp = scanner.nextLine();
            FileListManager fm = new FileListManager();
            fm.validateCyrillicInput(nameInp);

            System.out.print("Enter Path: ");
            String filePathInp = scanner.nextLine();

            System.out.print("Enter Format: ");
            String formatInp = scanner.nextLine();

            System.out.print("Enter Duration (in seconds): ");
            double durationInSecondsInp = scanner.nextDouble();
            if (durationInSecondsInp < 0 || durationInSecondsInp > 999999) {
                throw new NumberFormatException("Duration must be > 0 and < 999999");
            }
            scanner.nextLine();

            System.out.print("Enter Video Codec: ");
            String videoCodecInp = scanner.nextLine();

            System.out.print("Enter Audio Codec: ");
            String audioCodecInp = scanner.nextLine();

            System.out.print("Enter Subtitles (true/false): ");
            boolean subtitlesInp = scanner.nextBoolean();
            scanner.nextLine();

            System.out.print("Enter Size: ");
            double sizeInp = scanner.nextDouble();
            scanner.nextLine();
            if (sizeInp < 0 || sizeInp > 999999) {
                throw new NumberFormatException("Size must be > 0 and < 999999");
            }

            System.out.print("Enter Player: ");
            String playerInp = scanner.nextLine();

            return new VideoFile(nameInp, filePathInp, formatInp, durationInSecondsInp, videoCodecInp, audioCodecInp,
                    subtitlesInp, sizeInp, playerInp);
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Error: Invalid input format");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Метод виводу інформації в консоль
    @Override
    public String toString() {
        return ("Name: " + name + " Path: " + filePath + " Format: " + format + " Duration: " + durationInSeconds +
                " Video Codec: " + videoCodec + " Audio Codec: " + audioCodec + " Size: " + size +
                " Player: " + player);
    }
}
