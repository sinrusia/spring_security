package edu.user;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
