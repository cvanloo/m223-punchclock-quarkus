package ch.zli.m223.punchclock.controller;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.zli.m223.punchclock.domain.AuthRequest;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.service.AuthenticationService;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Path("/auth")
@RequestScoped
public class AuthController {
    
    @Inject
    JsonWebToken jwt;

    @Inject
    AuthenticationService authService;

    @Inject
    private EntityManager entityManager;

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest authRequest) {
        var query = entityManager.createQuery("FROM User WHERE accountname = :username").setParameter("username", authRequest.username);

        User user;
        try {
            user = (User) query.getSingleResult();
        } catch(NoResultException nre) {
            return Response.status(400, "User not found").build();
        }

        if (!user.checkPassword(authRequest.password)) {
            return Response.status(400, "Wrong password").build();
        }

        return Response.ok(authService.generateValidJwtToken(user.getAccountName(), user.getRole())).build();
    }
}