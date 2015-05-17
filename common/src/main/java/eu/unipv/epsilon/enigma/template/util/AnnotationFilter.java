package eu.unipv.epsilon.enigma.template.util;

import java.lang.annotation.Annotation;

public class AnnotationFilter implements Predicate<Class<?>> {

    Class<? extends Annotation> annotation;

    public AnnotationFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean test(Class<?> clazz) {
        return clazz.isAnnotationPresent(annotation);
    }

}
