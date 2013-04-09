import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import edu.pointcut.Target;

public class PointcutExpressionTest {

	@Test
	public void methodSignaturePointcut() throws SecurityException,
			NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public int edu.pointcut.Target.minus(int, int) throws java.lang.RuntimeException)");

		//
		assertThat(pointcut.getClassFilter().matches(Target.class)
						&& pointcut.getMethodMatcher().matches(
								Target.class.getMethod("minus", int.class, int.class), null), is(true));

		//

		//
	}
}
