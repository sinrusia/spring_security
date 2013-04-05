import java.lang.reflect.Proxy;

import org.junit.Test;

import reflection.Hello;
import reflection.HelloTarget;
import reflection.HelloUppercase;
import reflection.UppercaseHandler;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class HelloTest {

	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
	}

	@Test
	public void upperProxy() {
		Hello hello = new HelloUppercase(new HelloTarget());
		assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(hello.sayHi("Toby"), is("HI TOBY"));
		assertThat(hello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	@Test
	public void methodInvoke() {
		Hello proxieHello = (Hello) Proxy.newProxyInstance(getClass()
				.getClassLoader(), new Class[] { Hello.class },
				new UppercaseHandler(new HelloTarget()));
		assertThat(proxieHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxieHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxieHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
}
