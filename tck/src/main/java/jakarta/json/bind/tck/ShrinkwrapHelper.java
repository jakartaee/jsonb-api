package jakarta.json.bind.tck;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class ShrinkwrapHelper {
    
    public static WebArchive createDefaultWar(Class<?> testClass) {
        return ShrinkWrap.create(WebArchive.class, testClass.getSimpleName() + ".war")
                .addPackages(true, testClass.getPackage().getName());
    }
    
//    public static WebArchive buildDefaultApp(String appName, String... packages) throws Exception {
//        String appArchiveName = appName.endsWith(".war") ? appName : appName + ".war";
//        WebArchive app = ShrinkWrap.create(WebArchive.class, appArchiveName);
//        for (String p : packages) {
//            if (p.endsWith(".*"))
//                app = app.addPackages(true, p.replace(".*", ""));
//            else
//                app = app.addPackages(false, p);
//        }
//        String testAppResourcesDir = (appPath == null ? "" : appPath) + "test-applications/" + appName + "/resources/";
//        if (new File(testAppResourcesDir).exists()) {
//            app = (WebArchive) addDirectory(app, testAppResourcesDir);
//        }
//        return app;
//    }
    
    /**
     * Recursively adds a folder and all of its contents to an archive.
     *
     * @param a The archive to add the files to
     * @param dir The directory which will be recursively added to the archive.
     */
    public static Archive<?> addDirectory(Archive<?> a, String dir) throws Exception {
        return addDirectory(a, dir, Filters.includeAll());
    }

    /**
     * Recursively adds a folder and all of its contents matching a filter to an archive.
     *
     * @param a The archive to add the files to
     * @param dir The directory which will be recursively added to the archive.
     * @param filter A filter indicating which files should be included in the archive
     */
    public static Archive<?> addDirectory(Archive<?> a, String dir, Filter<ArchivePath> filter) throws Exception {
        return a.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(dir).as(GenericArchive.class),
                       "/", filter);
    }

}
