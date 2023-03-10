//package org.nampython.center.dispatcher;
//
//import com.cyecize.ioc.annotations.Autowired;
//import org.nampython.annotation.JavacheComponent;
//import org.nampython.config.JavacheConfigService;
//import org.nampython.config.JavacheConfigValue;
//import org.nampython.center.dispatcher.services.HttpSolet;
//import org.nampython.io.JarFileUnzipService;
//import org.nampython.log.LoggingService;
//import org.nampython.support.IoC;
//import org.nampython.util.PathUtils;
//import org.nampython.util.ReflectionUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//@JavacheComponent
//public class ApplicationScanningServiceImpl implements ApplicationScanningService{
//    private final List<String> applicationNames;
//    private final JarFileUnzipService jarFileUnzipService;
//    private final String applicationsFolderPath;
//    private final JavacheConfigService configService;
//    private final boolean skipExtractingAppsWithExistingFolder;
//    private final String compileOutputFolderName;
//    private final String appLibFolderName;
//    private final LibraryLoadingService libraryLoadingService;
//    private final LoggingService loggingService;
//    private final Map<String, List<Class<HttpSolet>>> soletClasses;
//
//
//    @Autowired
//    public ApplicationScanningServiceImpl(JarFileUnzipService jarFileUnzipService, JavacheConfigService configService, LibraryLoadingService libraryLoadingService, LoggingService loggingService) {
//        this.jarFileUnzipService = jarFileUnzipService;
//        this.configService = configService;
//        this.libraryLoadingService = libraryLoadingService;
//        this.loggingService = loggingService;
//        this.applicationsFolderPath = this.getApplicationsFolderPath();
//        this.skipExtractingAppsWithExistingFolder = this.configService.getConfigParam(
//                JavacheConfigValue.BROCOLLINA_SKIP_EXTRACTING_IF_FOLDER_EXISTS,
//                Boolean.class
//        );
//        this.soletClasses = new HashMap<>();
//        this.applicationNames = new ArrayList<>();
//
//        this.compileOutputFolderName = this.configService
//                .getConfigParamString(JavacheConfigValue.APP_COMPILE_OUTPUT_DIR_NAME);
//        this.appLibFolderName = this.configService
//                .getConfigParamString(JavacheConfigValue.APPLICATION_DEPENDENCIES_FOLDER_NAME);
//    }
//
//    @Override
//    public List<String> getApplicationNames() {
//        return null;
//    }
//
//    @Override
//    public Map<String, List<Class<HttpSolet>>> findSoletClasses() throws IOException, ClassNotFoundException {
//        final File applicationsFolder = new File(this.applicationsFolderPath);
//
//        if (applicationsFolder.exists() && applicationsFolder.isDirectory()) {
//            final List<File> allJarFiles = Arrays.stream(Objects.requireNonNull(applicationsFolder.listFiles()))
//                    .filter(this::isJarFile)
//                    .collect(Collectors.toList());
//
//            for (File applicationJarFile : allJarFiles) {
//                final String appName = applicationJarFile.getName().replace(".jar", "");
//                final String extractedJarFolderName = applicationJarFile.getCanonicalPath().replace(".jar", File.separator);
//
//                if (!this.skipExtractingAppsWithExistingFolder || !Files.exists(Paths.get(extractedJarFolderName))) {
//                    this.jarFileUnzipService.unzipJar(
//                            applicationJarFile,
//                            this.configService.getConfigParam(JavacheConfigValue.BROCCOLINA_FORCE_OVERWRITE_FILES, Boolean.class)
//                    );
//                }
//
//                this.loadApplicationFromFolder(extractedJarFolderName, appName);
//            }
//        }
//
//        return this.soletClasses;
//    }
//
//
//    /**
//     * Loads application libraries.
//     * Loads application classes.
//     * Adds the application name to the applicationNames list.
//     */
//    private void loadApplicationFromFolder(String appRootFolderPath, String applicationName) throws IOException {
//        final String classesRootFolderPath = PathUtils.appendPath(appRootFolderPath, this.compileOutputFolderName);
//        final String librariesRootFolderPath = PathUtils.appendPath(appRootFolderPath, this.appLibFolderName);
//
//        final File classesRootDirectory = new File(classesRootFolderPath);
//
//        if (!classesRootDirectory.exists() || !classesRootDirectory.isDirectory()) {
//            return;
//        }
//
//        final List<URL> appLibs = this.collectApplicationLibraries(librariesRootFolderPath);
//        final URLClassLoader classLoader = this.createNewClassLoader(classesRootDirectory.getCanonicalPath(), appLibs);
//
//        final Thread appThread = new Thread(() -> {
//            try {
//                this.loadClass(classesRootDirectory, "", applicationName, classLoader);
//                this.applicationNames.add("/" + applicationName);
//            } catch (ClassNotFoundException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//
//        appThread.setContextClassLoader(classLoader);
//
//        appThread.start();
//        try {
//            appThread.join();
//        } catch (InterruptedException e) {
//            this.loggingService.printStackTrace(e);
//        }
//    }
//
//    private URLClassLoader createNewClassLoader(String canonicalPath, List<URL> urls) {
//        try {
//            urls.add(ReflectionUtils.createDirURL(canonicalPath));
//            return new URLClassLoader(
//                    urls.toArray(URL[]::new),
//                    IoC.getApiClassLoader()
//            );
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    /**
//     * Iterates the given directory's files and filters jar files
//     * then returns a collection of URLs for each library.
//     */
//    private List<URL> collectApplicationLibraries(String librariesRootFolderPath) {
//        final File libraryFolder = new File(librariesRootFolderPath);
//        final List<URL> libraryURLs = new ArrayList<>(this.libraryLoadingService.getLibURLs().values());
//
//        if (!libraryFolder.exists() || !libraryFolder.isDirectory()) {
//            return libraryURLs;
//        }
//
//        Arrays.stream(Objects.requireNonNull(libraryFolder.listFiles()))
//                .filter(this::isJarFile)
//                .forEach(jf -> {
//                    try {
//                        libraryURLs.add(ReflectionUtils.createJarURL(jf.getCanonicalPath()));
//                    } catch (IOException ignored) {
//                    }
//                });
//
//        return libraryURLs;
//    }
//
//
//    /**
//     * Checks if a file's name ends with .jar
//     */
//    private boolean isJarFile(File file) {
//        return file.isFile() && file.getName().endsWith(".jar");
//    }
//
//    private String getApplicationsFolderPath() {
//        return PathUtils.appendPath(
//                this.configService.getConfigParamString(JavacheConfigValue.JAVACHE_WORKING_DIRECTORY),
//                this.configService.getConfigParamString(JavacheConfigValue.WEB_APPS_DIR_NAME)
//        );
//    }
//
//    /**
//     * Recursive method for loading classes, starts with empty packageName.
//     * If the file is directory, iterate all files inside and call loadClass with the current file name
//     * appended to the packageName.
//     * <p>
//     * If the file is file and the file name ends with .class, load it and check if the class
//     * is assignable from {@link HttpSolet}. If it is, add it to the map of solet classes.
//     */
//    private void loadClass(File currentFile, String packageName, String applicationName, URLClassLoader classLoader)
//            throws ClassNotFoundException {
//        if (currentFile.isDirectory()) {
//            for (File childFile : currentFile.listFiles()) {
//                this.loadClass(childFile, (packageName + currentFile.getName() + "."), applicationName, classLoader);
//            }
//        } else {
//            if (!currentFile.getName().endsWith(".class")) {
//                return;
//            }
//
//            final String className = (packageName.replace(this.compileOutputFolderName + ".", "")) + currentFile
//                    .getName()
//                    .replace(".class", "")
//                    .replace("/", ".");
//
//            final Class currentClassFile = Class.forName(className, true, classLoader);
//
//            if (HttpSolet.class.isAssignableFrom(currentClassFile)) {
//                if (!this.soletClasses.containsKey(applicationName)) {
//                    this.soletClasses.put(applicationName, new ArrayList<>());
//                }
//
//                this.soletClasses.get(applicationName).add(currentClassFile);
//            }
//        }
//    }
//}
