package net.snemeis;

import io.quarkus.qute.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class I18nNamespaceResolver implements NamespaceResolver {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getNamespace() {
        return "i18n";
    }

    @Override
    public CompletionStage<Object> resolve(EvalContext context) {
        // name == 'msg'
        return CompletedStage.of(resolveT(context));
    }

    private String resolveT(EvalContext context) {
        if (!"t".equals(context.getName())) {
            System.out.println("method was not 't'");
            return null;
        }

        if (context.getParams().isEmpty() || !context.getParams().get(0).isLiteral()) {
            System.out.println("completion stage was null");
            return null;
        }

        Object keyObj = context.getParams().get(0).getLiteral();
        if (keyObj.getClass() != String.class) {
            return null;
        }
        String key = (String) keyObj;

        Object[] args = context
                .getParams()
                .subList(1, context.getParams().size())
                .stream().map(expression -> this.resolveParamValue(context, expression))
                .toArray();

        return messageSource.getMessage(key, args, Locale.ENGLISH);
    }

    private Object resolveParamValue(EvalContext context, Expression expression) {
        if (expression.isLiteral()) {
            return expression.getLiteral();
        }

        return ((CompletedStage<Object>) context.evaluate(expression)).get();
    }
}
