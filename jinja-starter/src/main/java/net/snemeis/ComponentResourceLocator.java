package net.snemeis;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.loader.ResourceLocator;
import com.hubspot.jinjava.loader.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ComponentResourceLocator implements ResourceLocator {
    private final Map<String, String> components = new HashMap<>();

    /**
     * for debugging
     * @return count of all registered components
     */
    public int getCmpSize() {
        return components.size();
    }

    /**
     *
     * @param name name of component
     * @param template template content string
     */
    public void addComponent(String name, String template) {
        components.put(name, template);
    }

    /**
     *
     * @param name name of the component to find
     * @return layout string of the component
     */
    public Optional<String> findComponent(String name) {
        return Optional.ofNullable(components.get(name));
    }

    @Override
    public String getString(String fullName, Charset encoding, JinjavaInterpreter interpreter) throws ResourceNotFoundException {
        log.info("getting template: {} ", fullName);
        return findComponent(fullName).orElseThrow(() -> new ResourceNotFoundException("resource could not been found"));
    }
}
