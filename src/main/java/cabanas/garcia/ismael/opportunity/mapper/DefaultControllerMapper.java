package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultControllerMapper implements ControllerMapper{
    @Override
    public Map<String, Class<? extends Controller>> mapping(List<Class<? extends Controller>> controllers) {
        Map<String, Class<? extends Controller>> result = new HashMap<>();

        controllers.forEach(aClass -> {

            try {
                Class<?> clazz = Class.forName(aClass.getCanonicalName());
                Constructor<?> ctor = clazz.getConstructor();
                Controller controller = (Controller) ctor.newInstance();
                result.put(controller.getMappingPath(), aClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return result;
    }
}
