package com.rod.compile;

import com.google.auto.service.AutoService;
import com.rod.annotation.OnClick;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private ProcessingEnvironment mProcessingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessingEnvironment = processingEnvironment;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>() {{
            add(OnClick.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 获取由 OnClick 注解的所有元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elements) {
            OnClick onClick = element.getAnnotation(OnClick.class);
            if (element instanceof ExecutableElement) {
                ExecutableElement executableElement = (ExecutableElement) element;
            }
        }

        return false;
    }
}
