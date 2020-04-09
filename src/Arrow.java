import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;


public class Arrow extends Group {

    private Polyline mainLine = new Polyline();
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    Vertex v1;
    Vertex v2;

    public Arrow(Vertex v1, Vertex v2) {

        this.x1.set(v1.getLayoutX());
        this.y1.set(v1.getLayoutY());
        this.x2.set(v2.getLayoutX());
        this.y2.set(v2.getLayoutY());
        this.v1 = v1;
        this.v2 = v2;

        getChildren().addAll(mainLine);

        for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{this.x1, this.y1, this.x2, this.y2}) {
            s.addListener((l, o, n) -> update());
        }

        //setColor();visNode
        update();
    }

    private void update() {
        mainLine.getPoints().setAll(x1.get(), y1.get(), x2.get(), y2.get());
    }

    public void setColor() {
        this.mainLine.setStroke(Color.BLUE);
    }

    //getters and setters
    public char getV1WeightID() {
        return v1.getWeightID();
    }

    public char getV2WeightID() {
        return v2.getWeightID();
    }

    public double getX1() {
        return x1.get();
    }

    public SimpleDoubleProperty x1Property() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public double getY1() {
        return y1.get();
    }

    public SimpleDoubleProperty y1Property() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public double getX2() {
        return x2.get();
    }

    public SimpleDoubleProperty x2Property() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2.set(x2);
    }

    public double getY2() {
        return y2.get();
    }

    public SimpleDoubleProperty y2Property() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }
}
