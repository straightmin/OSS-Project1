import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * [GUI 기반 화상통화 프로그램] Server: 통화 프로그램 메인 서버 클라이언트 접속 대기, 클라이언트 객체 저장 및 관리
 * 
 * 작성자 : 이종영
 */

public class Server {
	// 서버소켓
	ServerSocket ss = null;
	// 접속한 클라이언트 정보들을 어레이리스트로 여러개 저장 및 관리
	ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
	// 포트번호 상수. 7777번으로 설정
	final static int portNum = 7777;

	// 메인 메소드. 클라이언트 접속 상시대기, 클라이언트 스레드 저장 및 동작
	public static void main(String[] args) {

		int count = 0; // 접속한 인원 구분용 카운트 변수
		Server server = new Server();

		try {
			server.ss = new ServerSocket(portNum);  //포트개방
			System.out.println("Server > Server Socket is Created ...");
			while (true) {
				// 클라이언트 접속 대기
				Socket socket = server.ss.accept();
				ConnectedClient c = new ConnectedClient(socket, server, count++);
				server.clients.add(c);
				c.start();
			}
		} catch (SocketException e) {
			System.out.println("Server > 소켓 관련 예외 발생. 서버종료");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Server > 입출력 관련 예외 발생");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("일반 예외 발생");
			e.printStackTrace();
		}

	}

}
