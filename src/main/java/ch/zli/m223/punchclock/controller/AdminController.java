package ch.zli.m223.punchclock.controller;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.PathParam;

import ch.zli.m223.punchclock.service.AdminService;

import ch.zli.m223.punchclock.domain.User;

import java.util.List;

@Path("/admin")
@RequestScoped
@RolesAllowed("Admin")
public class AdminController {
    
    @Inject
    AdminService adminService;

    /**
     * Get a list containing all users.
     * @return a list of users.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
        return adminService.getAllUsers();
    }

    /**
     * Create a new user.
     * @param user New user to create.
     * @return A html response containing the newly created user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(User user) {
        user = adminService.createUser(user);
        return Response.ok(user).build();
    }

    /**
     * Delete a user.
     * @param id Id of the user to delete.
     * @return 200 OK if the user was successfully deleted, otherwise 400 BAD REQUEST.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        if (adminService.deleteUser(id)) {
            return Response.ok().build();
        }

        return Response.status(Status.BAD_REQUEST).build();
    }

    /**
     * Modify a user.
     * @param user The modified user.
     * @return Returns the modified user on success, otherwise the unmodified (old) user.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User change(User user) {
        return adminService.updateUser(user);
    }
}
