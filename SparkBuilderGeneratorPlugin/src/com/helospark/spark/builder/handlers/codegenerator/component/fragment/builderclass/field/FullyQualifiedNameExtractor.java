package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import java.util.Optional;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.helospark.spark.builder.PluginLogger;

/**
 * Extracts the fully qualified name for the given declaration.
 * Implicit assumption of this class, that the AST was created with enabled binding.
 * Extracting fully qualified names require resolving of binding, which can possible be a costly operation.
 * @author helospark
 */
public class FullyQualifiedNameExtractor {
    private static final char GENERIC_PARAMETER_START_CHARACTER = '<';
    private PluginLogger pluginLogger;

    public FullyQualifiedNameExtractor() {
        pluginLogger = new PluginLogger();
    }

    public Optional<String> getFullyQualifiedBaseTypeName(FieldDeclaration fieldDeclaration) {
        return getFullyQualifiedParameterizedTypeName(fieldDeclaration)
                .map(value -> deleteGenericTypeFromString(value));
    }

    private String deleteGenericTypeFromString(String value) {
        int startOfGenericType = value.indexOf(GENERIC_PARAMETER_START_CHARACTER);
        if (startOfGenericType == -1) {
            return value;
        } else {
            return value.substring(0, startOfGenericType);
        }
    }

    public Optional<String> getFullyQualifiedParameterizedTypeName(FieldDeclaration fieldDeclaration) {
        ITypeBinding resolvedBinding = fieldDeclaration.getType().resolveBinding();
        Optional<String> result = Optional.ofNullable(resolvedBinding)
                .map(value -> value.getQualifiedName());
        if (!result.isPresent()) {
            if (!result.isPresent()) {
                pluginLogger.warn("Cannot extract fully qualified name of field declaration '" + String.valueOf(fieldDeclaration) + "', builder field will not be preinitialized");
            }
        }
        return result;
    }
}