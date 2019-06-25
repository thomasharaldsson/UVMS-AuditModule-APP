package eu.europa.ec.fisheries.uvms.audit.service;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@ArquillianSuiteDeployment
public abstract class BuildAuditServiceTestDeployment {

    final static Logger LOG = LoggerFactory.getLogger(BuildAuditServiceTestDeployment.class);

    @Deployment(name = "auditservice", order = 1)
    public static Archive<?> createDeployment() {
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "auditservice.war");

        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile();
        testWar.addAsLibraries(files);

        files = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.audit:audit-message").withTransitivity().asFile();
        testWar.addAsLibraries(files);
        
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.audit.service");


        testWar.addAsWebInfResource("ejb-jar.xml");
        testWar.addAsResource("persistence-integration.xml", "META-INF/persistence.xml");
        testWar.addAsResource("beans.xml", "META-INF/beans.xml");

		return testWar;
	}
}
