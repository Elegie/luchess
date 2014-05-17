package io.elegie.luchess.web.launcher.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.web.launcher.services.api.WebApp;
import io.elegie.luchess.web.launcher.services.api.WebAppService;

import java.io.File;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class AnyWarWebAppServiceTest {

    private static final String EMPTY_DIR = AnyWarWebAppServiceTest.class.getResource("/empty").getFile();
    private static final String ARCHIVES_DIR = AnyWarWebAppServiceTest.class.getResource("/archives").getFile();
    private static final String ARCHIVES_JAR = ARCHIVES_DIR + "/foo.jar";
    private static final String ARCHIVES_WAR = ARCHIVES_DIR + "/foo.war";

    @Test
    public void testGetWebAppFromEmpty() {
        assertNull(new AnyWarWebAppService(EMPTY_DIR).getWebApp());
    }

    @Test
    public void testGetWebAppFromDir() {
        testGetWebApp(new AnyWarWebAppService(ARCHIVES_DIR));
    }

    @Test
    public void testGetWebAppFromJar() {
        testGetWebApp(new AnyWarWebAppService(ARCHIVES_JAR));
    }

    private void testGetWebApp(WebAppService webAppService) {
        WebApp webApp = webAppService.getWebApp();
        assertNotNull(webApp);
        assertEquals("/", webApp.getContextPath());
        assertEquals(new File(ARCHIVES_WAR).getAbsolutePath(), webApp.getWarPath());
    }

}
