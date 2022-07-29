package di;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.my.dto.Product;
import com.my.exception.FindException;
import com.my.repository.ProductRepository;
import com.my.service.CustomerService;
import com.my.service.ProductService;

public class Test {

	public static void main(String[] args) {
		Product p1, p2;
		// 스프링 컨테이너 구동
		// spring용 라이브러리를 다운받기 위해 
		
		// product객체를 찾아서 사용하기
		// 계속해서 객체를 생성시키는 것이 아니라 찾아서 사용하는 것임
		String configurationPath = "configuration2.xml";
		ClassPathXmlApplicationContext ctx;
		
		ctx = new ClassPathXmlApplicationContext(configurationPath);
		p1 = ctx.getBean("p", Product.class);// 첫번째 인자에는 class name을 설정, 두번째 인자에는 캐스팅할 타입
		System.out.println(p1.hashCode());
		p2 = ctx.getBean("p", Product.class);
		// p라는 객체를 getBean을 이용하여 같은 객체를 바라보도록 참조함
		System.out.println(p2.hashCode());
		System.out.println(p1 == p2); // 같은 객체임
		
		CustomerService service = ctx.getBean("customerService", CustomerService.class);
		System.out.println(service.hashCode());
		
		ProductService service2 = ctx.getBean("productService", ProductService.class);
		System.out.println(service2);
		ProductRepository r1 = service2.getRepository();
		
		ProductRepository r2 = ctx.getBean("productRepository", ProductRepository.class);
		System.out.println(r1 == r2);
		
		try {
			Product p = r2.selectByProdNo("C0001");
			System.out.println(p);
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}