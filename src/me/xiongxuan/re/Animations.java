package me.xiongxuan.re;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Animations {
	private static double initWidth = 0; //С��ĳ�ʼ���
	
	private Text promptText = new Text();
	private ImageView welcomeImage = new ImageView("100ceng.png");
	
	private Timeline welcomeImageDisappearAnimation; //��ӭ������ʧ�Ķ���
	private Timeline ballRotateAnimation; //С����ת�Ķ���
	private Timeline promptTextAnimation; //��ʾ�ı��Ķ���
	private Timeline ballRotateInitAnimation; //ʹС�����ת���Ķ���
	private Timeline scoreMoveToCenter; //չʾ���������һ������
	private ParallelTransition parallelTransition; //ʹС���С���ص��ײ��ĺϲ�����
	private ParallelTransition displayScoreTransition; //չʾ�����ĵ�һ������
	private ParallelTransition stopDisplayTransition; //��������ĵ�һ������
	
	private double temp = -0.01; //��ʾ�ı�����͸��������
	
	public Animations() {
		//��ʼ����ʾ�ı�
		promptText.setText("���������ʼ��Ϸ");
		promptText.setFill(Color.RED);
		promptText.setFont(Font.font(14));
		promptText.setX(100);
		promptText.setY(580);
		
		//��ʾ�ı��Ķ���
		promptTextAnimation = new Timeline(new KeyFrame(Duration.millis(20), e -> {
			promptText.setOpacity(promptText.getOpacity() + temp);
			if (promptText.getOpacity() > 0.99 || promptText.getOpacity() < 0.3)
				temp *= -1;
		}));
		
	}
	
	public ImageView getWelcomeImage() {
		return welcomeImage;
	}
	
	/**
	 * ���ػ�ӭͼƬ��ʧ�Ķ����¼�
	 */
	public Timeline getWelcomeImageDisappearAnimation() {
		return welcomeImageDisappearAnimation;
	}
	
	/**
	 * ����С����ת�Ķ����¼�
	 * @return
	 */
	public Timeline getBallRotateAnimation() {
		return ballRotateAnimation;
	}

	/**
	 * ����С�����ת���Ķ����¼�
	 * @return
	 */
	public Timeline getBallRotateInitAnimation() {
		return ballRotateInitAnimation;
	}

	/**
	 * ����С���С���ص��ײ��ĺϲ��Ķ����¼�
	 * @return
	 */
	public ParallelTransition getParallelTransition() {
		return parallelTransition;
	}
	
	/**
	 * ����չʾ������һ���������¼�
	 */
	public ParallelTransition getpTransition() {
		return displayScoreTransition;
	}

	/**
	 * �������·�����ʾ�ı�
	 */
	public Text getPromptText() {
		promptTextAnimation.setCycleCount(Timeline.INDEFINITE);
		promptTextAnimation.play();
		
		return promptText;
	}
	
	/**
	 * ������ʾ�ı�������
	 */
	public void setPromptText(String text) {
		promptText.setText(text);
	}
	
	/**
	 * ��ʼ��С��ʹС����ʺϻ�ӭ���棨��С��Ŵ���ת��
	 * @param ball ����С���imageView
	 */
	public void initBall(ImageView ball) {
		initWidth = ball.getFitWidth();
		
		ball.setX(225);
		ball.setY(300);
		
		ball.setFitHeight(initWidth * 2);
		ball.setFitWidth(initWidth * 2);
		
		ballRotateAnimation = new Timeline(new KeyFrame(Duration.millis(10), 
				e -> ball.setRotate(((int)ball.getRotate() + 1) % 360)));
		ballRotateAnimation.setCycleCount(Timeline.INDEFINITE);
		ballRotateAnimation.play();
	}
	
	/**
	 * ��1s�ڽ�С��ת��
	 * @param ball
	 */
	public void correctBallRotate(ImageView ball) {
		ballRotateAnimation.stop();
		
		if (ball.getRotate() != 0) { //�ж�С���Ƿ������ģ�����������ģ��Ϳ���ת��
			double circleTimes = 360 - ball.getRotate();
			ballRotateInitAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / circleTimes), 
					e -> ball.setRotate(ball.getRotate() + 1)));
			ballRotateInitAnimation.setCycleCount((int)circleTimes);
			ballRotateInitAnimation.setOnFinished(e -> {
				ball.setRotate(0);
			});
			ballRotateInitAnimation.play();
		}
	}
	
	/**
	 * ��1.5s�ڰ�С���س�ʼ��С���Ƶ�ĳ��
	 * @param ball
	 * @param x
	 * @param y
	 */
	public void transBallToPoint(ImageView ball, double x, double y) {
		Point point1 = new Point(ball.getX(), ball.getY());
		Point point2 = new Point(x, y);
		double distance = point1.getDistance(point2);
		
		if (distance == 0)
			return;
		
		Timeline transition = new Timeline(new KeyFrame(Duration.millis(1500 / distance), e -> {
			ball.setX(ball.getX() + Math.cos(point1.getACos(point2)));
			ball.setY(ball.getY() + Math.sin(point1.getASin(point2)));
		}));
		transition.setCycleCount((int)distance);
		transition.setOnFinished(e -> {
			ball.setX(x);
			ball.setY(y);
		});
		
		//ʹС���������Ķ���
		double rate = (ball.getFitHeight() - initWidth) / distance;
		Timeline scale = new Timeline(new KeyFrame(Duration.millis(1500 / distance), e -> {
			ball.setFitWidth(ball.getFitWidth() - rate);
			ball.setFitHeight(ball.getFitHeight() - rate);
		}));
		scale.setCycleCount((int)distance);
		scale.setOnFinished(e -> {
			ball.setFitWidth(initWidth);
			ball.setFitHeight(initWidth);
		});
		
		//��������������������
		parallelTransition = new ParallelTransition(ball, transition, scale);
		parallelTransition.play();
	}

	Timeline messageMoveAnimation, scoreMoveAnimation;
	double scoreInitFontSize;
	double messageInitFontSize;
	boolean key = true;
	/**
	 * ��Ϸ����ʱ��ʾ����
	 */
	public void displayScore(Text score, Text message) {
		scoreInitFontSize = score.getFont().getSize();
		messageInitFontSize = message.getFont().getSize();
		
		Point point1 = new Point(message.getX(), message.getY());
		Point point2 = new Point(50, 250);
		double distance = point1.getDistance(point2);

		ballRotateAnimation.play(); //ʹС����ת
		
		//ʹ���������建�����
		Timeline scoreSizeAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / ((scoreInitFontSize * 2) * 10)), e -> {
			score.setFont(Font.font(score.getFont().getSize() + 0.1));
		}));
		scoreSizeAnimation.setCycleCount((int)((scoreInitFontSize * 2) * 10));
		//ʹ����ǰ����ı��������
		Timeline messageSizeAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / ((messageInitFontSize * 2) * 10)), e -> {
			message.setFont(Font.font(message.getFont().getSize() + 0.1));
		}));
		messageSizeAnimation.setCycleCount((int)((messageInitFontSize * 2) * 10));

		//ʹ����ǰ����ı��ƶ�����Ļ�м�
		messageMoveAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / distance), e -> {
			message.setX(message.getX() + Math.cos(point1.getACos(point2)));
			message.setY(message.getY() + Math.sin(point1.getASin(point2)));
			if (score.getX() + score.getLayoutBounds().getWidth() > 300 && key) {
				key = false;
				
				displayScoreTransition.pause();
				displayScoreTransition.getChildren().removeAll(scoreMoveAnimation, scoreSizeAnimation, messageSizeAnimation);
				scoreMoveAnimation = null;
				scoreSizeAnimation.pause();
				displayScoreTransition.play();
				
				scoreMoveToCenter(score);
			}
		}));
		messageMoveAnimation.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if (key) {
					scoreMoveToCenter(score);
				}
				else {
					key = true;
				}
			}
		});
		messageMoveAnimation.setCycleCount((int)distance);
		//ʹ����������ǰ����ı��ƶ���������ײ�ϱ�Ե�����ı�����ֹͣʱ��ʼ�ڶ��ζ���
		scoreMoveAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / distance), e -> {
			score.setX(message.getX() + message.getLayoutBounds().getWidth() + 10);
			score.setY(message.getY());
		}));
		scoreMoveAnimation.setCycleCount((int)distance);

		//ʹ������ֵ����ж���һ�𲥷�
		displayScoreTransition = new ParallelTransition(scoreSizeAnimation, messageSizeAnimation,
				messageMoveAnimation, scoreMoveAnimation);
		displayScoreTransition.setCycleCount(1);
		displayScoreTransition.play();
	}
	
	/**
	 * չʾ�����ĵڶ��ζ���
	 * @param score
	 */
	public void scoreMoveToCenter(Text score) {
		double distanceForBallToCenter;
		Point point1 = new Point(score.getX(), score.getY());
		Point point2 = new Point((300 - score.getLayoutBounds().getWidth()) / 2, 300);
		distanceForBallToCenter = point1.getDistance(point2);
		
//		System.out.println(point1);
//		System.out.println(point2);

		scoreMoveToCenter = new Timeline(new KeyFrame(Duration.millis(500 / distanceForBallToCenter), e -> {
			score.setX(score.getX() + Math.cos(point1.getACos(point2)));
			score.setY(score.getY() + Math.sin(point1.getASin(point2)));
		}));
		scoreMoveToCenter.setCycleCount((int)distanceForBallToCenter);
//		scoreMoveToCenter.setOnFinished(e -> {
////			score.setX(point4.getX());
////			score.setY(point4.getY());
////			System.out.println(score.getX());
////			System.out.println(score.getY());
//		});
		scoreMoveToCenter.play();
	}
	
	/**
	 * ����չʾ�����Ķ���
	 */
	public void stopDisplayScore(Text score, Text message) {
		double scoreInitFontSizeNew = score.getFont().getSize() - scoreInitFontSize;
		double messageInitFontSizeNew = message.getFont().getSize() - messageInitFontSize;
		
		//ʹ����������С
		Timeline scoreSizeAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / ((scoreInitFontSizeNew) * 10)), e -> {
			score.setFont(Font.font(score.getFont().getSize() - 0.1));
		}));
		scoreSizeAnimation.setOnFinished(e -> score.setFont(Font.font(scoreInitFontSize)));
		scoreSizeAnimation.setCycleCount((int)((scoreInitFontSizeNew) * 10));
		//ʹ����ǰ����ı�������С
		Timeline messageSizeAnimation = new Timeline(new KeyFrame(Duration.millis(1000 / ((messageInitFontSizeNew) * 10)), e -> {
			message.setFont(Font.font(message.getFont().getSize() - 0.1));
		}));
		messageSizeAnimation.setOnFinished(e -> message.setFont(Font.font(messageInitFontSize)));
		messageSizeAnimation.setCycleCount((int)((messageInitFontSizeNew) * 10));
		
		Point pointMessageInit = new Point(5, 15);
		Point pointMessage = new Point(message.getX(), message.getY());
		double distanceMessage = pointMessage.getDistance(pointMessageInit);
		Timeline moveMessage = new Timeline(new KeyFrame(Duration.millis(1000 / distanceMessage), e -> {
			message.setX(message.getX() + Math.cos(pointMessage.getACos(pointMessageInit)));
			message.setY(message.getY() + Math.sin(pointMessage.getASin(pointMessageInit)));
		}));
		moveMessage.setCycleCount((int)distanceMessage);
		
		Point pointScoreInit = new Point(82, 15);
		Point pointScore = new Point(score.getX(), score.getY());
		double distanceScore = pointScore.getDistance(pointScoreInit);
		Timeline moveScore = new Timeline(new KeyFrame(Duration.millis(1000 / distanceScore), e -> {
			score.setX(score.getX() + Math.cos(pointScore.getACos(pointScoreInit)));
			score.setY(score.getY() + Math.sin(pointScore.getASin(pointScoreInit)));
		}));
		moveScore.setOnFinished(e -> score.setY(message.getY()));
		moveScore.setCycleCount((int)distanceScore);
		
		stopDisplayTransition = new ParallelTransition(scoreSizeAnimation, messageSizeAnimation,
				moveMessage, moveScore);
		stopDisplayTransition.setCycleCount(1);
		stopDisplayTransition.setOnFinished(e -> score.setY(message.getY()));
		stopDisplayTransition.play();
	}
	
	/**
	 * ������ж�����setOnFinished
	 */
	public void clearAllAnimationFinished() {
		ballRotateAnimation.setOnFinished(null);
		promptTextAnimation.setOnFinished(null);
		ballRotateInitAnimation.setOnFinished(null);
		parallelTransition.setOnFinished(null);
	}
	
	/**
	 * ʹ��ӭͼƬ��ʧ
	 */
	public void welcomeImageDisappear() {
		welcomeImageDisappearAnimation = new Timeline(new KeyFrame(Duration.millis(10), e -> {
			welcomeImage.setOpacity(welcomeImage.getOpacity() - 0.01);
		}));
		welcomeImageDisappearAnimation.setCycleCount(100);
		welcomeImageDisappearAnimation.play();
	}
}
