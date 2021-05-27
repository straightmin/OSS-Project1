import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

/**
 * [GUI 기반 화상통화 프로그램]
 * MessageListener : 서버와 소켓통신하는 클래스
 * 소켓통신에서 송신과 수신을 스레드로 나눠 MessageListener에선 수신만 담당한다.
 * MyConnector에 의해 시작된다.
 * 
 * 작성자: 2017243078 민종현	/	작성날짜: 21.05.27
 * 수정자: 2017243055 최백균   /	작성날짜: 21.05.27
 */

public class MessageListener extends Thread {
	
	// 소켓 통신용 필드
	private Socket socket = null;
	private InputStream inStream = null;
	private DataInputStream dataInStream = null;
	private String msg;

    // 메시지 구분용 구문 문자
	private final String QuitTag = "QUIT";
	private final String connectTag = "CONNECT";
	private final String listTag = "LIST";
	private final String chatTag = "CHAT";
	private final String TK = "@";
	
	// 모든 클래스에 접근하기 위해 operator 인스턴스 저장
	private Operator mainOperator = null;
	
	public MessageListener(Socket _s, Operator _o){
		this.socket = _s;
		this.mainOperator = _o;
	}
	
	public void run() {
		StringTokenizer stringTokenizer = null;
		String control;
		try {
			inStream = socket.getInputStream();
			dataInStream = new DataInputStream(inStream);
			
			while(true) {
				msg = dataInStream.readUTF();
				stringTokenizer = new StringTokenizer(msg, TK);
				control = stringTokenizer.nextToken();	// 명령메시지 분리, switch를 통해 기능 구분
				System.out.println(msg);
				
				switch(control) {	// 명령 메시지에 따라 기능 구분
				case connectTag:	// 접속 메시지
					
					// 채팅 내용에 접속 메시지 출력
					mainOperator.mf.chatArea.append(stringTokenizer.nextToken() + "님이 접속하셨습니다.\n");
					mainOperator.mf.textScroll.getVerticalScrollBar().setValue(	// 스크롤 최하단으로 이동
							mainOperator.mf.textScroll.getVerticalScrollBar().getMaximum());
					break;
				case listTag:	// 접속인원 메시지
					String userName;
					mainOperator.mf.userList.setText("");
					
					// 접속 인원 출력
					while(stringTokenizer.hasMoreTokens()) {
						userName = stringTokenizer.nextToken();
						if(!mainOperator.mf.userList.getText().equals(""))
							userName = "\n" + userName;
						mainOperator.mf.userList.append(userName);
					}
					break;
				case chatTag:	// 채팅 메시지
					
					// 채팅 메시지 출력
					mainOperator.mf.chatArea.append(stringTokenizer.nextToken() + "\n");
					mainOperator.mf.textScroll.getVerticalScrollBar().setValue(
							mainOperator.mf.textScroll.getVerticalScrollBar().getMaximum());
					break;
				case QuitTag:	// Quit 메시지
					String exitUser = stringTokenizer.nextToken();
					String userList = mainOperator.mf.userList.getText();
					StringTokenizer userToken = new StringTokenizer(userList, "\n");
					String findUser = null;
					mainOperator.mf.userList.setText("");
					
					// 접속 인원 리스트 새로고침
					while(userToken.hasMoreTokens()) {
						findUser = userToken.nextToken();
						if(findUser.equals(exitUser)) continue;
						if(!mainOperator.mf.userList.getText().equals(""))
							findUser = "\n" + findUser;
						mainOperator.mf.userList.append(findUser);
					}
					mainOperator.mf.chatArea.append(exitUser + "님이 퇴장하셨습니다.\n");
					mainOperator.mf.textScroll.getVerticalScrollBar().setValue(
							mainOperator.mf.textScroll.getVerticalScrollBar().getMaximum());
					break;
				}
			}
			
		} catch (SocketException e) {
			System.out.println("소켓오류. 클라이언트 종료.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
