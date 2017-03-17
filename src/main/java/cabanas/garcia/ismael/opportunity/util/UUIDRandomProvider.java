package cabanas.garcia.ismael.opportunity.util;

import java.util.UUID;

public class UUIDRandomProvider implements UUIDProvider{
    @Override
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
