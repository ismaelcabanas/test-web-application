package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Mapping {

    private Map<KeyMapper, Class<? extends Controller>> mapper = new HashMap<>();

    public boolean hasControllers() {
        return !mapper.isEmpty();
    }

    public Optional<Class<? extends Controller>> getController(String mappingPath) {
        return getController(mappingPath, RequestMethodConstants.GET);
    }

    public void addMapping(String mappingPath, Class<? extends Controller> aClass) {
        addMapping(mappingPath, RequestMethodConstants.GET, aClass);
    }

    public void addMapping(String mappingPath, String method, Class<? extends Controller> aClass) {
        KeyMapper keyMapper = KeyMapper.builder().path(mappingPath).method(method).build();
        mapper.put(keyMapper, aClass);
    }

    public Optional<Class<? extends Controller>> getController(String mappingPath, String method) {
        KeyMapper keyMapper = KeyMapper.builder().path(mappingPath).method(method).build();
        return Optional.ofNullable(mapper.get(keyMapper));
    }

    private static class KeyMapper {
        private String path;
        private String method;

        public KeyMapper(String mappingPath, String method) {
            this.path = mappingPath;
            this.method = method;
        }

        public static KeyMapperBuilder builder(){
            return new KeyMapperBuilder();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyMapper keyMapper = (KeyMapper) o;

            if (path != null ? !path.equals(keyMapper.path) : keyMapper.path != null) return false;
            return method != null ? method.equals(keyMapper.method) : keyMapper.method == null;
        }

        @Override
        public int hashCode() {
            int result = path != null ? path.hashCode() : 0;
            result = 31 * result + (method != null ? method.hashCode() : 0);
            return result;
        }

        private static class KeyMapperBuilder {
            private String mappingPath;
            private String method;

            public KeyMapperBuilder path(String mappingPath) {
                this.mappingPath = mappingPath;
                return this;
            }

            public KeyMapperBuilder method(String method) {
                this.method = method;
                return this;
            }

            public KeyMapper build() {
                return new KeyMapper(this.mappingPath, this.method);
            }
        }
    }
}
