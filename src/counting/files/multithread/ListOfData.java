package counting.files.multithread;

import java.util.ArrayList;

public class ListOfData implements Comparable{
    protected static ArrayList<ListOfData> myList = new ArrayList<>(); //useless
    int data;
    String word;
    int occFile1;
    int occFile2;


    /**
     * Overloaded constructor for creation of new nodes
     * @param data
     * @param word
     * @param occFile1
     * @param occFile2
     */
    ListOfData(int data, String word, int occFile1, int occFile2){
        addNode(data, word, occFile1, occFile2);
        ListOfData node = this;
        myList.add(node);
    }


    /**
     * Utility function
     * @param data
     * @param word
     * @param occFile1
     * @param occFile2
     */
    private void addNode(int data, String word, int occFile1, int occFile2){
        this.data = data;
        this.word = word;
        this.occFile1 = occFile1;
        this.occFile2 = occFile2;
    }


    /**
     * Collections.sort implementation on Class's elements
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        int compare = ((ListOfData)o).getData();
        return compare - this.getData();
    }


    private int getData(){
        return this.data;
    }


    @Override
    public String toString() {
        return String.format("%s\t%d\t=\t%d\t+\t%d\n", this.word, getData(), this.occFile1, this.occFile2);
    }
}
