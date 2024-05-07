package zongkx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * @author zongkxc
 */

@RestController
public class Api {

    @Autowired
    private DataSource dataSource;




}
