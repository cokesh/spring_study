package backboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.my.dto.Board;
@RunWith(SpringRunner.class) 
// SpringRunner : 스프링 프레임워크의 테스트 모듈에서 제공되는 라이브러리
// 톰캣 키지 않아도 스프링 컨테이너 구동될 수 있도록 함
@ContextConfiguration(locations= {
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
public class LombokTest {
	@Autowired
	Board board;
	@Test(expected = NullPointerException.class) // null값을 넣으면 NullPointerException이 발생한다고 단정 지음
	// test 실행 해 보면 통과가 됨(NullPointerException 발생한다고 단정 지었기 때문)
	public void test() {
		board.setBoardNo(1);
		board.setBoardId(null); // null값을 넣으면 non-null인데 null 넣었다고 테스트 오류남 (NullPointerException)
		// null값을 넣으면 nullPointerException이 발생한다고 예상함
		System.out.println(board.toString());
	}

}
