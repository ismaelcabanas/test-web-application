package cabanas.garcia.ismael.opportunity.internal.creation.instance;

public interface Instantiator {
    <T> T newInstance(Class<T> aClass);
}
