package com.jason.router.processor;

import com.jason.router.annotation.Destination;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class DestinationProcessor extends AbstractProcessor {

    private static final String TAG = "DestinationProcessor";

    /**
     * 告诉编译器，当前处理器支持的注解类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(
                Destination.class.getCanonicalName()
        );
    }


    /**
     * 编译器找到我们关心的注解后，会回调这个方法
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(TAG + ">>> process start...");

        // 获取所有标记了 @Destination 注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(Destination.class);
        System.out.println(TAG + ">>> all Destination elements count = " + allDestinationElements.size());

        if (allDestinationElements.size() < 1) {
            return false;
        }


        for (Element element : allDestinationElements) {
            final TypeElement typeElement = (TypeElement) element;
            final Destination destination = typeElement.getAnnotation(Destination.class);
            if (destination == null) {
                continue;
            }
            final String url = destination.url();
            final String description = destination.description();
            final String realPath = typeElement.getQualifiedName().toString();

            System.out.println(TAG + ">>> url = " + url);
            System.out.println(TAG + ">>> description = " + description);
            System.out.println(TAG + ">>> realPath = " + realPath);
        }

        System.out.println(TAG + ">>> process finish.");

        return false;
    }
}
