package com.example.videofilerepository;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.List;

// Інтерфейсний клас для оголошення методів виводу та вводу інформції файлів
interface ReaderWriterFileInterface {
    void readingFromFile(String readFilePath) throws IOException;

    boolean writingToFile(String readFilePath);
}

/*
 * Клас для полегшення роботи зі списками відеофайлів
 */
public class FileListManager implements ReaderWriterFileInterface {
    // Список для редагування інформації
    private List<VideoFile> videoFiles;
    // Доступні формати відеофайлів
    private final String[] formatsStr = {"MP4", "MOV", "FLV", "WMV"};
    // Доступні відеокодеки
    private final String[] videoCodecsStr = {"H.264/AVC", "H.265/HEVC", "AV1", "VP9", "H.266/VVC"};
    // Доступні аудіокодеки
    private final String[] audioCodecsStr = {"G.711", "FLAC", "AMR", "ALAC", "AAC"};
    // Доступні відеоплеєри
    private final String[] playersStr = {"GOM.Player", "Kodi", "PotPlayer", "VLC.MediaPlayer"};
    // Максимально можливе значення розміру та тривалості файлів
    private final static int maxSize = 999999;

    // Очищення списку
    public void deleteResources() {
        this.videoFiles.clear();
    }

    // Базовий конструктор
    public FileListManager() {
        videoFiles = new ArrayList<>();
    }

    // Геттер для максимального розміру
    public int getMaxSize() {
        return maxSize;
    }

    // Геттер для поточного списку
    public List<VideoFile> getList() {
        return videoFiles;
    }

    // Суттер для поточного списку
    public void setList(List<VideoFile> list) {
        videoFiles = list;
    }

    // Метод зчитування інформації з файлу
    @Override
    public void readingFromFile(String readFilePath) {
        File readFile = new File(readFilePath);
        try (BufferedReader br = new BufferedReader(new FileReader(readFile))) {
            String line;
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                try {
                    lineCounter++;
                    String[] parts = line.split(" ");
                    String strName = parts[0];
                    validateCyrillicInput(strName);
                    String strPath = parts[1];
                    String strFormat = parts[2];
                    if (!Arrays.asList(formatsStr).contains(parts[2])) {
                        throw new NumberFormatException("The format must be like in the list (MP4, MOV, FLV, WMV): "
                                + lineCounter + " line");
                    }
                    double lDuration = Double.valueOf(parts[3]);
                    if (lDuration < 0 || lDuration > maxSize) {
                        throw new NumberFormatException("Duration must be > 0 and < 999999: " + lineCounter + " line");
                    }
                    String strVCodec = parts[4];
                    if (!Arrays.asList(videoCodecsStr).contains(parts[4])) {
                        throw new NumberFormatException("Video codec must be like in the list (H.264/AVC, " +
                                "H.265/HEVC, AV1, VP9, H.266/VVC): " + lineCounter + " line");
                    }
                    String strACodec = parts[5];
                    if (!Arrays.asList(audioCodecsStr).contains(parts[5])) {
                        throw new NumberFormatException("Audio codec must be like in the list " +
                                "(G.711, FLAC, AMR, ALAC, AAC): " + lineCounter + " line");
                    }

                    if (!parts[6].equals("true") && !parts[6].equals("false")) {
                        throw new NumberFormatException("Subtitles exists must be 'true' or 'flase': "
                                + lineCounter + " line");
                    }
                    Boolean boolSubtitles = Boolean.valueOf(parts[6]);
                    double lSize = Double.valueOf(parts[7]);
                    if (lSize < 0 || lSize > maxSize) {
                        throw new NumberFormatException("Size must be > 0 and < 999999: " + lineCounter + " line");
                    }
                    String strPlayer = parts[8];
                    if (!Arrays.asList(playersStr).contains(parts[8])) {
                        throw new NumberFormatException("Video player must be like in the list (GOM.Player, Kodi, " +
                                "PotPlayer, VLC.MediaPlayer): " + lineCounter + " line");
                    }

                    VideoFile videoFile = new VideoFile(strName, strPath, strFormat, lDuration, strVCodec,
                            strACodec, boolSubtitles, lSize, strPlayer);
                    videoFiles.add(videoFile);
                } catch (NumberFormatException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Перевірка на кириличні символи
    public boolean validateCyrillicInput(String input) {
        // Вираз для перевірки кириличних літер
        String cyrillicPattern = ".*[А-Яа-яЁё].*";

        if (Pattern.matches(cyrillicPattern, input)) {
            throw new IllegalArgumentException("Input contain Cyrillic letter");
        } else {
            return true;
        }
    }

    // Метод запису інформації у файлу
    @Override
    public boolean writingToFile(String readFilePath) {
        if (videoFiles.isEmpty()) {
            return false;
        }
        File writeFile = new File(readFilePath);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(writeFile))) {
            for (VideoFile videoFile : videoFiles) {
                String content = videoFile.getName() + " "
                        + videoFile.getPath() + " "
                        + videoFile.getFormat() + " "
                        + videoFile.getDuration() + " "//"sec "
                        + videoFile.getVideoCodec() + " "
                        + videoFile.getAudioCodec() + " "
                        //+ "Subtitles: " + videoFile.getSubtitles() + " "
                        + videoFile.getSubtitles() + " "
                        + videoFile.getSize() + " "//"mb "
                        + videoFile.getPlayer();
                br.write(content);
                br.newLine();
                System.out.println("True");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Метод видалення елемента за назвою
    public boolean deleteByName(String name) throws VideoNotFoundException {
        Iterator<VideoFile> iterator = videoFiles.iterator();
        while (iterator.hasNext()) {
            VideoFile videoFile = iterator.next();
            if (videoFile.getName().equals(name)) {
                iterator.remove();
                return true;
            }
        }
        throw new VideoNotFoundException("Video with name '" + name + "' not found.");
    }

    // Методи пошуку елементів

    // Метод пошуку за тривалістю
    public FileListManager findByDuration(double dur) throws VideoNotFoundException {
        FileListManager findList = new FileListManager();
        for (VideoFile node : videoFiles) {
            if (node.getDuration() == dur) {
                findList.getList().add(node);
            }
        }
        if (findList.getList().isEmpty()) {
            throw new VideoNotFoundException("Video with duration '" + dur + "' not found.");
        }
        return findList;
    }

    // Метод пошуку за шляхом
    public FileListManager findByPath(String path) throws VideoNotFoundException {
        FileListManager findList = new FileListManager();
        for (VideoFile node : videoFiles) {
            if (node.getPath().equals(path)) {
                findList.getList().add(node);
            }
        }
        if (findList.getList().isEmpty()) {
            throw new VideoNotFoundException("Video with path '" + path + "' not found.");
        }
        return findList;
    }

    // Методи для групування та сортування

    // Метод групування у елементів за відео або аудіок кодеками з використанням Map та stream
    public Map<String, List<VideoFile>> groupItemsByCodec(FileListManager items, int groupingType) {
        try {
            if (groupingType == 0) {
                return items.getList().stream().collect(Collectors.groupingBy(
                        VideoFile::getVideoCodec,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
            } else {
                return items.getList().stream().collect(Collectors.groupingBy(
                        VideoFile::getAudioCodec,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error grouping items by codec", e);
        }
    }

    // Метод сортування окремих груп
    public void sortGroupedElements(Map<String, List<VideoFile>> mapElements) {
        try {
            mapElements.forEach((category, itemList) -> {
                List<VideoFile> sortedItems = itemList.stream()
                        .sorted(Comparator.comparing(VideoFile::getDuration).reversed())
                        .collect(Collectors.toList());
                mapElements.put(category, sortedItems);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error sort items by codecs group", e);
        }
    }

    // Метод переведення Map у список
    public List<VideoFile> mapToListConvert(Map<String, List<VideoFile>> groupedItems) {
        try {
            return groupedItems.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error sort items by codecs group", e);
        }
    }

    // Методи для знаходження найдовших елементів у списку

    // Метод групування за субтитрами
    public FileListManager groupBySubtitles(boolean subtitlesExist) {
        FileListManager subtitlesFilesList;
        try {
            subtitlesFilesList = new FileListManager();
            for (VideoFile vf : videoFiles) {
                if (vf.getSubtitles() == subtitlesExist) {
                    subtitlesFilesList.getList().add(vf);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return subtitlesFilesList;
    }

    // Метод для зназодження найбільших значення
    public FileListManager findLongestFilesList(boolean subtitlesExist, int number) {
        FileListManager longestFilesList;
        try {
            longestFilesList = groupBySubtitles(subtitlesExist);
            longestFilesList.setList(findLongestNObjects(longestFilesList, number));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return longestFilesList;
    }

    // Метод для зназодження найбільших значення, додавання їх до списку та сортування за спаданням
    public List<VideoFile> findLongestNObjects(FileListManager fm, int n) {
        List<VideoFile> newList;
        try {
            newList = fm.getList().stream()
                    .sorted(Comparator.comparingDouble(VideoFile::getDuration).reversed()
                            .thenComparing(VideoFile::getName))
                    .limit(n)
                    .collect(Collectors.toList());
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return newList;
    }

    // Методи для знаходження найбільших та найменших значень

    // Метод групування за форматом
    public FileListManager groupByFormat(String form) {
        FileListManager newList;
        try {
            newList = new FileListManager();
            for (VideoFile videoFile : videoFiles) {
                if (videoFile.getFormat().equals(form)) {
                    newList.getList().add(videoFile);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return newList;
    }

    // Метод знаходження значень та запису їх у новий список
    public FileListManager findMinMaxSize() {
        FileListManager newList = new FileListManager();
        try {
            List<VideoFile> list = getList();
            if (list != null && !list.isEmpty()) {
                VideoFile minSize = new VideoFile(findMinSize(list));
                VideoFile maxSize = new VideoFile(findMaxSize(list));
                newList.getList().add(maxSize);
                newList.getList().add(minSize);
            } else {
                throw new EmptyListException("The list is either null or empty");
            }
        } catch (NullPointerException | EmptyListException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return newList;
    }

    // Метод знаходження мінімального значення
    private VideoFile findMinSize(List<VideoFile> videoFiles) {
        VideoFile minSizeElem = new VideoFile(videoFiles.get(0));
        for (VideoFile videoFile : videoFiles) {
            if (videoFile.getSize() < minSizeElem.getSize()) {
                minSizeElem = new VideoFile(videoFile);
            }
        }
        return minSizeElem;
    }

    // Метод знаходження максимального значення
    private VideoFile findMaxSize(List<VideoFile> videoFiles) {
        VideoFile maxSizeElem = new VideoFile(videoFiles.get(0));
        for (VideoFile videoFile : videoFiles) {
            if (videoFile.getSize() > maxSizeElem.getSize()) {
                maxSizeElem = new VideoFile(videoFile);
            }
        }
        return maxSizeElem;
    }

    // Метод сортування за Counting sort
    public FileListManager countingSort(int charSizeSort) {
        VideoFile[] result = new VideoFile[videoFiles.size()];

        int range = (int) Math.pow(255, charSizeSort);
        int countArr[] = new int[range];

        for (int i = 0; i < videoFiles.size(); i++) {
            int index = 0;
            for (int j = 0; j < charSizeSort; j++) {
                char currentChar = videoFiles.get(i).getName().charAt(charSizeSort - j - 1);
                int charIndex = (int) currentChar;
                int temp = (int) Math.pow(255, j);
                index += charIndex * temp;
            }
            countArr[index]++;
        }

        for (int i = 1; i < range; i++) {
            countArr[i] += countArr[i - 1];
        }

        for (int i = videoFiles.size() - 1; i >= 0; i--) {
            int index = 0;
            for (int j = 0; j < charSizeSort; j++) {
                char currentChar = videoFiles.get(i).getName().charAt(charSizeSort - j - 1);
                int charIndex = (int) currentChar;
                int temp = (int) Math.pow(255, j);
                index += charIndex * temp;
            }
            result[countArr[index] - 1] = videoFiles.get(i);
            countArr[index]--;
        }

        FileListManager newList = new FileListManager();
        for (int i = 0; i < videoFiles.size(); i++) {
            newList.getList().add(result[i]);
        }
        return newList;
    }
}
