package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Map.Entry;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;

import game.Board;
import game.Checker;
import game.Constant;
import game.Observer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
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

public class GameController implements Initializable, Observer {

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
	Map<Shape, Integer> shapeIndexMap;
	Map<Circle, Checker> checkerMap;
	Map<Checker, Circle> circleMap;

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
		shapeIndexMap = new HashMap<Shape, Integer>();

		for (int i = 0; i < 28; i++) {
			pointMap.put(new Integer(i), polygon[i]);
			shapeIndexMap.put(polygon[i], new Integer(i));
		}
		checkerMap = new HashMap<>();
		circleMap = new HashMap<>();

		board.registerObserver(this);
		board.setUp();
		board.setState(board.getRedState());
	}

	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void drawChecker(Checker checker, int color) {
		Circle c = createChecker(color);
		checkerMap.put(c, checker);
		circleMap.put(checker, c);
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
				event.consume();
			}
		});

		checker.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				initX = checker.getTranslateX();
				initY = checker.getTranslateY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
				me.consume();
			}
		});

		checker.setOnMouseReleased(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				Boolean insideShape;
				Boolean acceptedMove = false;
				for (Shape s : polygon) {
					insideShape = s.getBoundsInParent().contains(new Point2D(event.getSceneX(), event.getSceneY()));
					if (insideShape) {
						int startPos = checkerMap.get(checker).getPosition();
						int targetPos = shapeIndexMap.get(s);
						placeOnStack(checker, targetPos);
						acceptedMove = board.move(startPos, targetPos);
						break;
					}
				}
				if (!acceptedMove) {
					checker.setTranslateX(initX);
					checker.setTranslateY(initY);
				}
				event.consume();
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

		boolean lowerBoard = point >= Constant.BLACK && point < 13 || point == Constant.REDBAR;

		if (lowerBoard) {
			if (point == Constant.REDBAR) {
				pointX += 35;
				pointY += 25;
			} else if (point == Constant.BLACK) {
				pointX += 100;
				pointY += 200;
			} else
				pointY += 4 * checker.getRadius() - (1.5 * checker.getRadius() * checkersInStack);
		} else {
			if (point == Constant.BLACKBAR) {
				pointX += 35;
				pointY += +205;
			} else if (point == Constant.RED) {
				pointX += 100;
				pointY += 25;
			} else {
				pointY += -5 * checker.getRadius() + (1.5 * checker.getRadius() * checkersInStack);
			}
		}

		checker.setTranslateX(pointX);
		checker.setTranslateY(pointY);

	}

	@Override
	public void moveChecker(Checker checker, int toPoint) {
		Circle c = circleMap.get(checker);
		placeOnStack(c, toPoint);
	}

}
