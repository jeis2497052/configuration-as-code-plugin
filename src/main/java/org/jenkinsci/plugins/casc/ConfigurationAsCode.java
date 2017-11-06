package org.jenkinsci.plugins.casc;

import hudson.Plugin;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import jenkins.model.Jenkins;
import org.yaml.snakeyaml.Yaml;
import javax.servlet.ServletException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class ConfigurationAsCode extends Plugin {


    @Initializer(after = InitMilestone.EXTENSIONS_AUGMENTED)
    public static void configure() throws Exception {
        final File f = getConfigFile("JENKINS_CONF");
        if (f.exists()) {
            configure(f);
        }
    }

    public static void configure(File f) throws Exception {

        Map<String, Object> config = getConfigYaml(f, Map.class);
        for (Map.Entry<String, Object> e : config.entrySet()) {
            final Configurator configurator = Configurator.lookupRootElement(e.getKey());
            if (configurator == null) {
                throw new IllegalArgumentException("no configurator for root element "+e.getKey());
            }
            configurator.configure(e.getValue());
        }
    }

    public List<?> getConfigurators() {
        List<Object> elements = new ArrayList<>();
        for (RootElementConfigurator c : Jenkins.getInstance().getExtensionList(RootElementConfigurator.class)) {
            elements.add(c);
            listElements(elements, c.describe());
        }
        return elements;
    }

    private void listElements(List<Object> elements, Set<Attribute> attributes) {
        for (Attribute attribute : attributes) {

            final Class type = attribute.type;
            Configurator configurator = Configurator.lookup(type);
            if (configurator == null ) {
                continue;
            }
            for (Object o : configurator.getConfigurators()) {
                if (!elements.contains(o)) {
                    elements.add(o);
                }
            }
            listElements(elements, configurator.describe());
        }
    }




    private final List<Class> documented = new ArrayList<>();
    {

        documented.add(int.class);
        documented.add(String.class);
        documented.add(boolean.class);
        documented.add(Integer.class);
        // ...

        for (RootElementConfigurator c : Jenkins.getInstance().getExtensionList(RootElementConfigurator.class)) {
            final String name = c.getName();
            document(name, c.describe());
        }
    }

    private void document(String name, Set<Attribute> attributes) {

        Set<Class> next = new HashSet<>();
        System.out.println();
        System.out.println("## " + name);
        for (Attribute attribute : attributes) {

            // FIXME filter attribute to target a component without anything configurable

            final Class type = attribute.getType();
            System.out.print("**"+attribute.getName() + "**  (");
            if (attribute.isMultiple())
                System.out.print("list of ");
            System.out.println(type+")");
            if (!attribute.possibleValues().isEmpty()) {
                System.out.println("possible values :");
                for (Object o : attribute.possibleValues()) {
                    System.out.println(" - " + o);
                }
            }

            if (! documented.contains(type)) {
                next.add(type);
            }
        }

        for (Class type : next) {
            Configurator configurator = Configurator.lookup(type);
            if (configurator == null) continue;
            document(type.getName(), configurator.describe());
        }
    }

    @Initializer(after = InitMilestone.EXTENSIONS_AUGMENTED)
    public static void installPlugins() throws IOException, ServletException, InterruptedException {
        // TODO get version added to the install of the plugin so we can control the specific version
        Jenkins.getInstance().pluginManager.doCheckUpdatesServer();
        Collection<String> plugins = getConfigYaml(getConfigFile("JENKINS_PLUGINS"), ArrayList.class);
        Jenkins.getInstance().pluginManager.install(plugins, true);
    }

    private static <T> T getConfigYaml(File file, Class<T> type) throws FileNotFoundException{

        if(!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " Was not found, Check path or spelling");
        }
            return new Yaml().loadAs(new FileInputStream(file), type);
    }

    private static File getConfigFile(String envName) throws MalformedURLException, IOException {
        // TODO Fix this code as this is just for the MVP and is very verbose and probably a ugly solution
        BufferedWriter out = null;
        String envVar = System.getenv(envName);
        if(envVar != null) {
            if (envVar.contains("http")) {
                File file;
                String ymlText = "";
                URL url = new URL(envVar);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        ymlText += line + "\n";
                    }
                    File temp = File.createTempFile("something", ".yaml");
                    out = new BufferedWriter(new FileWriter(temp));
                    out.write(ymlText);
                    file = temp;
                    return file;
                } catch (IOException e) {
                    System.out.println(e);
                } finally {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            }
            return new File(envVar);
        }
        throw new FileNotFoundException("[ERROR] " + envName +  " variable is not set or the URL/URi is wrong. Can't complete setup");
    }
}
