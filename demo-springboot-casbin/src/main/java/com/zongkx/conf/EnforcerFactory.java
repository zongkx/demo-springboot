package com.zongkx.conf;

import com.zongkx.dao.PolicyDAO;
import com.zongkx.model.Policy;
import lombok.RequiredArgsConstructor;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class EnforcerFactory implements InitializingBean {

    private  Enforcer enforcer;
    private final JDBCAdapter jdbcAdapter;
    private final PolicyDAO dao;
    @Override
    public void afterPropertiesSet() throws Exception {
        Model model = new Model();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:/model.conf");
        model.loadModel(resource.getFile().getCanonicalPath());
        enforcer = new Enforcer(model, jdbcAdapter);
        List<Policy> all = dao.findAll();
        for (Policy policy : all) {
            this.addPolicy(policy.getSub(),policy.getObj(),policy.getAct());
        }
    }

    /**
     * 权限校验
     */
    public boolean enforce(Object... rvals) {
        return enforcer.enforce(rvals);
    }

    /**
     * 添加权限策略
     *
     */
    public boolean addPolicy(String sub,String obj,String act) {
        return enforcer.addPolicy(sub,obj,act);
    }

    /**
     * 删除权限策略
     */
    public boolean removePolicy(String sub,String obj,String act) {
        return enforcer.removePolicy(sub,obj,act);
    }

    /**
     * 权限是否存在
     */
    public boolean hasPolicy(String sub,String obj,String act) {
        return enforcer.hasPolicy(sub,obj,act);
    }


    /**
     * 添加角色继承
     */
    public boolean addRoleForUser(String rSub, String pSub) {
        return enforcer.addRoleForUser(rSub,pSub);
    }


    /**
     * 获取角色下的权限
     */
    public List<String> getRolesForPolicy(String sub) {
        return enforcer.getRolesForUser(sub);
    }

    /**
     * 移除角色权限
     * @param rSub 角色名
     * @param pSub 权限名
     * @return 是否继承
     */
    public boolean deleteRoleForUser(String rSub,String pSub){
        return enforcer.deleteRoleForUser(rSub, pSub);
    }
}
