package com.rod.compile;

import com.google.auto.service.AutoService;
import com.rod.compile.parser.BindingSet;
import com.rod.compile.parser.OnClickParser;
import com.rod.compile.parser.Parser;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class HaloProcessor extends AbstractProcessor {

    private List<Parser> mParsers = Arrays.asList(
            new OnClickParser()
    );

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return mParsers.stream()
                .map(Parser::getAnnotationClassName)
                .collect(Collectors.toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        newWayToParse(roundEnvironment);

        return false;
    }

    private void newWayToParse(RoundEnvironment roundEnvironment) {
        Map<TypeElement, BindingSet.Builder> builderMap = new HashMap<>();
        mParsers.forEach(parser -> parser.parse(builderMap, roundEnvironment));
        Map<TypeElement, BindingSet> bindingSetMap = new HashMap<>(builderMap.size());
        builderMap.entrySet()
                .forEach(entry -> {
                    bindingSetMap.put(entry.getKey(), entry.getValue().build());
                });
        bindingSetMap.entrySet()
                .forEach(entry -> brewJava(entry.getKey(), entry.getValue()));

    }

    private void brewJava(TypeElement typeElement, BindingSet bindingSet) {
        JavaFile javaFile = bindingSet.getJavaFile();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            error("brewJava fail", typeElement);
        }
    }

    private void error(String msg, TypeElement element) {
        processingEnv.getMessager()
                .printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

}
