package edu.anno;

public @interface DefaultConfig {
	public enum Severity { CRITICAL, IMPORTANT, TRIVIAL, DOCUMENTATION };
	Severity seveirty() default Severity.IMPORTANT;
	String item();
	String assignedTo();
	String dateAssigned();
}
