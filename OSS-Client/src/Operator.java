/**
 * [GUI 기반 화상통화 프로그램]
 * Operator: 클라이언트 단에서 모든 프레임을 관리하는 클래스
 * 모든 클래스를 하나의 인스턴스로 묶어서 관리한다.
 * 
 * 작성자 : 2017243055 최백균 / 작성날짜 : 2021.05.27
 */

public class Operator {

	// 모든 프레임과 커넥터를 필드로 관리
	MainFrame mf = null;
	MyConnector connector = null;

	public static void main(String[] args) {
		
		// operator 인스턴스 생성 후 모든 클래스에 인스턴스 전달, 각 클래스에서 인스턴스 활용
		Operator operator = new Operator();
		operator.mf = new MainFrame(operator);
		operator.connector = new MyConnector(operator);
	}
}
