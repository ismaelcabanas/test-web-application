package cabanas.garcia.ismael.opportunity.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrivateResources {

    private final List<Resource> privateResourcesList;

    public PrivateResources() {
        privateResourcesList = new ArrayList<>();
    }

    public void add(Resource resource) {
        privateResourcesList.add(resource);
    }

    public boolean hasResource(Resource resource) {
        Optional<Resource> result = privateResourcesList.stream().filter(resource1 -> resource1.equals(resource)).findFirst();
        return result.isPresent();
    }

    @Override
    public String toString() {
        return "PrivateResources{" +
                "privateResourcesList=" + privateResourcesList +
                '}';
    }
}
