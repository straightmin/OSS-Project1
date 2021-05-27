import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

	}

}
