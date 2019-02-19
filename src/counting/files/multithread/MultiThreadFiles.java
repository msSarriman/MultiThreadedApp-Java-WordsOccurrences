/**
 * This program, given two files, creates 2 threads to count the words on each file.
 * Then creates a third thread, to sum the words occurences on each file and print out as:
 * <word> <total occurrences> = <occurrences in file1> + <occurrences in file2>
 * ===============================
 * Time Complexity : O(NlogN), where N is the file with the most words (logN due to sorting).
 */

package counting.files.multithread;

import java.io.*;
import java.util.*;

public class MultiThreadFiles implements Runnable {
    static String OPERATING_SYSTEM;
    private String FILE_PATH;
    volatile static HashMap<String, Integer> myMap = new HashMap<>(); // cache
    volatile static ArrayList<String> listFileNames = new ArrayList<>();


    /**
     * Default constructor
     * used to get OPERATING_SYSTEM faster.
     */
    private MultiThreadFiles() {
        CheckOS cos = new CheckOS();
        OPERATING_SYSTEM = cos.getOS();
    }


    /**
     * Overloaded constructor
     *
     * @param path Instances will become threads, reading this.FILE_PATH file.
     */
    private MultiThreadFiles(String path) {
        this.FILE_PATH = path;
    }


    /**
     * Method that returns the filename out of an absolute path.
     *
     * @param str
     * @return
     */
    private String extractFileName(String str) {
        int index = 0;
        if (OPERATING_SYSTEM.equals("win")) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '\\') index = i;
            }
            return str.substring(index + 1);
        } else if (OPERATING_SYSTEM.equals("linux") || OPERATING_SYSTEM.equals("mac")) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '/') index = i;
            }
            return str.substring(index + 1);
        }
        return null;
    }


    /**
     * Every thread of the run() will read a file and count its words occurrences, saving them into static myMap
     */
    @Override
    public void run() {
        File file = new File(FILE_PATH);
        String fileName = extractFileName(FILE_PATH);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                for (String str : line.trim().replace(".", " ").split(" ")) {
                    if (str.equals("")) continue;
                    String tempKey = fileName + "___" + str;
                    if (!myMap.containsKey(tempKey)) myMap.put(tempKey, 1);
                    else myMap.put(tempKey, myMap.get(tempKey) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method that turns the relative path to absolute
     *
     * @param str
     * @return
     */
    private static String relativeToAbsolute(String str) {
        // Get the Operating System
        MultiThreadFiles obj = new MultiThreadFiles();
        String tempStr = str;
        listFileNames.add(tempStr);
        if (OPERATING_SYSTEM.equals("win"))
            if (!str.contains(":\\")) tempStr = System.getProperty("user.dir") + "\\" + str;
            else if (OPERATING_SYSTEM.equals("linux") || OPERATING_SYSTEM.equals("mac"))
                if (str.indexOf("/") != 0) tempStr = System.getProperty("user.dir") + "\\" + str;
                else
                    return "SO_SORRY_MATE";
        return tempStr;
    }


    public static void main(String[] args) throws InterruptedException {
        // Get the paths
        String file1path, file2path;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first file's path (absolute or relative):");
        file1path = relativeToAbsolute(sc.nextLine());
        System.out.print("Enter second file's path (absolute or relative):");
        file2path = relativeToAbsolute(sc.nextLine());


        // Create the runnable objects to produce the cache
        MultiThreadFiles runner1 = new MultiThreadFiles(file1path);
        MultiThreadFiles runner2 = new MultiThreadFiles(file2path);
        Thread thread1 = new Thread(runner1);
        Thread thread2 = new Thread(runner2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Create the final runnable to print the results
        MultiThreadFiles finalRunner = new MultiThreadFiles();
        Thread threadFinal = new Thread(finalRunner) {
            @Override
            public void run() {
                HashMap<String, Integer> totalOccurrences = new HashMap<>();
                ArrayList<ListOfData> lodAl = new ArrayList<>();
                // Calculate the total occurences
                for (Map.Entry<String, Integer> words : myMap.entrySet()) {
                    String tempWord = (words.getKey().split("___"))[1];
                    if (!totalOccurrences.containsKey(tempWord)) totalOccurrences.put(tempWord, words.getValue());
                    else totalOccurrences.put(tempWord, totalOccurrences.get(tempWord) + words.getValue());
                }
                // Create the final result structure for the stdout
                for (Map.Entry<String, Integer> words : totalOccurrences.entrySet()) {
                    //String words.getKey() = (words.getKey().split("___"))[1];
                    if (myMap.containsKey(listFileNames.get(0) + "___" + words.getKey())
                            && myMap.containsKey(listFileNames.get(1) + "___" + words.getKey())) {
                        lodAl.add(new ListOfData(
                                totalOccurrences.get(words.getKey()),
                                words.getKey(),
                                myMap.get(listFileNames.get(0) + "___" + words.getKey()),
                                myMap.get(listFileNames.get(1) + "___" + words.getKey())));
                    } else if (myMap.containsKey(listFileNames.get(0) + "___" + words.getKey())
                            && !myMap.containsKey(listFileNames.get(1) + "___" + words.getKey())) {
                        lodAl.add(new ListOfData(
                                totalOccurrences.get(words.getKey()),
                                words.getKey(),
                                myMap.get(listFileNames.get(0) + "___" + words.getKey()),
                                0));
                    } else if (!myMap.containsKey(listFileNames.get(0) + "___" + words.getKey())
                            && myMap.containsKey(listFileNames.get(1) + "___" + words.getKey())) {
                        lodAl.add(new ListOfData(
                                totalOccurrences.get(words.getKey()),
                                words.getKey(),
                                0,
                                myMap.get(listFileNames.get(1) + "___" + words.getKey())));
                    } else {
                        lodAl.add(new ListOfData(
                                totalOccurrences.get(words.getKey()),
                                words.getKey(),
                                0,
                                0));
                    }
                }
                // sort the results and print
                Collections.sort(lodAl);
                for (ListOfData l : lodAl)
                    System.out.print(l);
            }
        };
        threadFinal.start();
        threadFinal.join();
    }
}
