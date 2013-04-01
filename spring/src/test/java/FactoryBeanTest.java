import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.message.Message;
import edu.message.MessageFactoryBean;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FactoryBeanTest {
	
	@Autowired
	ApplicationContext context;
	
	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		assertThat(message, is(Message.class));
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}
	
	@Test
	public void getFactoryBean() throws Exception {
		Object factory = context.getBean("&message");
		assertThat(factory, is(MessageFactoryBean.class));
	}

}
