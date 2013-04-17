import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import edu.user.jaxb.SqlType;
import edu.user.jaxb.Sqlmap;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class JaxbTest {

	@Test
	public void readSqlmap() throws JAXBException, IOException{
		String contextpath = Sqlmap.class.getPackage().getName();
		// ���ε��� Ŭ������ ��ġ�� ������ JAXB ���ؽ�Ʈ�� �����.
		JAXBContext context = JAXBContext.newInstance(contextpath);
		// create Unmarshaller
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// �𸶼��� �ϸ� ���ε� ������Ʈ Ʈ���� ��Ʈ�� Sqlmap�� �����ش�.
		Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));
		
		List<SqlType> sqlList = sqlmap.getSql();
		
		assertThat(sqlList.size(), is(6));
		
	}
}
