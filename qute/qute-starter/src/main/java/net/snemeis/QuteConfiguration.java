package net.snemeis;

import io.quarkus.qute.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Configuration
public class QuteConfiguration {

    @Autowired
    MessageSource messageSource;

    // TODO: Could something like this be used to register global helper methods?
    // @Autowired
    // Map<String, Function> helperMethods;

    /*
        Notes:
        - sections are implemented control flow statements
        -- they could also be custom defined tags (custom/other templates)
     */

    @Bean
    Engine quteEngine() {
        return Engine.builder()
                .addDefaults()
                .addLocator(this::templateLocator)
                .addLocator(this::componentLocator)
                .addValueResolver(ReflectionValueResolver::new)
                .addResultMapper(this::someResultMapper)
                .addValueResolver(this::i18nValueResolver)
//                .addValueResolver(this::helperMethodValueResolver) // TODO: implement this
                .build();
    }

    // map any result to any other string, because the result should change post evaluation ??
    private String someResultMapper(Object o, Expression expression) {
        return null;
    }

    private ValueResolver i18nValueResolver() {
        return new ValueResolver() {
            @Override
            public boolean appliesTo(EvalContext context) {
                return "translate".equals(context.getName());
            }

            @Override
            public CompletionStage<Object> resolve(EvalContext context) {
                var params = context.getParams().stream().map(Expression::getLiteral).toList();
                String code = (String) params.get(0);
                var args = params.subList(1, params.size()).toArray();

                // TODO: how to dynamically change here the locale?
                String message = messageSource.getMessage(code , args, Locale.ENGLISH);

                return CompletedStage.of(new RawString(message));
            }
        };
    }

//    private ValueResolver helperMethodValueResolver() {
//        return new ValueResolver() {
//            @Override
//            public boolean appliesTo(EvalContext context) {
//                return helperMethods.containsKey(context.getName());
//            }
//
//            @Override
//            public CompletionStage<Object> resolve(EvalContext context) {
//                // TODO
//            }
//        };
//    }

    private Optional<TemplateLocator.TemplateLocation> templateLocator(String id) {
        String fileName = id + ".qute.html";
        String localPath = System.getProperty("user.dir") + "/qute/qute-server/src/main/resources/templates/" + fileName;
        String classPath = "/templates/" + fileName;
        InputStream stream;

        // TODO: just do file resolving if in dev mode
        File file = new File(localPath);
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            stream = getClass().getResourceAsStream(classPath);
        }

        if (stream == null) {
            return Optional.empty();
        }

        InputStream finalStream = stream;
        return Optional.of(new TemplateLocator.TemplateLocation() {
            @Override
            public Reader read() {
                return new InputStreamReader(finalStream);
            }

            @Override
            public Optional<Variant> getVariant() {
                return Optional.empty();
            }
        });
    }

    private Optional<TemplateLocator.TemplateLocation> componentLocator(String id) {
        String path = "/" + id + ".qute.html"; // Adjust this path according to your needs
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return Optional.empty();
        }
        return Optional.of(new TemplateLocator.TemplateLocation() {
            @Override
            public Optional<Variant> getVariant() {
                return Optional.empty(); // You can provide a variant if needed
            }

            @Override
            public Reader read() {
                return new InputStreamReader(stream);
            }
        });
    }
}
