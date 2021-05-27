/**
 * [GUI 기반 화상통화 프로그램]
 * Operator: 클라이언트 단에서 모든 프레임을 관리하는 클래스
 * 모든 클래스를 하나의 인스턴스로 묶어서 관리한다.
 * 
 * 작성자 : 2017243055 최백균 / 작성날짜 : 2021.05.27
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame{

	// 공간 제어용
	JPanel basePanel = new JPanel(new BorderLayout());
	JPanel centerPanel = new JPanel(new BorderLayout());
	JPanel eastPanel = new JPanel(new BorderLayout());
	
	// centerPanel용 컴포넌트 선언
	JTextArea chatArea = new JTextArea();
	JTextField typeField = new JTextField();
	JScrollPane textScroll = new JScrollPane();
	
	// eastPanel용 컴포넌트 선언
	JLabel userLabel = new JLabel("접속인원");
	JTextArea userList = new JTextArea();
	JButton sendButton = new JButton("전송");

	// 모든 클래스에 접근하기 위해 operator 인스턴스 저장
	Operator mainOperator = null;
	
	final int CENTER_PANE = 700;
	final int EAST_PANE = 100;
	
	MainFrame(Operator _o){
		mainOperator = _o;
		
		setTitle("메신저");
		
		MyActionListener mal = new MyActionListener();
		
		Font font = new Font(null, Font.PLAIN, 20);
		
		// 공간 배치
		setContentPane(basePanel);
		basePanel.add(centerPanel, BorderLayout.CENTER);
		basePanel.add(eastPanel, BorderLayout.EAST);
		
		// center 패널 추가 작업
		chatArea.setEditable(false);
		textScroll = new JScrollPane(chatArea);
		textScroll.setPreferredSize(new Dimension(CENTER_PANE, 10));
		typeField.setPreferredSize(new Dimension(CENTER_PANE, 50));
		chatArea.setFont(font);
		typeField.setFont(font);
		centerPanel.add(textScroll, BorderLayout.CENTER);
		centerPanel.add(typeField, BorderLayout.SOUTH);
		
		// east 패널 추가 작업
		userList.setEditable(false);
		userLabel.setPreferredSize(new Dimension(EAST_PANE, 20));
		userList.setPreferredSize(new Dimension(EAST_PANE, 10));
		sendButton.setPreferredSize(new Dimension(EAST_PANE, 50));
		eastPanel.add(userLabel, BorderLayout.NORTH);
		eastPanel.add(userList, BorderLayout.CENTER);
		eastPanel.add(sendButton, BorderLayout.SOUTH);
		
		typeField.addActionListener(mal);
		sendButton.addActionListener(mal);
		this.addWindowListener(new MyWindowListener());
		setVisible(true);
		
		setSize(800, 700);
	}
	
	// TextField, Button 통합 이벤트 리스너
	class MyActionListener implements ActionListener {

		@Override
		// 채팅 메시지 전송
		public void actionPerformed(ActionEvent e) {
			
			String msg = typeField.getText();
			if(msg.equals("")) return;
			mainOperator.connector.sendMessage(msg);
			typeField.setText("");
		}
		
	}
	
	// 윈도우 종료 이벤트 리스너
	class MyWindowListener extends WindowAdapter {
		
		// 로그아웃 정보 전달 후 클라이언트 종료
		public void windowClosing(WindowEvent e) {
			mainOperator.connector.sendQuitInformation();
			chatArea.setText("");
			userList.setText("");
			typeField.setText("");
			System.exit(0);
		}
	}
}
