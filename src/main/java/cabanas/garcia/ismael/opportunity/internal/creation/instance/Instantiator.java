package cabanas.garcia.ismael.opportunity.internal.creation.instance;

@FunctionalInterface
public interface Instantiator {
    <T> T newInstance(Class<T> aClass);
}
