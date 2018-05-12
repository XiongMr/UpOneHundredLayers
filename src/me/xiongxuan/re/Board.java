package me.xiongxuan.re;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.shape.Rectangle;

public class Board {
	private int width, heigth; //���ĸߺͿ�Ҳ���������������ķ�Χ
	private final int recHeight = 5;
	
	
	private Random random = new Random();
	
	private ArrayList<Rectangle> recs = new ArrayList<Rectangle>();
	
	public Board(int width, int heigth) {
		this.width = width;
		this.heigth = heigth;
		
		initRecs();
	}
	
	/**
	 * ��ʼ�����������
	 */
	public void initRecs() {
		for (int i = 1; i < 5; i++)
			recs.add(getARectangle(i));
	}
	
	/**
	 * ��������ļ���
	 * @return ����Ϊһ��ArrayList�ĳ����μ���
	 */
	public ArrayList<Rectangle> getRecs() {
		return recs;
	}
	
	/**
	 * ����һ������
	 * @param ����Ϊľ���λ�ã�1Ϊ�����棬4Ϊ������
	 * @return ����Ϊһ��Rectangle��
	 */
	public Rectangle getARectangle(int i) {
		Rectangle rectangles = new Rectangle();
		rectangles.setFill(javafx.scene.paint.Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		
		rectangles.setWidth(random.nextInt(width / 3 - width / 4 + 1) + width / 4 + 1);
		rectangles.setHeight(recHeight);
		rectangles.setX(random.nextInt((int) (width - rectangles.getWidth())));
		rectangles.setY(heigth - i * heigth / 4);
		
		return rectangles;
	}
	
	/**
	 * ���������һ������
	 */
	public Rectangle getARectangle() {
		Rectangle rectangles = new Rectangle();
		rectangles.setFill(javafx.scene.paint.Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		
		rectangles.setWidth(random.nextInt(width / 3 - width / 4 + 1) + width / 4 + 1);
		rectangles.setHeight(recHeight);
		rectangles.setX(random.nextInt((int) (width - rectangles.getWidth())));
		rectangles.setY(heigth - 4 * heigth / 4);
		
		return rectangles;
	}
	
	/**
	 * �ж�ArrayList�����еĳ����κ�Բ���Ƿ�����
	 * @param x,y,r Բ�ε�Բ������Ͱ뾶
	 * @return
	 */
	public boolean isImpactAll(double x, double y, int r) {
		for (int i = 0; i < 4; i++) {
			if (isImpact(x, y, r, recs.get(i)))
				return true;
		}
		return false;
	}
	
	/**
	 * �ж�һ�������κ�Բ���Ƿ�����
	 * @param x,y,r Բ�ε�Բ������Ͱ뾶
	 * @param rectangle ��������
	 * @return
	 */
	public boolean isImpact(double x, double y, int r, Rectangle rectangle) {
		//�ѳ����ε��������Ҷ��ӳ�r�ĳ��ȣ��ж�Բ���Ƿ����µĳ������ڣ������ڣ���û��ײ
		if (rectangle.getX() - r < x &&
				rectangle.getX() + rectangle.getWidth() + r > x &&
				rectangle.getY() - r < y &&
				rectangle.getY() + rectangle.getHeight() + r > y) {
			//�ж�Բ���Ƿ����³������ĸ��ǵ�r * r�������ڣ������ڣ�����ײ
			if (rectangle.getX() - r < x && rectangle.getX() > x && rectangle.getY() - r < y && rectangle.getY() > y ||
					rectangle.getX() - r < x && rectangle.getX() > x && rectangle.getY() + rectangle.getHeight() < y && rectangle.getY() + rectangle.getHeight() + r > y ||
					rectangle.getX() + rectangle.getWidth() < x && rectangle.getX() + rectangle.getWidth() + r > x && rectangle.getY() - r < y && rectangle.getY() > y ||
					rectangle.getX() + rectangle.getWidth() < x && rectangle.getX() + rectangle.getWidth() + r > x && rectangle.getY() + rectangle.getHeight() < y && rectangle.getY() + rectangle.getHeight() + r > y) {
				//�ж�Բ�ĺ�ԭ�������ĸ�����ľ��룬����һ������С�ڰ뾶r������ײ
				if (getDistance(rectangle.getX(), rectangle.getY(), x, y) < r ||
						getDistance(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), x, y) < r ||
						getDistance(rectangle.getX(), rectangle.getY() + rectangle.getHeight(), x, y) < r ||
						getDistance(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight(), x, y) < r) {
					return true;
				}
				else
					return false;
			}
			else
				return true;
		}
		else
			return false;
	}
	
	/**
	 * ���������ľ���
	 * @param x 
	 * @param y 
	 * @param x2 
	 * @param y2
	 * @return
	 */
	public double getDistance(double x, double y, double x2, double y2) {
		return Math.sqrt(Math.abs(x-x2)*Math.abs(x-x2)+Math.abs(y-y2)*Math.abs(y-y2));
	}
	
	public void pullDownAll(double v) {
		for (int i = 0; i < 4; i++)
			pullDown(i, v);
	}
	
	public void pullDown(int index, double v) {
		recs.get(index).setY(recs.get(index).getY() + v);
	}
	
	public boolean isBottomOutBorder() {
		if (recs.get(0).getY() > BallGame.HEIGHT)
			return true;
		return false;
	}
	
	public void removeBottomRec() {
		recs.remove(0);
	}
	
	public void removeAllRec() {
		recs.clear();
	}
	
	public Rectangle getTopRectangle() {
		return recs.get(3);
	}
	
	public Rectangle getBottomRectangle() {
		return recs.get(0);
	}
}
