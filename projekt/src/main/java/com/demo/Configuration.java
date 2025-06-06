package com.demo;

import com.demo.dao.UserDAO;
import com.demo.models.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;
import java.util.Map;

import static com.demo.models.UserRole.USER;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "",
                useForwardToLogin = false
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:global/JsfCrudDemoDataSource",
        callerQuery = "SELECT password FROM \"USER\" WHERE login = ?",
        groupsQuery = "SELECT ug.name FROM \"USER\" u JOIN USERGROUP ug ON u.id=ug.user_id WHERE u.login = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
                "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
                "Pbkdf2PasswordHash.Iterations=3072",
                "Pbkdf2PasswordHash.SaltSizeBytes=64",
        }

)
@JMSDestinationDefinition(
        name = "java:app/jms/queue1",
        interfaceName = "jakarta.jms.Queue",
        destinationName = "queue1")
@JMSConnectionFactoryDefinition(name="java:app/jms/cf1")

@FacesConfig
@Singleton
@Startup
public class Configuration {

    @Inject
    private Pbkdf2PasswordHash pbkdf;

    @EJB
    private UserDAO userDao;

    @PostConstruct
    private void init() {
        Map<String,String> map = new HashMap<>();
        map.put("Pbkdf2PasswordHash.Algorithm","PBKDF2WithHmacSHA512");
        map.put("Pbkdf2PasswordHash.Iterations","3072");
        map.put("Pbkdf2PasswordHash.SaltSizeBytes","64");
        pbkdf.initialize(map);

        initDatabase();
    }

    private void initDatabase() {
        User u = new User();
        u.setUsername("test");
        u.setPassword(pbkdf.generate("test".toCharArray()));
        u.setEmail("test@test.pl");
        u.setName("Ania");
        u.setSurname("Kowal");
        u.setRole(USER);
        userDao.save(u);
    }
}

