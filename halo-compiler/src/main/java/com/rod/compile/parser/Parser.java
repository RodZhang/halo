package com.rod.compile.parser;

import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * 注解解析器，负责某一个具体注解的解析
 *
 * @author Rod
 * @date 2019-07-07
 */
public interface Parser {

    /**
     * 获得此 Parser 负责解析的 Annotation Class
     *
     * @return Annotation Class
     */
    Class<? extends Annotation> getAnnotationClass();

    String getAnnotationClassName();

    /**
     * 处理注解的具体工作
     * @param builderMap
     * @param roundEnv
     */
    void parse(Map<TypeElement, BindingSet.Builder> builderMap, RoundEnvironment roundEnv);

    void bind(int targetViewId, ExecutableElement elem, MethodSpec.Builder methodBuilder);

    /**
     * 注入 processingEnv，用于打印日志
     * @param processingEnv
     */
    void setProcessingEnv(ProcessingEnvironment processingEnv);
}
