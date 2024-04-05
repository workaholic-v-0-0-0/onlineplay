package online.caltuli.batch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import online.caltuli.business.UserManager;

@ApplicationScoped
public class AiUser {

    @Inject
    private UserManager userManager;

    public void task() {
        authenticate();
    }

    public void authenticate() {

    }

    public void proposeGame() {

    }

}
