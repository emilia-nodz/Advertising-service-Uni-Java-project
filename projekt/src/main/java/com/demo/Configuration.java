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

@JMSDestinationDefinition(
        name = "java:app/jms/queue1",
        interfaceName = "jakarta.jms.Queue",
        destinationName = "queue1")
@JMSConnectionFactoryDefinition(name="java:app/jms/cf1")

@FacesConfig
@Singleton
@Startup
public class Configuration {

}

