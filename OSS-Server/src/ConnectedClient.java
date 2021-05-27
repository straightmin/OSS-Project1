import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

/**
 * [GUI 기반 화상통화 프로그램] ConnectedClient: 접속 클라이언트 저장용 스레드 각 스레드는 클라이언트와 1대1로
 * 소켓통신하며 클라이언트가 송신한 메시지를 수신하여 구분한 후 메시지에 알맞는 동작을 수행한다.
 */

public class ConnectedClient extends Thread {
	Socket socket;
	OutputStream outStream;
	DataOutputStream dataOutStream;
	InputStream inputStream;
	DataInputStream dataInStream;
	String msg;
	String uName;
	Server server;

	// 메시지 구분용 구문 문자
	private final String QuitTag = "QUIT";
	private final String connectTag = "CONNECT";
	private final String listTag = "LIST";
	private final String chatTag = "CHAT";
	private final String TK = "@";

	ConnectedClient(Socket _s, Server _ss, int number) {
		this.socket = _s;
		this.server = _ss;
		this.uName = "익명 " + number;
	}

	public void run() {
		try {
			System.out.println("Server > " + this.socket.toString() + "에서의 접속이 연결되었습니다.");
			outStream = this.socket.getOutputStream();
			dataOutStream = new DataOutputStream(outStream);
			inputStream = this.socket.getInputStream();
			dataInStream = new DataInputStream(inputStream);

			// 접속 동작
			String users = "";
			for (ConnectedClient cc : server.clients) { // 접속 회원 리스트 작성
				if (cc.uName == null)
					continue;
				users += TK + cc.uName;
			}
			for (ConnectedClient cc : server.clients) { // 접속 메시지, 회원 리스트 전달
				// 클라이언트 측에서 채팅방에 입장한 회원 파악 위해 이름 전달
				outStream = cc.socket.getOutputStream();
				dataOutStream = new DataOutputStream(outStream);
				dataOutStream.writeUTF(connectTag + TK + uName);
				dataOutStream.writeUTF(listTag + users);
			}

			// 모든 클라이언트에 접근한 후 엉뚱한 곳에 연결되기 때문에 아웃풋 스트림 재설정
			outStream = this.socket.getOutputStream();
			dataOutStream = new DataOutputStream(outStream);

			while (true) {
				msg = dataInStream.readUTF();
				StringTokenizer st = new StringTokenizer(msg, TK);
				String control = st.nextToken(); // 명령메시지 분리, switch를 통해 기능 구분

				switch (control) { // 명령 메시지에 따라 기능 구분
				case chatTag: // 채팅 메시지
					msg = st.nextToken();

					// 송신한 클라이언트를 포함 모든 클라이언트에게 채팅 메시지 전달
					for (ConnectedClient cc : server.clients) {
						outStream = cc.socket.getOutputStream();
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF(chatTag + TK + uName + " : " + msg);
					}

					break;
				case QuitTag: // 클라이언트종료

					// 다른 모든 클라이언트에게 클라이언트종료시 정보 전달
					for (ConnectedClient cc : server.clients) {
						if (uName.equals(cc.uName))
							continue;
						outStream = cc.socket.getOutputStream();
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF(QuitTag + TK + uName);
					}
					uName = null; // 회원 리스트 무결성을 위해 클라이언트종료시 이름 비우기
					break;
				}
				// switch(control)

				// 모든 클라이언트에 접근한 후 엉뚱한 곳에 연결되기 때문에 아웃풋 스트림 재설정
				outStream = this.socket.getOutputStream();
				dataOutStream = new DataOutputStream(outStream);
			}
			// while(true)

		} catch (SocketException e) {
			System.out.println("Server > " + this.socket.toString() + "에서의 접속이 해제되었습니다.");
		} catch (IOException e) {
			System.out.println("Server > 입출력 관련 예외 발생");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("최종? 예외");
			e.printStackTrace();
		} finally {
			// 오류 또는 클라이언트 종료로 인해 소켓 연결이 해제되면 서버와 연결된 클라이언트 정보 삭제, 모든 스트림 페쇄
			server.clients.remove(server.clients.indexOf(this));
			try {
				dataInStream.close();
				inputStream.close();
				dataOutStream.close();
				outStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// try ~ catch

	}
	// run()

}
