import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wemb.MyBean;


public class ContainerTest {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		MyBean bean = context.getBean("myBeam", MyBean.class);
		bean.call();
		
		assertEquals("A", "A");
	}

}
