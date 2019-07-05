package com.rod.compile;

import com.google.auto.service.AutoService;
import com.rod.annotation.OnClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class HaloProcessor extends AbstractProcessor {

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

        Map<TypeElement, List<ExecutableElement>> typeElementMap = getTypeElements(roundEnvironment);
        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : typeElementMap.entrySet()) {
            writeJavaFile(entry.getKey(), entry.getValue());
        }

        return true;
    }

    /**
     * 获取由 OnClick 注解的所有元素
     */
    private Map<TypeElement, List<ExecutableElement>> getTypeElements(
            RoundEnvironment roundEnvironment
    ) {
        Map<TypeElement, List<ExecutableElement>> result = new HashMap<>();

        List<ExecutableElement> executableElements;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elements) {
            Element parentElement = element.getEnclosingElement();
            if (element.getKind() != ElementKind.METHOD
                    || parentElement == null
                    || parentElement.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement typeElement = (TypeElement) parentElement;
            executableElements = result.get(typeElement);
            if (executableElements == null) {
                executableElements = new ArrayList<>();
                result.put(typeElement, executableElements);
            }
            executableElements.add((ExecutableElement) element);
        }

        return result;
    }

    private void writeJavaFile(TypeElement key, List<ExecutableElement> value) {
        String packageName = mProcessingEnvironment.getElementUtils()
                .getPackageOf(key)
                .getQualifiedName()
                .toString();
        String typeName = key.getSimpleName().toString();
        ClassName className = ClassName.get(packageName, typeName);
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(typeName + "_ViewBinding")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className, "activity", Modifier.FINAL);

        ClassName androidViewClassName = ClassName.get("android.view", "View");
        ClassName onClickListenerClassName = ClassName.get("android.view", "View", "OnClickListener");
        for (ExecutableElement element : value) {
            OnClick anno = element.getAnnotation(OnClick.class);
            int viewId = anno.value();
            TypeSpec onClassListenerClass = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(onClickListenerClassName)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(androidViewClassName, "view")
                            .addStatement("$N.$N($N)", "activity", element.getSimpleName(), "view")
                            .returns(void.class)
                            .build()
                    ).build();
            constructorBuilder.addStatement("$N.findViewById($L).setOnClickListener($L)",
                    "activity",
                    viewId,
                    onClassListenerClass);
        }
        constructorBuilder.build();
        classBuilder.addMethod(constructorBuilder.build());
        try {
            JavaFile.builder(packageName, classBuilder.build())
                    .build()
                    .writeTo(mProcessingEnvironment.getFiler());
        } catch (IOException e) {
            mProcessingEnvironment.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, e.toString(), key);
        }
    }
}
