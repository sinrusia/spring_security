import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import edu.ioc.bean.Hello;
import edu.ioc.bean.Printer;
import edu.ioc.bean.StringPrinter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IocTest {
	
	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void registerBeanWithDependency() {
		// IoC �����̳� ����, ����ȭ ���ÿ� �����̳ʷ� �����Ѵ�.
		
		// generate Application Context
		StaticApplicationContext ac = new StaticApplicationContext();
		
		// StringPrinter Ŭ���� Ÿ���̸� printer��� �̸��� ���� ���� ����Ѵ�.
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		// �ܼ����� ���� ������Ƽ ���
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		// ���̵� printer�� �󿡴��� ���۷����� ������Ƽ�� ���
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		// register Hello Class
		ac.registerBeanDefinition("hello", helloDef);
		
		
		
		// IoC �����̳ʰ� ����� ���� �����ߴ��� Ȯ���ϱ� ���� ���� ��û�ϰ� null�� �ƴ��� Ȯ���Ѵ�.
		// generate Hello Class
		Hello hello = ac.getBean("hello", Hello.class);
		
		hello.print();
		
		// check hello instance
		//assertThat(hello, is(notNullValue()));
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericApplicationContext() {
		GenericApplicationContext ac = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
		
		reader.loadBeanDefinitions("IocTest-context.xml");
		
		Hello hello = ac.getBean("hello", Hello.class);
		
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello parent"));
	}
	
	@Test
	public void childParentTest() {
		ApplicationContext parent = new GenericXmlApplicationContext("IocTest-context.xml");
		GenericApplicationContext child = new GenericApplicationContext(parent);
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
		reader.loadBeanDefinitions("ChildContext.xml");
		child.refresh();
		
		Printer printer = child.getBean("printer", Printer.class);
		
		assertThat(printer, is(notNullValue()));
		
		Hello hello = child.getBean("hello", Hello.class);
		assertThat(hello, is(notNullValue()));
		
		hello.print();
		assertThat(printer.toString(), is("Hello Child"));
		
	}
}
