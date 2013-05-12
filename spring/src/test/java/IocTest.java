import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import edu.ioc.bean.Hello;

public class IocTest {

	@Test
	public void registerBeanWithDependency() {
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		Hello hello1 = ac.getBean("hello1", Hello.class);
		assertThat(hello1, is(notNullValue()));
	}
}
