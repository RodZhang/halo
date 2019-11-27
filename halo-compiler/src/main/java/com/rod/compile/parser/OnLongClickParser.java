package com.rod.compile.parser;

import android.view.View;

import com.rod.annotation.OnLongClick;
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
 * @date 2019/11/27
 */
public class OnLongClickParser implements Parser {

    private static final Class<? extends Annotation> ANNOTATION_CLASS = OnLongClick.class;
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
    public void parse(Map<TypeElement, BindingSet.Builder> builderMap, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ANNOTATION_CLASS);
        if (elements == null || elements.isEmpty()) {
            return;
        }
        elements.stream()
                .filter(elem -> elem != null && ((Element) elem).getKind() == ElementKind.METHOD)
                .map(elem -> (ExecutableElement) elem)
                .forEach(executableElement -> parseElem(builderMap, executableElement));
    }

    @Override
    public void bind(int targetViewId, ExecutableElement elem, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("view = rootView.findViewById($L)", targetViewId);

        MethodSpec.Builder innerMethodBuilder = MethodSpec.methodBuilder("onLongClick")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(View.class, "view", Modifier.FINAL)
                .addAnnotation(Override.class)
                .returns(TypeName.BOOLEAN);
        addCallStatement(innerMethodBuilder, elem);

        methodBuilder.addStatement(
                "view.setOnLongClickListener($L)",
                TypeSpec.anonymousClassBuilder("", "")
                        .superclass(View.OnLongClickListener.class)
                        .addMethod(innerMethodBuilder.build())
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

        OnLongClick annotation = element.getAnnotation(OnLongClick.class);
        int id = annotation.value();
        builder.putListenerMethod(id, new ListenerElem(id, element, this));
    }

    private void addCallStatement(MethodSpec.Builder methodBuilder, ExecutableElement elem) {
        List<? extends VariableElement> parameters = elem.getParameters();
        if (parameters.isEmpty()) {
            methodBuilder.addStatement("return target.$L()", elem.getSimpleName());
            return;
        }
        if (parameters.size() > 1) {
            ProcessHelper.error(mProcessingEnv, "OnLongClick should has 0 or 1 param", elem);
            return;
        }
        VariableElement param = parameters.get(0);
        if (ProcessHelper.isSameType(param.asType(), HaloProcessConstants.VIEW_TYPE)) {
            methodBuilder.addStatement("return target.$L(view)", elem.getSimpleName());
        } else if (ProcessHelper.isSubtypeOfType(param.asType(), HaloProcessConstants.VIEW_TYPE)) {
            methodBuilder.addStatement("return target.$L(($T) view)", elem.getSimpleName(), param.asType());
        } else {
            ProcessHelper.error(mProcessingEnv, "OnLongClick's param should be subType of View", elem);
        }
    }
}
