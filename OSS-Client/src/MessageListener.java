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
 * 작성자: 2017243078 민종현	/	작성날짜: 21.05.17
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

		try {
			inStream = socket.getInputStream();
			dataInStream = new DataInputStream(inStream);
			
			/* ~~~
			 * 소스코드 작성
			 * ~~~
			 */
			
		} catch (SocketException e) {
			System.out.println("소켓오류. 클라이언트 종료.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
