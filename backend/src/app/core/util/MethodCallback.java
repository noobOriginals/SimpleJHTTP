package app.core.util;

@FunctionalInterface
public interface MethodCallback<T> {
    public T runMethod();
}
