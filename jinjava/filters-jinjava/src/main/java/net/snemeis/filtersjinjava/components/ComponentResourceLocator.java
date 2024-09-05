package net.snemeis.filtersjinjava.components;

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
     *
     * @param name name of component
     * @param template template content string
     */
    public void addComponent(String name, String template) {
        this.components.put(name, template);
    }

    public int getComponentsCount() {
        return this.components.size();
    }

    /**
     *
     * @param name name of the component to find
     * @return layout string of the component
     */
    public Optional<String> findComponent(String name) {
        return Optional.ofNullable(this.components.get(name));
    }

    @Override
    public String getString(String fullName, Charset encoding, JinjavaInterpreter interpreter) throws ResourceNotFoundException {
        log.info("getting template: {} ", fullName);
        return findComponent(fullName).orElseThrow(() -> new ResourceNotFoundException("resource could not been found"));
    }
}
