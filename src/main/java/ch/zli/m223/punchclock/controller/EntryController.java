package ch.zli.m223.punchclock.controller;

import java.util.List;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.service.AdminService;
import ch.zli.m223.punchclock.service.EntryService;
import ch.zli.m223.punchclock.domain.User;

@Path("/entries")
@Tag(name = "Entries", description = "Handling of entries")
@RequestScoped
@RolesAllowed("User")
public class EntryController {

    @Inject
    EntryService entryService;

    @Inject
    AdminService adminService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List all Entries", description = "")
    public List<Entry> list(@Context SecurityContext ctx) {
        User user = adminService.getUserByName(ctx.getUserPrincipal().getName());
        if (null != user) {
            return entryService.getAllEntriesFromUser(user.getId());
        }
        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a new Entry", description = "The newly created entry is returned. The id may not be passed.")
    public Response add(Entry entry, @Context SecurityContext ctx) {
        if (entry.getCheckIn().isAfter(entry.getCheckOut())) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        String username = ctx.getUserPrincipal().getName();
        User user = adminService.getUserByName(username);
        
        if (null == user) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        entry.setUser(user);
        entry = entryService.createEntry(entry);
        return Response.ok(entry).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete an Entry", description = "Deletes an entry")
    public Response delete(@PathParam("id") Long id, @Context SecurityContext ctx) {
        User user = adminService.getUserByName(ctx.getUserPrincipal().getName());
        Entry entry = entryService.getEntryById(id);
        if (null != entry && entry.getUser().getId() == user.getId()) {
            if (entryService.deleteEntry(id)) {
                return Response.ok().build();
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Status.UNAUTHORIZED).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update an Entry", description = "Updates an Entry")
    public Response patch(Entry entry, @Context SecurityContext ctx) {
        User user = adminService.getUserByName(ctx.getUserPrincipal().getName());
        Entry oldEntry = entryService.getEntryById(entry.getId()); // get old entry to make sure the user wasn't changed.
        if (oldEntry.getUser().getId() == user.getId()) {
            entry.setUser(user);
            entry = entryService.updateEntry(entry);
            return Response.ok(entry).build();
        }

        return Response.status(Status.BAD_REQUEST).entity(oldEntry).build();
    }
}
