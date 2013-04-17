package edu.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.dao.UserDao;
import edu.user.SqlRetrievalFailureException;
import edu.user.SqlService;
import edu.user.jaxb.SqlType;
import edu.user.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {

	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public XmlSqlService() {
		String contextPath = Sqlmap.class.getPackage().getName();
		try{
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = getClass().getResourceAsStream("sqlmap.xml");
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()){
				this.sqlMap.put(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e){
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = this.sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		else
			return sql;
	}

}
