package com.rod.compile;

import java.util.List;
import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * @author Rod
 * @date 2019-07-15
 */
public final class ProcessHelper {

    public static boolean isSubtypeOfType(TypeMirror typeMirror, String type) {
        if (isSameType(typeMirror, type)) {
            return true;
        }

        // 判断 typeMirror 是否是类或者接口
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }

        // 由于上面已经判断了是 TypeKind.DECLARED，所以这是强转
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            // TODO: 2019-07-16 看看这里的typeStr到底是什么样子的
            StringBuilder typeStr = new StringBuilder(declaredType.asElement().toString());
            typeStr.append("<");
            for (int i = 0, size = typeArguments.size(); i < size; i++) {
                if (i > 0) {
                    typeStr.append(",");
                }
                typeStr.append("?");
            }
            typeStr.append(">");
            if (Objects.equals(typeStr.toString(), type)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        // 判断 element 是否是类或者接口
        // 这里看上去和上面有重复，jake 为什么要分开判断，并且是在上面那段逻辑之后？？
        if (!(element instanceof TypeElement)) {
            return false;
        }

        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();

        // 递归判断父类是否和要比较的 type 匹配
        if (isSubtypeOfType(superType, type)) {
            return true;
        }

        // 递归判断直接实现或者继承的接口是否和要比较的 type 匹配
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, type)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSameType(TypeMirror typeMirror, String type) {
        return Objects.equals(typeMirror.toString(), type);
    }

    public static void error(ProcessingEnvironment processingEnv, String msg, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }
}
