package com.rod.compile.parser;

import android.view.View;

import com.rod.annotation.OnClick;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author Rod
 * @date 2019-07-07
 */
public class OnClickParser implements Parser {

    private static final Class<? extends Annotation> ANNOTATION_CLASS = OnClick.class;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return ANNOTATION_CLASS;
    }

    @Override
    public String getAnnotationClassName() {
        return ANNOTATION_CLASS.getCanonicalName();
    }

    @Override
    public void parse(Map<TypeElement, BindingSet.Builder> builderMap, RoundEnvironment roundEnv) {
        Set<? extends Element> elemSet = roundEnv.getElementsAnnotatedWith(ANNOTATION_CLASS);
        if (elemSet.isEmpty()) {
            return;
        }

        elemSet.stream()
                .filter(elem -> elem != null && ((Element) elem).getKind() == ElementKind.METHOD)
                .map(elem -> (ExecutableElement) elem)
                .forEach(executableElem -> parseElem(builderMap, executableElem));
    }

    @Override
    public void bind(int targetViewId, ExecutableElement elem, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("view = rootView.findViewById($L)", targetViewId);

        methodBuilder.addStatement(
                "view.setOnClickListener($L)",
                TypeSpec.anonymousClassBuilder("", "")
                        .superclass(View.OnClickListener.class)
                        .addMethod(
                                MethodSpec.methodBuilder("onClick")
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(View.class, "view", Modifier.FINAL)
                                        .addAnnotation(Override.class)
                                        .addStatement("activity.$L(view)", elem.getSimpleName())
                                        .returns(TypeName.VOID)
                                        .build()
                        )
                        .build()
        );
    }

    private void parseElem(
            Map<TypeElement, BindingSet.Builder> builderMap,
            ExecutableElement element
    ) {
        Element parent = element.getEnclosingElement();
        if (parent == null || parent.getKind() != ElementKind.CLASS) {
            // TODO: 2019-07-08 write message
            return;
        }
        TypeElement typeElement = (TypeElement) parent;
        BindingSet.Builder builder = BindingSet.getOrNewBuilder(builderMap, typeElement);

        OnClick annotation = element.getAnnotation(OnClick.class);
        int id = annotation.value();
        builder.putListenerMethod(id, new ListenerElem(id, element, this));
    }
}
