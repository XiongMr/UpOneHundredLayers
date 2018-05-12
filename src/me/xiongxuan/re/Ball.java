package me.xiongxuan.re;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Ball {
	private ImageView imageView = new ImageView("balls.png"); //С���ͼƬ
	private Timeline moveRight, moveLeft; //����С�������ƶ���timeline

	private final int maxCount = 110; //�������������ĸ߶�
	private int count = maxCount; //������¼С���ʱ�����ĸ߶�	
	private int direction = 1; //С��ķ���-1Ϊ���£�1Ϊ����
	private final int RADIOUS = 13; //С��İ뾶
	private double YMoveStep = 1.1, XMoveStep = 0.75; //С�����»������ƶ�һ���ľ���
	private double xMoveSpeed = 6; //С�������ƶ����ٶȣ�ԽСԽ��
	
	public Ball(int paneWidth, int paneHeight) {
		imageView.setSmooth(true);
		
		imageView.setFitHeight(RADIOUS * 2);
		imageView.setFitWidth(RADIOUS * 2);
		
		//�����ƶ��Ķ���
		moveRight = new Timeline(new KeyFrame(Duration.millis(xMoveSpeed), e -> {
			if (imageView.getX() + imageView.getFitWidth() + XMoveStep > paneWidth) {
				imageView.setX(paneWidth - imageView.getFitWidth());
			}
			else {
				imageView.setX(imageView.getX() + XMoveStep);;
			}
		}));
		moveRight.setCycleCount(Timeline.INDEFINITE);
		//�����ƶ��Ķ���
		moveLeft =  new Timeline(new KeyFrame(Duration.millis(xMoveSpeed), e -> {
			if (imageView.getX() - XMoveStep < 0) {
				imageView.setX(0);
			}
			else {
				imageView.setX(imageView.getX() - XMoveStep);
			}
		}));
		moveLeft.setCycleCount(Timeline.INDEFINITE);
	}
	
	public ImageView getImageView() {
		return imageView;
	}
	
	/**
	 * ��ʼ��С��
	 * ��С������в�����Ϊ��ʼ
	 */
	public void initBall() {
		count = maxCount;
		direction = 1;
	}
	
	/**
	 * ʹС�������ƶ�
	 * @param direction Ϊ1ʱ���ң�Ϊ-1ʱ����
	 */
	public void XMoveBall(int direction) { //1Ϊ���ң�-1Ϊ����
		if (direction == 1) {
			moveRight.play();
		}
		else if (direction == -1) {
			moveLeft.play();
		}
	}
	
	/**
	 * ����С�������ƶ��ĸ߶�
	 */
	public void moveBall() {
		if (count!= 0) {
			YMoveBall();
			count--;
		}
		else if (count == 0 && getDirection() == 1) {
			reversalYDirection();
			count = maxCount;
		}
		else 
			count--;
	}
	
	/**
	 * ʹС�������ƶ�
	 */
	public void YMoveBall() {
		if (direction == -1) {
			imageView.setY(imageView.getY() + YMoveStep);
		}
		else {
			if (imageView.getY() > YMoveStep)
				imageView.setY(imageView.getY() - YMoveStep);
			else
				imageView.setY(0);
		}
	}
	
	/**
	 * ʹС�������ƶ�v����λ����������������������»����õ�
	 */
	public void pullDown(double v) {
		imageView.setY(imageView.getY() + v);
	}
	
	/**
	 * ֹͣС�������ƶ��Ķ���
	 * @param direction Ϊ1ʱ���ң�Ϊ-1ʱ����
	 */
	public void stopXMoveBall(int direction) {//1Ϊ���ң�-1Ϊ����
		if (direction == 1)
			moveRight.stop();
		else
			moveLeft.stop();
	}
	
	/**
	 * ʹС�����µķ���ת
	 */
	public void reversalYDirection() {
		direction = direction == -1 ? 1 : -1; 
	}
	
	/**
	 * ����count��ʹcount = maxCount
	 */
	public void resetCount() {
		count = maxCount;
	}
	
	/**
	 * �ж��Ƿ�Խ��
	 */
	public boolean isCrossBorder() {
		if (imageView.getY() + imageView.getFitHeight() > 600 || imageView.getY() < 0)
			return true;
		else {
			return false;
		}
	}
	
	/**
	 * ����С���ʱ�ķ���
	 * @return
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * ����С��ǰx������
	 * @return
	 */
	public double getX() {
		return imageView.getX() + RADIOUS;
	}
	
	/**
	 * ����С��ǰy������
	 * @return
	 */
	public double getY() {
		return imageView.getY() + RADIOUS;
	}
	
	/**
	 * ����С��İ뾶
	 */
	public int getR() {
		return RADIOUS;
	}
}
