package cabanas.garcia.ismael.opportunity.util;

public class DefaultTimeProvider implements TimeProvider {
    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}
