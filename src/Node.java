import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Random;

// node class

public class Node extends Button {

    private Node parent = null;
    private ArrayList<Node> children = null;
    public static char ch = 'a';
    private static int count = 0;
    public int stage = 1;
    public int ID;
    public char weightID;
    private String state = "CANDIDATE";
    private ArrayList<Integer> nodeNumberList = new ArrayList<Integer>();

    public Node(Double x, Double y, ArrayList<Integer> list) {
        setLayoutX(x);
        setLayoutY(y);

        this.nodeNumberList = list;

        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));

        weightID = (char) ch++;
        ID = randInt(0, 10);

        //Determine whether the random number is repeated
        if (nodeNumberList.contains(ID)) {
            while (nodeNumberList.contains(ID))
                ID = randInt(0, 10);
        }
        nodeNumberList.add(ID);
        setText(weightID + ":" + ID + "");
        getStyleClass().add("visNode");
    }

    //Generate a random number
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    //get the number in the node
    public int getID() {
        return this.ID;
    }

    //Set parent node
    public void setParent(Node node) {
        this.parent = node;
    }

//    public void setChildren(Node node){
//        this.children.add(node);
//    }
//
//    public void removeChildren(Node node){
//        this.children.remove(node);
//    }

    public Node gettheParent() {
        return this.parent;
    }

//    public ArrayList<Node> gettheChildren(){
//        return this.children;
//    }

    //get the character of the node
    public char getWeightID() {
        return this.weightID;
    }

    //set and get the state of the node now
    public void setState(String newState) {
        this.state = newState;
    }

    public String getState() {
        return this.state;
    }

    //set and get the stage of the node now
    public int getStage() {
        return this.stage;
    }

    public void setStage() {
        this.stage++;
    }

    public ArrayList<Integer> getNumberList() {
        return this.nodeNumberList;
    }
}
