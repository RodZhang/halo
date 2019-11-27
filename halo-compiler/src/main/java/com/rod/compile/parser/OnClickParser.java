package com.rod.compile.parser;

import android.view.View;

import com.rod.annotation.OnClick;
import com.rod.compile.HaloProcessConstants;
import com.rod.compile.ProcessHelper;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author Rod
 * @date 2019-07-07
 */
public class OnClickParser implements Parser {

    private static final Class<? extends Annotation> ANNOTATION_CLASS = OnClick.class;
    private ProcessingEnvironment mProcessingEnv;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return ANNOTATION_CLASS;
    }

    @Override
    public String getAnnotationClassName() {
        return ANNOTATION_CLASS.getCanonicalName();
    }

    @Override
    public void setProcessingEnv(ProcessingEnvironment processingEnv) {
        mProcessingEnv = processingEnv;
    }

    @Override
    public void parse(
            Map<TypeElement, BindingSet.Builder> builderMap,
            RoundEnvironment roundEnv
    ) {
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

        MethodSpec.Builder innerMethodBuilder = MethodSpec.methodBuilder("onClick")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(View.class, "view", Modifier.FINAL)
                .addAnnotation(Override.class)
                .returns(TypeName.VOID);
        addCallStatement(innerMethodBuilder, elem);

        methodBuilder.addStatement(
                "view.setOnClickListener($L)",
                TypeSpec.anonymousClassBuilder("", "")
                        .superclass(View.OnClickListener.class)
                        .addMethod(innerMethodBuilder.build())
                        .build()
        );
    }

    private void addCallStatement(MethodSpec.Builder methodBuilder, ExecutableElement elem) {
        List<? extends VariableElement> parameters = elem.getParameters();
        if (parameters.isEmpty()) {
            methodBuilder.addStatement("target.$L()", elem.getSimpleName());
            return;
        }
        if (parameters.size() > 1) {
            ProcessHelper.error(mProcessingEnv, "OnClick should has 0 or 1 param", elem);
            return;
        }
        VariableElement param = parameters.get(0);
        if (ProcessHelper.isSameType(param.asType(), HaloProcessConstants.VIEW_TYPE)) {
            methodBuilder.addStatement("target.$L(view)", elem.getSimpleName());
        } else if (ProcessHelper.isSubtypeOfType(param.asType(), HaloProcessConstants.VIEW_TYPE)) {
            methodBuilder.addStatement("target.$L(($T) view)", elem.getSimpleName(), param.asType());
        } else {
            ProcessHelper.error(mProcessingEnv, "OnClick's param should be subType of View", elem);
        }
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
