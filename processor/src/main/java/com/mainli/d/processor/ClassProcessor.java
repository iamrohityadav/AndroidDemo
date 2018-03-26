package com.mainli.d.processor;

import com.google.auto.service.AutoService;
import com.mainli.d.annotations.BindView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ClassProcessor extends AbstractProcessor {
    /**
     * 被注解处理工具调用
     *
     * @param processingEnv 处理器环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

    }

    /**
     * 处理方法注解方法
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                Element enclosingElement = element.getEnclosingElement();
                BindView annotation = element.getAnnotation(BindView.class);
                int id = annotation.value();

                Element enclosingElement1 = element.getEnclosingElement();
                PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(enclosingElement);
                Name qualifiedName = packageOf.getQualifiedName();
                print("包名:" + qualifiedName);
            }
        }
        if (roundEnv.processingOver()) {

            writeLog();
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        String canonicalName = BindView.class.getCanonicalName();
        print("要解析BindView的getCanonicalName:" + canonicalName);
        return Collections.singleton(canonicalName);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private StringBuffer mLog = new StringBuffer();

    /**
     * 我的gradle Console一直报以下错误看不到日志故使用文件来记录日志
     * Caused by: java.io.IOException: CreateProcess error=2, 系统找不到指定的文件。
     * at java.lang.ProcessImpl.create(Native Method)
     * at java.lang.ProcessImpl.<init>(ProcessImpl.java:386)
     * at java.lang.ProcessImpl.start(ProcessImpl.java:137)
     * at java.lang.ProcessBuilder.start(ProcessBuilder.java:1029)
     * ... 87 more
     *
     * @param msg
     */
    public void print(String msg) {
        mLog.append(msg).append("\n\r");
    }

    final String LOG_PATH = "D:\\processor-log.txt";

    public void writeLog() {
        FileOutputStream fileOutputStream = null;
        FileInputStream fileInputStream = null;
        File file = new File(LOG_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileInputStream = new FileInputStream(file);
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.write("------------------------\n\r".getBytes());
            fileOutputStream.write(mLog.toString().getBytes());
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }
        }
    }
}
