package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.util.ExpressionRegularValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DefaultMapping implements Mapping{

    private Map<KeyMapper, Class<? extends Controller>> mapper = new HashMap<>();

    @Override
    public boolean hasControllers() {
        return !mapper.isEmpty();
    }

    @Override
    public Optional<Class<? extends Controller>> getController(String mappingPath) {
        return getController(Resource.builder().path(mappingPath).build());
    }

    @Override
    public Optional<Class<? extends Controller>> getController(Resource resource) {
        return getController(resource, RequestMethodEnum.GET);
    }

    @Override
    public void addMapping(String mappingPath, Class<? extends Controller> aClass) {
        addMapping(Resource.builder().path(mappingPath).build(), RequestMethodEnum.GET, aClass);
    }

    @Override
    public void addMapping(Resource resource, Class<? extends Controller> aClass) {
        addMapping(resource, RequestMethodEnum.GET, aClass);
    }

    @Override
    public void addMapping(Resource resource, RequestMethodEnum method, Class<? extends Controller> aClass) {
        KeyMapper keyMapper = KeyMapper.builder().path(resource).method(method).build();
        mapper.put(keyMapper, aClass);
    }

    @Override
    public void addMapping(String mappingPath, RequestMethodEnum method, Class<? extends Controller> aClass) {
        addMapping(Resource.builder().path(mappingPath).build(), method, aClass);
    }

    @Override
    public Optional<Class<? extends Controller>> getController(Resource mappingPath, RequestMethodEnum method) {
        for (Map.Entry<KeyMapper, Class<? extends Controller>> entry : mapper.entrySet()) {
            if(entry.getKey().match(mappingPath, method))
                return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Class<? extends Controller>> getController(String mappingPath, RequestMethodEnum method) {
        return this.getController(Resource.builder().path(mappingPath).build(), method);
    }

    private static class KeyMapper {
        private Resource pattern;
        private RequestMethodEnum method;

        public KeyMapper(Resource mappingPath, RequestMethodEnum method) {
            this.pattern = mappingPath;
            this.method = method;
        }

        public boolean match(Resource path, RequestMethodEnum method){
            return ExpressionRegularValidator.isValidate(this.pattern.getPath(), path.getPath())
                    && this.method.equals(method);
        }

        public static KeyMapperBuilder builder(){
            return new KeyMapperBuilder();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyMapper keyMapper = (KeyMapper) o;

            if (pattern != null ? !pattern.equals(keyMapper.pattern) : keyMapper.pattern != null) return false;
            return method != null ? method.equals(keyMapper.method) : keyMapper.method == null;
        }

        @Override
        public int hashCode() {
            int result = pattern != null ? pattern.hashCode() : 0;
            result = 31 * result + (method != null ? method.hashCode() : 0);
            return result;
        }

        private static class KeyMapperBuilder {
            private Resource mappingPath;
            private RequestMethodEnum method;

            public KeyMapperBuilder path(Resource mappingPath) {
                this.mappingPath = mappingPath;
                return this;
            }

            public KeyMapperBuilder method(RequestMethodEnum method) {
                this.method = method;
                return this;
            }

            public KeyMapper build() {
                return new KeyMapper(this.mappingPath, this.method);
            }
        }
    }
}
