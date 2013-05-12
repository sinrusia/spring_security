package reflect;

public class ReflectMethod {
	// field
	public String field;
	
	public ReflectMethod() {
		// TODO Auto-generated constructor stub
	}
	
	public ReflectMethod(String field) {
		this.field = field;
	}
	
	public String getField() {
		System.out.println("call field : " + this.field);
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public void methodA() {
		System.out.println("call method A");
	}
	
	public void methodB(String str) {
		System.out.println("call method B : " + str);
	}
	
	public String methodC() {
		System.out.println("call method C");
		return "method C";
	}
}
