package com.rod.compile.parser;

import com.google.auto.common.MoreElements;
import com.rod.compile.HaloProcessConstants;
import com.rod.compile.ProcessHelper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Rod
 * @date 2019-07-08
 */
public class BindingSet {

    private static final ClassName VIEW = ClassName.get("android.view", "View");

    private final TypeName mTargetTypeName;
    private final ClassName mBindingClassName;
    private final Map<Integer, List<ListenerElem>> mListenerMethods;
    private final boolean mIsActivity;

    private BindingSet(Builder builder) {
        mTargetTypeName = builder.mTargetType;
        mBindingClassName = builder.mBindingClassName;
        mListenerMethods = builder.mListenerMethods;
        mIsActivity = builder.mIsActivity;
    }

    public static Builder getOrNewBuilder(
            Map<TypeElement, Builder> builderMap,
            TypeElement typeElement
    ) {
        Builder result;
        if (!builderMap.containsKey(typeElement) || builderMap.get(typeElement) == null) {
            result = BindingSet.newBuilder(typeElement);
            builderMap.put(typeElement, result);
        } else {
            result = builderMap.get(typeElement);
        }
        return result;
    }

    static Builder newBuilder(TypeElement typeElement) {
        // TODO: 2019-07-13 搞清楚 TypeMirror/ParameterizedTypeName 以及为什么要强转
        TypeMirror typeMirror = typeElement.asType();
        TypeName targetType = TypeName.get(typeMirror);
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }
        String packageName = MoreElements.getPackage(typeElement).getQualifiedName().toString();
        String className = typeElement.getQualifiedName().toString().substring(
                packageName.length() + 1
        ).replace(".", "$");
        ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");
        boolean isActivity = ProcessHelper.isSubtypeOfType(
                typeMirror, HaloProcessConstants.ACTIVITY_TYPE
        );
        return new Builder(targetType, bindingClassName, isActivity);
    }

    public JavaFile getJavaFile() {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(mBindingClassName);
        classBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC);

        if (mIsActivity) {
            classBuilder.addMethod(createConstructorForActivity());
        }

        classBuilder.addMethod(createConstructor());

        return JavaFile.builder(mBindingClassName.packageName(), classBuilder.build())
                .addFileComment("Generated code from compiler. Do not modify!")
                .build();
    }

    private MethodSpec createConstructorForActivity() {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mTargetTypeName, "activity", Modifier.FINAL);
        constructorBuilder.addStatement("this(activity, activity.getWindow().getDecorView())");
        return constructorBuilder.build();
    }

    private MethodSpec createConstructor() {

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mTargetTypeName, "target", Modifier.FINAL)
                .addParameter(HaloProcessConstants.VIEW_CLASSNAME, "rootView", Modifier.FINAL)
                .addStatement("$T view", VIEW);

        addListenerBindings(constructorBuilder);

        return constructorBuilder.build();
    }

    private void addListenerBindings(MethodSpec.Builder constructorBuilder) {
        mListenerMethods.forEach(
                (viewId, listeners) -> {
                    addListenerBinding(viewId, listeners, constructorBuilder);
                }
        );
    }

    private void addListenerBinding(
            int viewId, List<ListenerElem> listeners, MethodSpec.Builder constructorBuilder
    ) {
        listeners.forEach(listenerElem ->
                listenerElem.mParser.bind(viewId, listenerElem.mElem, constructorBuilder)
        );
    }

    public static class Builder {

        private final Map<Integer, List<ListenerElem>> mListenerMethods = new HashMap();
        private final TypeName mTargetType;
        private final ClassName mBindingClassName;
        private final boolean mIsActivity;

        public Builder(TypeName targetType, ClassName bindingClassName, boolean isActivity) {
            mTargetType = targetType;
            mBindingClassName = bindingClassName;
            mIsActivity = isActivity;
        }

        Builder putListenerMethod(int id, ListenerElem listenerElem) {
            if (listenerElem != null) {
                List<ListenerElem> elementList = mListenerMethods.get(id);
                if (elementList == null) {
                    elementList = new ArrayList<>();
                    mListenerMethods.put(id, elementList);
                }
                elementList.add(listenerElem);
            }
            return this;
        }

        public BindingSet build() {
            return new BindingSet(this);
        }

    }
}
