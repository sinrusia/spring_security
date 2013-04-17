package edu.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.dao.UserDao;
import edu.user.SqlRetrievalFailureException;
import edu.user.SqlService;
import edu.user.jaxb.SqlType;
import edu.user.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String sqlmapFile;
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	public XmlSqlService() {
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = this.sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		else
			return sql;
	}
	
	@PostConstruct
	public void loadSql() {
		logger.debug("init start");
		String contextPath = Sqlmap.class.getPackage().getName();
		try{
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = getClass().getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()){
				this.sqlMap.put(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e){
			throw new RuntimeException(e);
		}
	}

}
