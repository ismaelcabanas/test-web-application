package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.controller.rest.UserCreateController;
import cabanas.garcia.ismael.opportunity.controller.rest.UserDeleteController;
import cabanas.garcia.ismael.opportunity.controller.rest.UserUpdateController;
import cabanas.garcia.ismael.opportunity.controller.web.LoginPostController;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.InstantiationException;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.Instantiator;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.service.DefaultUserService;
import cabanas.garcia.ismael.opportunity.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class DIControllerFactory {

    private final Instantiator instantiator;

    public DIControllerFactory(Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    public Controller getInstance(Class<? extends Controller> clazz)  {
        if(clazz.getName().equals(LoginPostController.class.getName())
                || clazz.getName().equals(UserCreateController.class.getName())
                || clazz.getName().equals(UserDeleteController.class.getName())
                || clazz.getName().equals(UserUpdateController.class.getName())){
            try {
                return clazz.getConstructor(UserService.class).newInstance(new DefaultUserService(InMemoryUserRepository.getInstance()));
            } catch (java.lang.InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new InstantiationException("Unable to create instance of \'" + clazz.getSimpleName() + "\'.\nPlease ensure it has 0-arg constructor which invokes cleanly.", e);
            }
        }


        return instantiator.newInstance(clazz);
    }
}
