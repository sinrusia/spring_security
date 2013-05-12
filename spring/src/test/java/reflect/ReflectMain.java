package reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectMain {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
		Class getClass1 = Class.forName("reflect.ReflectMethod");
		Class getClass2 = ReflectMethod.class;
		Class getClass3 = new ReflectMethod().getClass();
		
		// �����ڿ� ���ڰ� ���� ���
		Object object1 = getClass1.newInstance();
		
		// �����ڿ� ���ڰ� ���� ���
		Class[] constructorParamClass = new Class[] {String.class};
		Object[] constructorParamObject = new Object[] {"Reflect Test!!!"};
		Constructor constructor = getClass1.getConstructor(constructorParamClass);
		Object object2 = constructor.newInstance(constructorParamObject);
		
		Field field = getClass1.getField("field");
		
		Object fieldValue = field.get(object2);
		
		System.out.println(fieldValue);
		
		
	}
}
