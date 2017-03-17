package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.http.session.DefaultSessionManager;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.security.permission.Permission;
import cabanas.garcia.ismael.opportunity.security.permission.Permissions;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthorizationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.service.DefaultPrivateResourceService;
import cabanas.garcia.ismael.opportunity.service.PrivateResourcesService;
import cabanas.garcia.ismael.opportunity.steps.model.PermissionData;
import cabanas.garcia.ismael.opportunity.steps.model.UserData;
import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.filters;
import static cabanas.garcia.ismael.opportunity.steps.Hooks.httpServer;

public class StartServerStepDefs implements En {

    public static Permissions permissions = new Permissions();
    public static PrivateResourcesService privateResourceService = new DefaultPrivateResourceService(new PrivateResources());

    public StartServerStepDefs() {
        When("^I start the web server on (\\d+) port$", (Integer port) -> {
            httpServer = new SunHttpServer(port);
            httpServer.start();
        });
        Then("^the web server is up$", () -> {
            Assert.assertThat(httpServer.isRunning(), Is.is(IsEqual.equalTo(true)));
        });

        And("^I stopped it$", () -> {
            httpServer.stop();
        });
        Then("^the web server is down$", () -> {
            Assert.assertThat(httpServer.isRunning(), Is.is(IsEqual.equalTo(false)));
        });
        Given("^private resources (.*)$", (String resources) -> {
            String[] resourcesSplitted = resources.split(",");

            PrivateResources privateResources = new PrivateResources();

            Arrays.stream(resourcesSplitted).forEach(resource ->
                privateResources.add(Resource.builder().path(resource.trim()).build())
            );
            
            privateResourceService = new DefaultPrivateResourceService(privateResources);

        });

        And("^there are the next users in the system$", (DataTable dataTable) -> {
            addUsers(dataTable.asList(UserData.class));
        });
        Given("^the next table of permissions$", (DataTable dataTable) -> {
            List<PermissionData> permissionsData = dataTable.asList(PermissionData.class);
            permissions = new Permissions();
            permissionsData.forEach(permissionData -> {
                Roles roles = Roles.builder().build();
                Arrays.stream(permissionData.getRoles()).forEach(rolename -> roles.add(rolename.trim()));
                permissions.add(Permission.builder().resource(Resource.builder().path(permissionData.getResource()).build()).roles(roles).build());
            });
        });
    }

    private void addUsers(List<UserData> users) {
        UserRepository userRepository = InMemoryUserRepository.getInstance();
        users.stream().forEach(userData -> {
            Roles roles = Roles.builder().build();
            Arrays.stream(userData.getRoles()).forEach(rolename -> roles.add(rolename.trim()));
            userRepository.persist(User.builder().username(userData.getUsername()).roles(roles).password(userData.getPassword()).build());
        });
    }
}