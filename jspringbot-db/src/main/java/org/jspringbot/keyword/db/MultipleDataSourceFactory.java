package org.jspringbot.keyword.db;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;

public class MultipleDataSourceFactory extends AbstractFactoryBean<DataSource> implements MethodInterceptor {

    private BasicDataSourceManager manager;

    @Required
    public void setManager(BasicDataSourceManager manager) {
        this.manager = manager;
    }

    public DataSource getCurrentDataSource() {
        return manager.getCurrentDataSource();
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    protected DataSource createInstance() throws Exception {
        return ProxyFactory.getProxy(DataSource.class, this);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.getMethod().invoke(getCurrentDataSource(), methodInvocation.getArguments());
    }
}
