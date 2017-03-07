package cabanas.garcia.ismael.opportunity.internal.creation.instance;

public class ConstructorInstantiator implements Instantiator {
    @Override
    public <T> T newInstance(Class<T> aClass) {
        try {
            return aClass.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            throw new InstantiationException("Unable to create instance of \'" + aClass.getSimpleName() + "\'.\nPlease ensure it has 0-arg constructor which invokes cleanly.", e);
        }
    }
}
