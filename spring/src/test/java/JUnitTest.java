import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JUnitTest {

	@Autowired
	ApplicationContext context;
	
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	static ApplicationContext contextObject = null;
	
	@Test
	public void test01(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		assertThat(contextObject == null || contextObject == this.context, is(true));
		contextObject = this.context;
	}
}
