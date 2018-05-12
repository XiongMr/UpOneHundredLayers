package me.xiongxuan.re;

import javafx.animation.ParallelTransition;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Components {
	private Text messages; //����ǰ����ʾ�ı�
	private Text score; //��ҵķ���
	private Button pauseBt; //��ͣ��ť
	
	ParallelTransition pTransition = new ParallelTransition(); //
	
	public Components() {
		messages = new Text("��ķ���Ϊ��");
		messages.setFill(Color.RED);
		messages.setX(5);
		messages.setY(15);
		
		score = new Text("0");
		score.setX(messages.getLayoutBounds().getWidth() + 10);
		score.setY(15);

		pauseBt = new Button("��ͣ");
		pauseBt.setLayoutX(250);
	}

	public Text getMessages() {
		return messages;
	}

	public void setMessages(Text messages) {
		this.messages = messages;
	}

	public Button getPauseBt() {
		return pauseBt;
	}

	public void setPauseBt(Button pauseBt) {
		this.pauseBt = pauseBt;
	}

	public Text getScore() {
		return score;
	}
}
