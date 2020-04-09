import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class Controller {

    //bind weight
    ArrayList<Integer> list = new ArrayList<Integer>();
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

    Node node1;
    Node node2;
    Node nodeDelete;

    int totalSucceedAttack = 0;

    Arrow arrow;
    ArrayList<Node> nodeGroup = new ArrayList<Node>();

    Map<Integer, Arrow> map = new HashMap<Integer, Arrow>();

    //record nodes
    public void addinNodeGroup(Node node) {
        nodeGroup.add(node);
    }

    //graph events
    public void onGraphPressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            node1 = createAndAddNode(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    public void onGraphDragDetected(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            node2 = createAndAddNode(mouseEvent.getX(), mouseEvent.getY());

            arrow = createAndArrow(node1, node2);
        }
    }

    public void onGraphDragged(MouseEvent mouseEvent) {
        if (node2 != null) {
            node2.setLayoutX(mouseEvent.getX());
            node2.setLayoutY(mouseEvent.getY());
        }
    }

    public void onGraphReleased(MouseEvent mouseEvent) {

        node2 = null;

    }

    //Node events
    private void onNodePressed(MouseEvent mouseEvent, Node node) {
        if (mouseEvent.isPrimaryButtonDown()) {
            node1 = node;
        } else if (mouseEvent.isSecondaryButtonDown()) {
            nodeDelete = node;
        }
    }

    private void onNodeDragDetected(MouseEvent mouseEvent, Button node) {
        node.toFront();
        nodeDelete = null;
        if (mouseEvent.isPrimaryButtonDown()) {
            node2 = createAndAddNode(
                    node.getLayoutX() + mouseEvent.getX() + node.getTranslateX(),
                    node.getLayoutY() + mouseEvent.getY() + node.getTranslateY());
            arrow = createAndArrow(node1, node2);
        } else if (mouseEvent.isSecondaryButtonDown()) {
            nodeDelete = null;
        }
    }

    private void onNodeDragged(MouseEvent mouseEvent, Button node) {
        if (node2 != null) {
            node2.setLayoutX(node.getLayoutX() + mouseEvent.getX() + node.getTranslateX());
            node2.setLayoutY(node.getLayoutY() + mouseEvent.getY() + node.getTranslateY());
        }
        if (mouseEvent.isSecondaryButtonDown()) {
            node.setLayoutX(node.getLayoutX() + mouseEvent.getX() + node.getTranslateX());
            node.setLayoutY(node.getLayoutY() + mouseEvent.getY() + node.getTranslateY());
        }
    }

    private void onNodeReleased(MouseEvent mouseEvent, Node node) {
        if (nodeDelete != null) {
            graph.getChildren().remove(nodeDelete);
        }

        node2 = null;
        nodeDelete = null;
    }

    //helper events
    private Node createAndAddNode(Double x, Double y) {
        Node node = new Node(x, y, list);
        list = node.getNumberList();
        int listindex = 0;
        while (listindex < list.size()) {
            System.out.print(list.get(listindex) + " ");
            listindex++;
        }
        node.setOnMousePressed(mouseEvent -> onNodePressed(mouseEvent, node));
        node.setOnDragDetected(mouseEvent -> onNodeDragDetected(mouseEvent, node));
        node.setOnMouseDragged(mouseEvent -> onNodeDragged(mouseEvent, node));
        node.setOnMouseReleased(mouseEvent -> onNodeReleased(mouseEvent, node));

        addinNodeGroup(node);
        int i = 0;
        if (nodeGroup.size() != 1) {
            while (i < nodeGroup.size()) {
                Node temNode = nodeGroup.get(i);
                if (temNode.getWeightID() != node.getWeightID()) {
                    Arrow temArrow = createAndArrow(temNode, node);
                    int num1 = temNode.getWeightID();
                    int num2 = node.getWeightID();
                    map.put(num1 * num2, temArrow);

                }
                i++;
            }
        }
        graph.getChildren().add(node);
        return node;
    }

    //Create line
    private Arrow createAndArrow(Node v1, Node v2) {

        Arrow arrow = new Arrow(v1, v2);

        arrow.x1Property().bind(v1.layoutXProperty());
        arrow.y1Property().bind(v1.layoutYProperty());
        arrow.x2Property().bind(v2.layoutXProperty());
        arrow.y2Property().bind(v2.layoutYProperty());

        graph.getChildren().add(arrow);
        return arrow;
    }

    private String getAttacker() {
        return attackerField.getText();
    }

    private String getAttacked() {
        return attackedField.getText();
    }

    public boolean isLowerCase(char c) {
        return c >= 97 && c <= 122;
    }

    //Submit action
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        Node attacker = null;
        Node attacked = null;
        Node temnode;
        boolean winFlag = false;
        char nodeWinner = 'A';
        char charAttacker = getAttacker().charAt(0);
        char charAttacked = getAttacked().charAt(0);

        boolean validateResult = validate();
        if (validateResult == false)
            return;

        Arrow temArrow = map.get(charAttacker * charAttacked);

        int indexofNode = 0;
        while (indexofNode < nodeGroup.size()) {
            temnode = nodeGroup.get(indexofNode);
            if (temnode.getWeightID() == charAttacker)
                attacker = temnode;
            if (temnode.getWeightID() == charAttacked)
                attacked = temnode;
            indexofNode++;
        }

        if (attacker.getState().equals(Constants.candidate)) {
            if (attacked.getState().equals(Constants.candidate)) {
                if (attacker.getStage() == attacked.getStage()) {
                    if (attacker.getID() < attacked.getID()) {
                        attacker.setStage();
                        attacked.setState(Constants.captured);
                        attacked.setParent(attacker);
//                        attacker.setChildren(attacked);
                        temArrow.setColor();
                        totalSucceedAttack++;
                        if (isWin()) {
                            nodeWinner = attacker.getWeightID();
                        }
                    } else {
                        attacker.setState(Constants.passive);
                    }
                } else if (attacker.getStage() > attacked.getStage()) {
                    attacker.setStage();
                    attacked.setState(Constants.captured);
                    attacked.setParent(attacker);
//                    attacker.setChildren(attacked);
                    temArrow.setColor();
                    totalSucceedAttack++;
                    if (isWin()) {
                        nodeWinner = attacker.getWeightID();
                    }
                } else {
                    attacker.setState(Constants.passive);
                }
            } else if (attacked.getState().equals(Constants.passive)) {
                attacker.setStage();
                attacked.setParent(attacker);
//                attacker.setChildren(attacked);
                attacked.setState(Constants.captured);
                temArrow.setColor();
                totalSucceedAttack++;
                if (isWin()) {
                    nodeWinner = attacker.getWeightID();
                }
            } else if (attacked.getState().equals(Constants.captured)) {
                Node nodeparent = attacked.gettheParent();
                if (nodeparent.getStage() < attacker.getStage()) {
                    nodeparent.setState(Constants.passive);
                    attacked.setParent(attacker);
                    attacker.setStage();
//                    nodeparent.removeChildren(attacked);
                    temArrow.setColor();
                    totalSucceedAttack++;
                    if (isWin()) {
                        nodeWinner = attacker.getWeightID();
                    }
                } else if (nodeparent.getStage() == attacker.getStage()) {
                    if (attacker.getID() < nodeparent.getID()) {
                        nodeparent.setState(Constants.passive);
                        attacked.setParent(attacker);
                        attacker.setStage();
                        temArrow.setColor();
                        totalSucceedAttack++;
                        if (isWin()) {
                            nodeWinner = attacker.getWeightID();
                        }
                    } else {
                        attacker.setState(Constants.passive);
                    }
                } else {
                    attacker.setState(Constants.passive);
                }
            }
        } else {
            attackerField.setText(Constants.errorText3);
        }
        if (isWin()) {
            System.out.println("Winn");
            console.setText("attacker = " + attacker.getState() + " stage = " + attacker.getStage() + "\n" +
                    "attacked = " + attacked.getState() + " stage = " + attacked.getStage() + "\n" +
                    "End and winner is ———— " + nodeWinner);

        } else {
            console.setText("attacker = " + attacker.getState() + " stage = " + attacker.getStage() + "\n" +
                    "attacked = " + attacked.getState() + " stage = " + attacked.getStage());
        }
        removeError(attackerField);
        removeError(attackedField);
    }

    // validate error input
    private boolean validate() {
        if (getAttacker().length() == 0) {
            attackerField.setText(Constants.errorText1);
            validateError(attackerField);
            return false;
        }
        if (getAttacked().length() == 0) {
            attackedField.setText(Constants.errorText1);
            validateError(attackedField);
            return false;
        }
        if (getAttacker().length() != 1) {
            attackerField.setText(Constants.errorText2);
            validateError(attackerField);
            return false;
        }
        if (getAttacked().length() != 1) {
            attackedField.setText(Constants.errorText2);
            validateError(attackedField);
            return false;
        }
        char charAttacker = getAttacker().charAt(0);
        if (!isLowerCase(charAttacker)) {
            attackerField.setText(Constants.errorText4);
            validateError(attackerField);
            return false;
        }

        char charAttacked = getAttacked().charAt(0);
        if (!isLowerCase(charAttacked)) {
            attackedField.setText(Constants.errorText4);
            validateError(attackedField);
            return false;
        }
        return true;
    }

    private void validateError(TextField textField) {
        textField.getStyleClass().add("error");
    }

    private void removeError(TextField textField) {
        textField.getStyleClass().remove("error");
    }

    //judge is win or not
    private boolean isWin() {
        if (totalSucceedAttack == nodeGroup.size() - 1) {

            return true;
        }
        return false;
    }

}
