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
		// 바인딩용 클래스들 위치를 가지고 JAXB 컨텍스트를 만든다.
		JAXBContext context = JAXBContext.newInstance(contextpath);
		// create Unmarshaller
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// 언마샬을 하면 매핑된 오브젝트 트리의 루트인 Sqlmap을 돌려준다.
		Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));
		
		List<SqlType> sqlList = sqlmap.getSql();
		
		assertThat(sqlList.size(), is(6));
		
	}
}
