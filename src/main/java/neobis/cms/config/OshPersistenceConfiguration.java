package neobis.cms.Config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "oshEntityManagerFactory",
        transactionManagerRef = "oshTransactionManager",
        basePackages = {"neobis.cms.Repo.Osh"})
public class OshPersistenceConfiguration {


    @Bean(name = "oshDataSource")
    @ConfigurationProperties(prefix = "osh.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oshEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oshEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("oshDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("neobis.cms.Entity.Osh").persistenceUnit("osh").build();
    }

    @Bean(name = "oshTransactionManager")
    public PlatformTransactionManager oshTransactionManager(
            @Qualifier("oshEntityManagerFactory") EntityManagerFactory oshEntityManagerFactory) {
        return new JpaTransactionManager(oshEntityManagerFactory);
    }

}
