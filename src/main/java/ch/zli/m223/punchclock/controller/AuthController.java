package ch.zli.m223.punchclock.controller;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ch.zli.m223.punchclock.domain.AuthRequest;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.service.AuthenticationService;
import ch.zli.m223.punchclock.service.AdminService;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.persistence.EntityManager;

@Path("/auth")
@PermitAll
public class AuthController {
    
    @Inject
    JsonWebToken jwt;

    @Inject
    AuthenticationService authService;

    @Inject
    AdminService adminService;

    @Inject
    EntityManager entityManager;

    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(AuthRequest authRequest) {
        User user = adminService.getUserByName(authRequest.username);
        if (null == user) {
            return Response.status(Status.UNAUTHORIZED).build(); // Wrong username
        }

        try {
            if (!user.checkPassword(authRequest.password)) {
                return Response.status(Status.UNAUTHORIZED).build(); // Wrong password
            }
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(authService.generateValidJwtToken(user.getAccountName(), user.getRole())).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(AuthRequest authRequest) {
        User newUser = new User(authRequest.username, authRequest.password, "User");
        if (adminService.createUser(newUser) != null) {
            return Response.ok().build();
        }

        return Response.status(Status.BAD_REQUEST).build();
    }
}