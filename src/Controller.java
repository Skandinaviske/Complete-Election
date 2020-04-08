import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class Controller {

    @FXML
    AnchorPane graph;
    @FXML
    TextField attackerField;
    @FXML
    TextField attackedField;
    @FXML
    Button submit;
    @FXML
    TextArea console;

    Vertex vertex1;
    Vertex vertex2;
    //Vertex vertexTemp;
    Vertex vertexDelete;

    Arrow arrow;
    ArrayList<Vertex> vertexGroup = new ArrayList<Vertex>();

    Map<Integer, Arrow> map = new HashMap<Integer, Arrow>();

    public void addinVertexGroup(Vertex vertex){
        vertexGroup.add(vertex);
    }


    //graph events
    public void onGraphPressed(MouseEvent mouseEvent) {
        if(mouseEvent.isPrimaryButtonDown()) {
            vertex1 = createAndAddVertex(mouseEvent.getX(),mouseEvent.getY());
        }
    }

    public void onGraphDragDetected(MouseEvent mouseEvent) {
        if(mouseEvent.isPrimaryButtonDown()){
            vertex2 = createAndAddVertex(mouseEvent.getX(),mouseEvent.getY());

            arrow = createAndArrow(vertex1, vertex2);
        }
    }

    public void onGraphDragged(MouseEvent mouseEvent) {
        if(vertex2 != null){
            vertex2.setLayoutX(mouseEvent.getX());
            vertex2.setLayoutY(mouseEvent.getY());
        }
    }

    public void onGraphReleased(MouseEvent mouseEvent) {

        vertex2 = null;

    }

    //vertex events
    private void onVertexPressed(MouseEvent mouseEvent, Vertex vertex) {
        if(mouseEvent.isPrimaryButtonDown()){
            vertex1 = vertex;
        }else if(mouseEvent.isSecondaryButtonDown()){
            vertexDelete = vertex;
        }
    }

    private void onVertexDragDetected(MouseEvent mouseEvent, Button vertex) {
        vertex.toFront();
        vertexDelete = null;
        if(mouseEvent.isPrimaryButtonDown()){
            vertex2 = createAndAddVertex(
                    vertex.getLayoutX() + mouseEvent.getX() + vertex.getTranslateX(),
                    vertex.getLayoutY() + mouseEvent.getY() + vertex.getTranslateY());
            arrow = createAndArrow(vertex1,vertex2);
        }else if(mouseEvent.isSecondaryButtonDown()){
            vertexDelete = null;
        }
    }

    private void onVertexDragged(MouseEvent mouseEvent, Button vertex) {
        if(vertex2 != null){
            vertex2.setLayoutX(vertex.getLayoutX()+mouseEvent.getX()+vertex.getTranslateX());
            vertex2.setLayoutY(vertex.getLayoutY()+mouseEvent.getY()+vertex.getTranslateY());
        }
        if(mouseEvent.isSecondaryButtonDown()){
            vertex.setLayoutX(vertex.getLayoutX()+mouseEvent.getX()+vertex.getTranslateX());
            vertex.setLayoutY(vertex.getLayoutY()+mouseEvent.getY()+vertex.getTranslateY());
        }
    }

    private void onVertexReleased(MouseEvent mouseEvent, Vertex vertex) {
        if(vertexDelete != null){
            graph.getChildren().remove(vertexDelete);
        }

        vertex2 = null;
        vertexDelete = null;
    }

    //helper events
    private Vertex createAndAddVertex(Double x, Double y){
        Vertex vertex = new Vertex(x, y);

        vertex.setOnMousePressed(mouseEvent -> onVertexPressed(mouseEvent,vertex));
        vertex.setOnDragDetected(mouseEvent -> onVertexDragDetected(mouseEvent,vertex));
        vertex.setOnMouseDragged(mouseEvent ->  onVertexDragged(mouseEvent,vertex));
        vertex.setOnMouseReleased(mouseEvent -> onVertexReleased(mouseEvent,vertex));

        addinVertexGroup(vertex);
        int i = 0;
        if(vertexGroup.size()!=1){
            while(i<vertexGroup.size()){
                Vertex temVertex= vertexGroup.get(i);
                if(temVertex.getWeightID()!=vertex.getWeightID()){
                    Arrow temArrow = createAndArrow(temVertex,vertex);
                    //Key key = new Key(temVertex.getWeightID(),vertex.getWeightID());
                    int num1 = temVertex.getWeightID();
                    int num2 = vertex.getWeightID();
                    map.put(num1*num2, temArrow);

                }
                i++;
            }
        }
        graph.getChildren().add(vertex);
        return vertex;
    }

    private Arrow createAndArrow(Vertex v1, Vertex v2){
        //Arrow arrow = new Arrow(v1.getLayoutX(), v1.getLayoutY(), v2.getLayoutX(), v2.getLayoutY());

        Arrow arrow = new Arrow(v1,v2);

        arrow.x1Property().bind(v1.layoutXProperty());
        arrow.y1Property().bind(v1.layoutYProperty());
        arrow.x2Property().bind(v2.layoutXProperty());
        arrow.y2Property().bind(v2.layoutYProperty());

        graph.getChildren().add(arrow);
        return arrow;
    }

    private String getAttacker(){
        return attackerField.getText();
    }

    private String getAttacked(){
        return attackedField.getText();
    }

    public boolean isLowerCase(char c) {
        return c >=97 && c <= 122;
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        Vertex attacker = null;
        Vertex attacked = null;
        Vertex temvertex;

        if(getAttacker().length()==0){
            attackerField.setText(Constants.errorText1);
            validateError(attackerField);
            return;
        }
        if(getAttacked().length()==0){
            attackedField.setText(Constants.errorText1);
            validateError(attackedField);
            return;
        }
        if(getAttacker().length()!=1){
            attackerField.setText(Constants.errorText2);
            validateError(attackerField);
            return;
        }
        if(getAttacked().length()!=1){
            attackedField.setText(Constants.errorText2);
            validateError(attackedField);
            return;
        }
        char charAttacker = getAttacker().charAt(0);
        if(!isLowerCase(charAttacker)){
            attackerField.setText(Constants.errorText4);
            validateError(attackerField);
            return;
        }

        char charAttacked = getAttacked().charAt(0);
        if(!isLowerCase(charAttacked)){
            attackedField.setText(Constants.errorText4);
            validateError(attackedField);
            return;
        }

        Arrow temArrow = map.get(charAttacker*charAttacked);

        int index0fvertex = 0;
        while(index0fvertex<vertexGroup.size()){
            temvertex = vertexGroup.get(index0fvertex);
            if(temvertex.getWeightID()==charAttacker)
                attacker = temvertex;
            if(temvertex.getWeightID()==charAttacked)
                attacked = temvertex;
            index0fvertex++;
        }

        if(attacker.getState().equals(Constants.candidate))
        {
            if(attacked.getState().equals(Constants.candidate)){
                if(attacker.getStage()==attacked.getStage()){
                    if(attacker.getID()<attacked.getID()){
                        attacker.setStage();
                        attacked.setState(Constants.captured);
                        attacked.setParent(attacker);
//                        attacker.setChildren(attacked);
                        temArrow.setColor();
                   }else{
                        attacker.setState(Constants.passive);
                    }
                }else if(attacker.getStage()>attacked.getStage()){
                    attacker.setStage();
                    attacked.setState(Constants.captured);
                    attacked.setParent(attacker);
//                    attacker.setChildren(attacked);
                    temArrow.setColor();
                }else{
                    attacker.setState(Constants.passive);
                }
            }else if(attacked.getState().equals(Constants.passive)){
                attacker.setStage();
                attacked.setParent(attacker);
//                attacker.setChildren(attacked);
                attacked.setState(Constants.captured);
                temArrow.setColor();
            }else if(attacked.getState().equals(Constants.captured)){
                Vertex vertexparent = attacked.gettheParent();
                if(vertexparent.getStage()<attacker.getStage()){
                    vertexparent.setState(Constants.passive);
                    attacked.setParent(attacker);
                    attacker.setStage();
//                    vertexparent.removeChildren(attacked);
                    temArrow.setColor();
                }else if(vertexparent.getStage()==attacker.getStage()){
                    if(attacker.getID()<vertexparent.getID()){
                        vertexparent.setState(Constants.passive);
                        attacked.setParent(attacker);
                        attacker.setStage();
                        temArrow.setColor();
                    }else{
                        attacker.setState(Constants.passive);
                    }
                }else{
                    attacker.setState(Constants.passive);
                }
            }
        } else{
            attackerField.setText(Constants.errorText3);
        }

        console.setText("attacker = "+attacker.getState()+" stage = "+attacker.getStage() + "\n" +
                "attacked = "+attacked.getState()+" stage = "+attacked.getStage());
        System.out.println("attacker = "+attacker.getState()+" stage = "+attacker.getStage());
        System.out.println("attacked = "+attacked.getState()+" stage = "+attacked.getStage());
    }

    private void validateError(TextField textField){
        textField.getStyleClass().add("error");
    }

//    private void validate(TextField tf) {
//        ObservableList<String> styleClass = tf.getStyleClass();
//        System.out.print(styleClass);
//        if (tf.getText().trim().length()==0) {
//            if (! styleClass.contains("error")) {
//                System.out.println("Stallar");
//                styleClass.add("error");
//            }
//        } else {
//            // remove all occurrences:
//            styleClass.removeAll(Collections.singleton("error"));
//        }
//    }

//    private void setUpValidation(TextField tf) {
//        tf.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable,
//                                String oldValue, String newValue) {
//                validate(tf);
//            }
//        });
//        validate(tf);
//    }
}
