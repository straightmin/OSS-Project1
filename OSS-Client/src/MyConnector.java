import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * [GUI 기반 화상통화 프로그램]
 * MyConnector: 서버와 소켓통신하는 클래스
 * 소켓통신에서 송신과 수신을 스레드로 나눠 MyConnector에선 송신만 담당한다.
 * 
 * 작성자: 2017243078 민종현	/	작성날짜: 21.05.27
 */

public class MyConnector {
	
	// 소켓 통신용 필드
	private Socket socket = null;
	private OutputStream outStream = null;
	private DataOutputStream dataOutStream = null;
	private MessageListener msgListener = null;
	private final static int portNum = 7777;

    // 메시지 구분용 구문 문자
	private final String QuitTag = "QUIT";
	private final String chatTag = "CHAT";
	private final String TK = "@";
	
	// 모든 클래스에 접근하기 위해 operator 인스턴스 저장
	private Operator mainOperator = null;
	
	// 서버측에서 개방된 주소와 포트로 접속. 수신용 messageListener 스레드 동작. 송신 스트림 설정
		MyConnector(Operator _o) {
			try {
				mainOperator = _o;
				socket = new Socket("localhost", portNum); // 열린 포트로 접속
				msgListener = new MessageListener(this.socket, mainOperator);
				msgListener.start();
				outStream = socket.getOutputStream();
				dataOutStream = new DataOutputStream(outStream);
			} catch (SocketException e) {
				// 소켓 통신오류 = 서버 닫혀있음
				System.out.println("서버가 닫혀있습니다. 클라이언트를 종료합니다.");
				e.printStackTrace();
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 접속 종료 정보 전달 메소드
		 */
		public void sendQuitInformation() {
			try {
				dataOutStream.writeUTF(QuitTag + TK);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 채팅 메시지 전달 메소드
		 * 
		 * @param String: 입력한 채팅 메시지
		 */
		public void sendMessage(String message) {
			try {
				dataOutStream.writeUTF(chatTag + TK + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
}
