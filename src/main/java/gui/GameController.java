package gui;

import java.awt.Desktop;
import java.awt.MenuBar;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Timer;

import game.Board;
import game.Checker;
import game.Constant;
import game.Die;
import game.Game;
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
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
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
	@FXML
	ImageView die1;
	@FXML
	ImageView die2;
	@FXML
	ImageView die3;
	@FXML
	ImageView die4;
	@FXML
	ImageView background;
	@FXML
	javafx.scene.control.MenuBar menuBar;
	
	@FXML
	Button btnRoll;
	@FXML
	Button btnOK;

	@FXML
	Circle goRed;
	@FXML
	Circle goBlack;
	@FXML
	Text redBorneOffCount;
	@FXML
	Text blackBorneOffCount;
	@FXML
	Text informationText;

	

	Board board = Board.getInstance();
	Game game;
	Stack[] points = board.getPoint();
	Shape[] polygon;
	ImageView[] guiDice;
	Map<Integer, Shape> pointMap;
	Map<Shape, Integer> shapeIndexMap;
	Map<Circle, Checker> checkerMap;
	Map<Checker, Circle> circleMap;

	Point2D dragAnchor;
	private double initX;
	private double initY;
	private double stagex;
	private double stagey;

	public void init(Stage stage, Parent root) {
		this.stage = stage;
		ap = (AnchorPane) root;
		addDragListeners(menuBar, stage);

		background.setImage(new Image(ClassLoader.getSystemResourceAsStream("wood.jpg")));

		polygon = new Shape[] { stack0, stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9, stack10,
				stack11, stack12, stack13, stack14, stack15, stack16, stack17, stack18, stack19, stack20, stack21,
				stack22, stack23, stack24, stack25, stack26, stack27 };

		guiDice = new ImageView[] { die1, die2, die3, die4 };

		pointMap = new HashMap<Integer, Shape>();
		shapeIndexMap = new HashMap<Shape, Integer>();

		for (int i = 0; i < 28; i++) {
			pointMap.put(new Integer(i), polygon[i]);
			shapeIndexMap.put(polygon[i], new Integer(i));
		}
		checkerMap = new HashMap<>();
		circleMap = new HashMap<>();

		board.registerObserver(this);
		
//		int[] redSetupPoints = {19,20,21,22,23,24};
//		int[] noOfRedCheckers = {0,0,0,0,0,2};
//		int[] blackSetupPoints = {1,2,3,4,5,6};
//		int[] noOfBlackCheckers = {2,2,2,3,3,3};
//		board.createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
		board.setUp();
		board.setState(board.getBlackState());
		board.nextPlayer();
		game = new Game();
		game.registerObserver(this);
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
				if(event.isPrimaryButtonDown() && !event.isSecondaryButtonDown()){
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
				}}
				event.consume();
			}
		});
		checker.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if(me.isPrimaryButtonDown()){
				initX = checker.getTranslateX();
				initY = checker.getTranslateY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());}
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
						acceptedMove = game.move(startPos, targetPos);
						enableCheckers(board.getState().getColor());
						break;
					}
				}
				if (!acceptedMove) {
					checker.setTranslateX(initX);
					checker.setTranslateY(initY);
				}
				event.consume();
				countBearOff();
			}
		});

		ap.getChildren().add(checker);
		return checker;
	}

	public void placeOnStack(Circle checker, int point) {
		checker.setDisable(false);
		checker.toFront();
		Shape targetShape = pointMap.get(point);
		double pointX = targetShape.getLayoutX();
		double pointY = targetShape.getLayoutY();

		int checkersInStack = points[point].size();

		if (checkersInStack > 0) {
			Checker topchecker = (Checker) points[point].peek();
			circleMap.get(topchecker).setDisable(true);
		}

		boolean lowerBoard = point >= Constant.BLACK && point < 13 || point == Constant.REDBAR;

		if (lowerBoard) {
			if (point == Constant.REDBAR) {
				pointX += 35;
				pointY += 25 + (1.5 * checker.getRadius() * checkersInStack);
			} else if (point == Constant.BLACK) {
				pointX += 100;
				pointY += 200;
			} else
				pointY += 4 * checker.getRadius() - (1.5 * checker.getRadius() * checkersInStack);
		} else {
			if (point == Constant.BLACKBAR) {
				pointX += 35;
				pointY += +205 - (1.5 * checker.getRadius() * checkersInStack);
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

	@FXML
	public void rollDice() {
		game.roll();
		btnRoll.setDisable(true);
	}

	@FXML
	public void exit() {
		System.exit(0);
	}

	@FXML
	public void newGame() {
		for (Entry<Circle, Checker> entry : checkerMap.entrySet()) {
			ap.getChildren().remove(entry.getKey());
		}
		checkerMap.clear();
		circleMap.clear();
		board.setUp();
		board.nextPlayer();
		redBorneOffCount.setText(null);
		blackBorneOffCount.setText(null);
		informationText.setText(null);
		game.removeDice();
	}

	@Override
	public void drawDice(ArrayList<Die> dice) {
		for (ImageView i : guiDice) {
			i.setImage(null);
		}
		for (int i = 0; i < dice.size(); i++) {
			int dots = dice.get(i).getValue();
			guiDice[i].setImage(setDie(dots));
		}
	}

	public Image setDie(int dots) {
		String fileName = String.format("%d.png", dots);
		Image die = new Image(ClassLoader.getSystemResourceAsStream(fileName));
		return die;
	}

	@Override
	public void updatePlayer(int player) {
		if (player == Constant.RED) {
			goRed.setVisible(true);
			goBlack.setVisible(false);
		} else {
			goRed.setVisible(false);
			goBlack.setVisible(true);
		}
		btnRoll.setDisable(false);
	}

	private void enableCheckers(int player) {
		disableAllCheckers();
		if (board.getState() == board.getBlackBarstate() && !points[Constant.BLACKBAR].empty()) {
			Checker c = (Checker) points[Constant.BLACKBAR].peek();
			circleMap.get(c).setDisable(false);
		} else if (board.getState() == board.getRedBarState() && !points[Constant.REDBAR].empty()) {
			Checker c = (Checker) points[Constant.REDBAR].peek();
			circleMap.get(c).setDisable(false);
		} else {
			for (int i = 0; i < points.length; i++) {
				if (!points[i].empty()) {
					Checker c = (Checker) points[i].peek();
					if (c.color == player && c.getPosition() != Constant.BLACK && c.getPosition() != Constant.RED) {
						circleMap.get(c).setDisable(false);
					}
				}
			}
		}
	}

	private void disableAllCheckers() {
		for (Entry<Checker, Circle> entry : circleMap.entrySet()) {
			entry.getValue().setDisable(true);
		}
	}

	private void addDragListeners(final Node node, Stage primaryStage) {

		node.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				stagex = node.getScene().getWindow().getX() - event.getScreenX();
				stagey = node.getScene().getWindow().getY() - event.getScreenY();
			}
		});

		node.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() + stagex);
				primaryStage.setY(event.getScreenY() + stagey);
			}
		});
	}
	
	private void countBearOff() {
		if(board.getState() == board.getBlackBearOffState() || board.getState() == board.getRedBearOffState()){
			String blackCount = points[Constant.BLACK].empty()? null : String.format("%d", points[Constant.BLACK].size());
			blackBorneOffCount.setText(blackCount);
			blackBorneOffCount.toFront();
			String redCount = points[Constant.RED].empty()? null: String.format("%d", points[Constant.RED].size());
			redBorneOffCount.setText(redCount);
			redBorneOffCount.toFront();
	 }
	}
	
	@FXML
	public void openWiki(){
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Backgammon"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notifyNoMoves() {
		informationText.setText("NO POSSIBLE MOVES!");
		disableAllCheckers();
		btnRoll.setVisible(false);
		btnRoll.setDisable(true);
		btnOK.setVisible(true);
		btnOK.setDisable(false);
		btnOK.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				game.removeDice();
				btnOK.setVisible(false);
				btnOK.setDisable(true);
				btnRoll.setVisible(true);
				btnRoll.setDisable(false);
				informationText.setText(null);
				enableCheckers(board.getState().getColor());
			}
		});
	}

	@Override
	public void notifyWinner(int player) {
		System.out.println("Winner Notified");
		if(player == Constant.RED){
			informationText.setText("RED WON THE ROUND");
		}else
			informationText.setText("BLACK WON THE ROUND");
	}

}
