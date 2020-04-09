import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Random;

public class Vertex extends Button {

    private Vertex parent = null;
    private ArrayList<Vertex> children = null;
    private static char ch = 'a';
    private static int count = 0;
    public int stage = 1;
    public int ID;
    public char weightID;
    private String state = "CANDIDATE";
    private ArrayList<Integer> vertexNumberList = new ArrayList<Integer>();

    public Vertex(Double x, Double y, ArrayList<Integer> list) {
        setLayoutX(x);
        setLayoutY(y);

        this.vertexNumberList = list;

        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));

        weightID = (char) ch++;
        ID = randInt(0, 10);
        if(vertexNumberList.contains(ID)){
            while(vertexNumberList.contains(ID))
            ID = randInt(0, 10);
        }
        vertexNumberList.add(ID);
        setText(weightID + ":" + ID + "");
        getStyleClass().add("visNode");
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public int getID() {
        return this.ID;
    }

    public void setParent(Vertex vertex) {
        this.parent = vertex;
    }

//    public void setChildren(Vertex vertex){
//        this.children.add(vertex);
//    }
//
//    public void removeChildren(Vertex vertex){
//        this.children.remove(vertex);
//    }

    public Vertex gettheParent() {
        return this.parent;
    }

//    public ArrayList<Vertex> gettheChildren(){
//        return this.children;
//    }

    public char getWeightID() {
        return this.weightID;
    }


    public void setState(String newState) {
        this.state = newState;
    }

    public String getState() {
        return this.state;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage() {
        this.stage++;
    }

    public ArrayList<Integer> getNumberList(){
        return this.vertexNumberList;
    }
}
