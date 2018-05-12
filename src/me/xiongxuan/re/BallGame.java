package me.xiongxuan.re;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BallGame extends Application {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 600;
	
	private int delayMax = 20, delay = delayMax; //С���½�ʱ����û�ж�׼���壬����ʱͻȻ�����ƶ�����ײ�������壬�ᵼ�¶�ε���ײ�ж���ʹ��Ϸ��ǰ������
	 									//�������������һ���ӳ��жϵ�ʱ�䣬�ж���ײ�ɹ����ӳ�delayMax * Ball.upTime ms��ʱ�������һ���ж�

	private double boardPullDownVelocity = 0.7; //�����½����ٶȣ�Խ���½���Խ��
	private double ballPullDownVelocity = 0.1; //С����������½����ٶȣ�Խ���½���Խ��
	
	Timeline animationBall;
	Timeline animationBoard;
	
	Ball ball = new Ball(WIDTH, HEIGHT);
	Pane pane = new Pane();
	Board board = new Board(WIDTH, HEIGHT);
	Scene scene = new Scene(pane);
	Components components = new Components();
	Animations animations = new Animations();
	ParallelTransition pTransition = new ParallelTransition();
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage rootStage) throws Exception {
		welcomeAnimation();
		
		rootStage.setTitle("�ϲ���һ�ٲ�Ķ��ǻ���");
		rootStage.setScene(scene);
		rootStage.show();
	}
	
	/**
	 * ��Ϸ��ʼʱչʾ��ӭ����
	 */
	public void welcomeAnimation() {
		scene.setOnKeyPressed(e -> {
			scene.setOnKeyPressed(null); //ʹ����ֻ�ܰ�һ��
			
			pane.getChildren().remove(animations.getPromptText()); //�Ƴ���������ʾ�ı�
			animations.correctBallRotate(ball.getImageView()); //��1s�ڽ�С��ת��
			
			animations.getBallRotateInitAnimation().setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					animations.transBallToPoint(ball.getImageView(), 137, 574); //�����ƶ���ĳ����
					//�����ƶ������������¼�
					animations.getParallelTransition().setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							pane.getChildren().addAll(board.getRecs());
							animations.welcomeImageDisappear(); //��һ�������Ķ���ʹ��ӭ�����ͼƬ��ʧ
							////����ͼƬ��ʧ����¼�
							animations.getWelcomeImageDisappearAnimation().setOnFinished(e -> {
								pane.getChildren().remove(animations.getWelcomeImage()); //��pane���Ƴ�welcomeImage
								startGame();
								animations.clearAllAnimationFinished(); //������ж�����setOnFinished
							});
						}
					});
				}
			});
		});
		pane.getChildren().addAll(animations.getWelcomeImage(), animations.getPromptText());
		
		animations.initBall(ball.getImageView()); //��ʼ��С�򣨰�С��Ŵ���ת��
		pane.getChildren().add(ball.getImageView());
	}
	
	/**
	 * ��ʼ��Ϸ
	 */
	public void startGame() {
		components.getPauseBt().setOnMouseClicked(new PauseBtClicked()); //������ͣ��ť���¼�
		pane.getChildren().addAll(components.getMessages(), components.getScore(), components.getPauseBt());
		
		
		pane.setOnKeyPressed(new LeftRightMoveBall()); //��ס���Ҽ�ʱһֱ����С�������ƶ��Ķ���
		pane.setOnKeyReleased(new StopLeftRightMoveBall()); //�ɿ����Ҽ�ʱֹͣС�������ƶ��Ķ���
		
		animationBall = new Timeline(new KeyFrame(Duration.millis(0.1), new UpDownBall())); //С�������ƶ��Ķ���
		animationBoard  = new Timeline(new KeyFrame(Duration.millis(8), new AllDown())); //С�������һ�����ƵĶ���
		
		pTransition.getChildren().addAll(animationBall, animationBoard);
		pTransition.setCycleCount(Timeline.INDEFINITE);
		pTransition.play();
		
	}
	
	/**
	 * ��Ϸ����
	 */
	public void gameOver() {
		System.out.println("Game over");
		
		//ֹͣ�������ƺ�С�������½��Ķ���
		pTransition.stop();
		pane.setOnKeyPressed(null);
		pane.setOnKeyReleased(null);
		//ֹͣ���Ҽ�����С��
		ball.stopXMoveBall(1);
		ball.stopXMoveBall(-1);
		
		//չʾ�����Ķ�������չʾ��ʾ�ı�
		pane.getChildren().add(animations.getPromptText());
		
		animations.displayScore(components.getScore(), components.getMessages());
		animations.getpTransition().setOnFinished(e -> {
			//��ʾ��������¼�����������ؿ���Ϸ
			pane.setOnKeyPressed(ec -> {
				//����һ�����Ժ�����ȥ�����еİ����¼�����ֹ�����bug��
				pane.setOnKeyPressed(null);
				pane.setOnKeyReleased(null);
				
				//�Ƴ���ʾ�ı�
				pane.getChildren().remove(animations.getPromptText());
				animations.stopDisplayScore(components.getScore(), components.getMessages());
				animations.correctBallRotate(ball.getImageView());
				
				//С�����ת�����������飬���ؿ���Ϸ
				animations.getBallRotateInitAnimation().setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						restartGame();
//						animations.clearAllAnimationFinished();
					}
				});
			});
		});
		
	}
	
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restartGame() {
		//�Ƴ�����ϵ��������壬����������ArrayList
		pane.getChildren().removeAll(board.getRecs());
		board.removeAllRec();
		
		//���´�������µ����壬�����������ʾ
		board.initRecs();
		pane.getChildren().addAll(board.getRecs());
		
		//���üƷְ�
		components.getScore().setText("0");
		
		//��С���Ƶ���ײ�
		animations.transBallToPoint(ball.getImageView(), 137, 574);
		
		//С���Ƶ��ײ�����¼�
		animations.getParallelTransition().setOnFinished(e -> {
			ball.initBall(); //��ʼ��С�� ��С������в�����Ϊ��ʼ
			
			//�������������ƶ����¼�
			pane.setOnKeyPressed(new LeftRightMoveBall());
			pane.setOnKeyReleased(new StopLeftRightMoveBall());

			pTransition.play();
		});
		
	}
	
	/**
	 * ����������ƶ�С����¼�
	 */
	class LeftRightMoveBall implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			if (e.getCode() == KeyCode.LEFT) {
				ball.XMoveBall(-1);
			}
			else if (e.getCode() == KeyCode.RIGHT) {
				ball.XMoveBall(1);
			}
		}
	}
	
	/**
	 * �ɿ�������ֹͣ�ƶ�С����¼�
	 */
	class StopLeftRightMoveBall implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			if (e.getCode() == KeyCode.LEFT)
				ball.stopXMoveBall(-1);
			else if (e.getCode() == KeyCode.RIGHT)
				ball.stopXMoveBall(1);
		}
	}
	
	/**
	 * �����ƶ�С����¼�
	 */
	class UpDownBall implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ball.moveBall();
			if (ball.isCrossBorder()) {
				gameOver();
			}
			if (delay == 0 &&
					board.isImpactAll(ball.getX(), ball.getY(), ball.getR())) {
				if (ball.getDirection() == 1) {
					gameOver();
				}
				else {
					components.getScore().setText(Integer.toString(Integer.valueOf(components.getScore().getText()) + 1));
					ball.resetCount();
					delay = delayMax;
					ball.reversalYDirection();
				}
			}
			else 
				delay = delay > 0 ? delay - 1 : delay;
		}
	}
	
	/**
	 * ʹ���������»����¼�
	 */
	class AllDown implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			board.pullDownAll(boardPullDownVelocity);
			if (board.isBottomOutBorder()) { //�ж����·��������Ƿ����
				//������Ƴ����·������壬�ٴ���һ��������
				pane.getChildren().remove(board.getBottomRectangle());
				board.removeBottomRec();
				
				//��������뵽ArrayList������������ʾ
				board.getRecs().add(board.getARectangle());
				pane.getChildren().add(board.getTopRectangle());
			}
			ball.pullDown(ballPullDownVelocity);
		}
	}
	
	/**
	 * �����ͣ��ť���¼�
	 */
	class PauseBtClicked implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (pTransition.getStatus() != Status.STOPPED) {
				if (components.getPauseBt().getText().equals("��ͣ")) {
					//�����ͣ�Ժ�ʹ���еİ�����Ч��Ϊ�˷�ֹ��ͣ��С���������ƶ���
					pane.setOnKeyPressed(null);
					pane.setOnKeyReleased(null);
					pTransition.pause();
					components.getPauseBt().setText("����");
				}
				else {
					pane.setOnKeyPressed(new LeftRightMoveBall());
					pane.setOnKeyReleased(new StopLeftRightMoveBall());
					pTransition.play();
					components.getPauseBt().setText("��ͣ");
				}
			}
		}
	}
}
