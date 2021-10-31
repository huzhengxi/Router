package com.jason.router.processor;

import com.google.auto.service.AutoService;
import com.jason.router.annotation.Destination;

import java.io.Writer;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.text.html.HTML;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
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
        // 避免多次调用
        if (roundEnv.processingOver()) {
            return false;
        }
        System.out.println(TAG + ">>> process start...");

        // 获取所有标记了 @Destination 注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(Destination.class);
        System.out.println(TAG + ">>> all Destination elements count = " + allDestinationElements.size());

        if (allDestinationElements.size() < 1) {
            return false;
        }

        String className = "RouterMapping_" + System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        builder.append("package com.jason.router.mapping;\n\n");
        builder.append("import java.util.HashMap;\n");
        builder.append("import java.util.Map;\n\n");
        builder.append("public class ").append(className).append(" {\n");
        builder.append("    public static Map<String, String> get() {\n");
        builder.append("        Map<String, String> mapping = new HashMap<>();\n\n");


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

            builder.append("        mapping.put(")
                    .append("\"" + url + "\"")
                    .append(", ")
                    .append("\"" + realPath + "\"")
                    .append(");\n");
        }

        builder.append("\n");
        builder.append("        return mapping;\n");
        builder.append("    }\n");
        builder.append("}\n ");

        String mappingFullClassName = "com.jason.router.mapping." + className;
        System.out.println(TAG + " >>> mappingFullClassName = " + mappingFullClassName);
        System.out.println(TAG + " class content = \n" + builder);

        //写入自动生成的类到本地文件中
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(mappingFullClassName);
            Writer writer = sourceFile.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (Exception exception) {
            throw new RuntimeException("Error while create file", exception);
        }
        System.out.println(TAG + " >>> process finish.");

        return false;
    }
}
