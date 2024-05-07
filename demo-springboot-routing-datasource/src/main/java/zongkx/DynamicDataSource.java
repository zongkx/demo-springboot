package zongkx;//在Spring 2.0.1中引入了AbstractRoutingDataSource, 该类充当了DataSource的路由中介, 能有在运行时, 根据某种key值来动态切换到真正的DataSource上。

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class DynamicDataSource extends AbstractRoutingDataSource {
	@Autowired
	private UserSession userSession;

	@Autowired
	@Qualifier("selectDataSource")
	private DataSource selectDataSource;
 
	@Autowired
	@Qualifier("updateDataSource")
	private DataSource updateDataSource;
 
	/**
	 * 配置初始数据源信息
	 */
	@Override
	public void afterPropertiesSet() {
		Map<Object, Object> map = new HashMap<>();
		map.put("selectDataSource", selectDataSource);
		map.put("updateDataSource", updateDataSource);
		setTargetDataSources(map);
		setDefaultTargetDataSource(updateDataSource);
		super.afterPropertiesSet();
	}
 
    /**
	 * 这个是主要的方法，根据key执行value中的数据源
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return userSession.get();
	}
}