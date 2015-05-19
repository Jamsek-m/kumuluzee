package com.kumuluz.ee.jetty;

import com.kumuluz.ee.common.ServletServer;
import com.kumuluz.ee.common.exceptions.ServletServerException;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.util.logging.Logger;

/**
 * @author Tilen
 */
public class JettyServletServer implements ServletServer {

    Logger log = Logger.getLogger(JettyServletServer.class.getSimpleName());

    private Server server;
    private WebAppContext appContext;

    public JettyServletServer() {
    }

    @Override
    public void initServer() {

        server = createJettyFactory().create();

        log.info(getServerName() + " initiated");
    }

    @Override
    public void startServer() {

        if (server == null) {

            String msg = "Jetty has to be initialized before starting it";

            log.severe(msg);

            throw new IllegalStateException(msg);
        }

        if (server.isStarted() || server.isStarting()) {

            String msg = "Jetty is already started";

            log.severe(msg);

            throw new IllegalStateException(msg);
        }

        try {
            server.start();
        } catch (Exception e) {

            log.severe(e.getMessage());

            throw new ServletServerException(e.getMessage(), e.getCause());
        }

        log.info(getServerName() + " started");
    }

    @Override
    public void stopServer() {

        if (server == null) {

            String msg = "Jetty has to be initialized before stopping it";

            log.severe(msg);

            throw new IllegalStateException(msg);
        }

        if (server.isStarted() || server.isStarting()) {

            String msg = "Jetty is already stopped";

            log.severe(msg);

            throw new IllegalStateException(msg);
        }

        try {
            server.stop();
        } catch (Exception e) {

            log.severe(e.getMessage());

            throw new ServletServerException(e.getMessage(), e.getCause());
        }

        log.info(getServerName() + " stopped");
    }

    @Override
    public void initWebContext() {

        appContext = new WebAppContext();

        appContext.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });

        appContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/classes/.*");

        appContext.setParentLoaderPriority(true);

        appContext.setContextPath("/");
        appContext.setResourceBase("src/main/resources");

        server.setHandler(appContext);
    }

    @Override
    public String getServerName() {

        return "Jetty";
    }

    private JettyFactory createJettyFactory() {

        return new JettyFactory();
    }
}