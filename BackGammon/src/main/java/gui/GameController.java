package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Map.Entry;

import game.Board;
import game.Checker;
import game.Constant;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class GameController implements Initializable {

	private Stage stage;
	private AnchorPane ap;

	@FXML
	Rectangle stack0;
	@FXML
	Polygon stack1;
	@FXML
	Polygon stack2;
	@FXML
	Polygon stack3;
	@FXML
	Polygon stack4;
	@FXML
	Polygon stack5;
	@FXML
	Polygon stack6;
	@FXML
	Polygon stack7;
	@FXML
	Polygon stack8;
	@FXML
	Polygon stack9;
	@FXML
	Polygon stack10;
	@FXML
	Polygon stack11;
	@FXML
	Polygon stack12;
	@FXML
	Polygon stack13;
	@FXML
	Polygon stack14;
	@FXML
	Polygon stack15;
	@FXML
	Polygon stack16;
	@FXML
	Polygon stack17;
	@FXML
	Polygon stack18;
	@FXML
	Polygon stack19;
	@FXML
	Polygon stack20;
	@FXML
	Polygon stack21;
	@FXML
	Polygon stack22;
	@FXML
	Polygon stack23;
	@FXML
	Polygon stack24;
	@FXML
	Rectangle stack25;
	@FXML
	Rectangle stack26;
	@FXML
	Rectangle stack27;

	Shape[] polygon;
	Board board = Board.getInstance();
	Stack[] points = board.getPoint();
	Map<Integer, Shape> pointMap;
	Map<Circle, Checker> checkerMap;

	Point2D dragAnchor;
	private double initX;
	private double initY;

	public void init(Stage stage, Parent root) {
		this.stage = stage;
		ap = (AnchorPane) root;

		polygon = new Shape[] { stack0, stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9, stack10,
				stack11, stack12, stack13, stack14, stack15, stack16, stack17, stack18, stack19, stack20, stack21,
				stack22, stack23, stack24, stack25, stack26, stack27 };

		pointMap = new HashMap<Integer, Shape>();

		for (int i = 0; i < 28; i++) {
			pointMap.put(new Integer(i), polygon[i]);
		}
		checkerMap = new HashMap<Circle, Checker>();
		
		board.setGameController(this);
		board.setUp();
	}

	public void initialize(URL location, ResourceBundle resources) {

	}

	public void drawChecker(Checker checker, int color) {
		Circle c = createChecker(color);
		checkerMap.put(c, checker);
		placeOnStack(c, checker.getPosition());
	}

	public Circle createChecker(final int color) {

		final Color checkerColor;
		if (color == Constant.BLACK) {
			checkerColor = Color.BLACK;
		} else {
			checkerColor = Color.RED;
		}

		final Circle checker = new Circle(20, checkerColor);
		checker.setTranslateX(50);
		checker.setTranslateY(50);

		checker.setFill(new RadialGradient(0, 0, 0.05, 0.1, 1, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.rgb(250, 250, 255)), new Stop(1, checkerColor) }));

		checker.setCursor(Cursor.CLOSED_HAND);

		checker.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				checker.setEffect(new InnerShadow(7, checkerColor.brighter().brighter()));

			}
		});

		checker.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				checker.setEffect(null);

			}

		});

		checker.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				double dragX = event.getSceneX() - dragAnchor.getX();
				double dragY = event.getSceneY() - dragAnchor.getY();

				double newXPosition = initX + dragX;
				double newYPosition = initY + dragY;

				checker.toFront();
				if ((newXPosition >= checker.getRadius()) && (newXPosition <= (ap.getWidth() - checker.getRadius()))) {
					checker.setTranslateX(newXPosition);
				}
				if ((newYPosition >= checker.getRadius() + 25)
						&& (newYPosition <= (ap.getHeight() - checker.getRadius()))) {
					checker.setTranslateY(newYPosition);
				}
			}
		});

		checker.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				initX = checker.getTranslateX();
				initY = checker.getTranslateY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
			}
		});
		
		checker.setOnMouseReleased(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				
			}
			
		});
		ap.getChildren().add(checker);
		return checker;
	}

	public void placeOnStack(Circle checker, int point) {
		Shape targetShape = pointMap.get(point);
		double pointX = targetShape.getLayoutX();
		double pointY = targetShape.getLayoutY();
		
		int checkersInStack = points[point].size();
		
		boolean lowerBoard = point>= Constant.BLACK && point < 13 || point == Constant.REDBAR;
		
		if (lowerBoard) {
			if(point == Constant.REDBAR){
				pointX += 35;
				pointY += 25; 
			}else if(point == Constant.BLACK){
				pointX += 100;
				pointY += 200;
			}else
			pointY += 4 * checker.getRadius() - (1.5*checker.getRadius()*checkersInStack);
		}else{
			if(point == Constant.BLACKBAR){
				pointX += 35;
				pointY += +205;
			}else if(point == Constant.RED){
				pointX += 100;
				pointY += 25;
			}
			else{
			pointY += -5 * checker.getRadius() + (1.5*checker.getRadius()*checkersInStack);
			}
		}
		
		checker.setTranslateX(pointX);
		checker.setTranslateY(pointY);

	}
}
