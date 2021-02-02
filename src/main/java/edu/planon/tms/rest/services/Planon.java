package edu.planon.tms.rest.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.planonsoftware.tms.lib.client.BusinessObject;
import edu.planon.tms.rest.dto.Asset;

import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.enterprise.service.api.IPnESField;
import nl.planon.enterprise.service.api.PnESValueType;
import nl.planon.util.pnlogging.PnLogger;

// This sets the root path of the service
@Path("/")
public class Planon {
    private static final PnLogger LOG = PnLogger.getLogger(Planon.class);
    private static final String WELCOME_QUOTE = "Welcome to Planon Rest Services!!";

    @GET
    @Produces(value={"application/json"})
    public Response getDefaultGreeting() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("greeting", WELCOME_QUOTE);
        return Response.status(200).entity(jsonObject.toString()).build();
    }

    @GET
    @Path("/asset/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAssetResponse(@PathParam("id") Integer id) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("getting asset detail");
        JsonObject jsonObject = getBO("BaseAsset", id);

        return Response.status(200).entity(jsonObject.toString()).build();
    }

    @POST
    @Path("/asset")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createAssetResponse(Asset request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("creating or updating asset detail");

        Integer id = createBO("UsrAsset", request);

        return Response.status(201).header("Location", "/asset/" + id).build();
    }

    @PUT
    @Path("/asset/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateAssetResponse(@PathParam("id") Integer id, Asset request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("creating or updating asset detail");

        updateBO("UsrAsset", id, request);

        return Response.status(200).build();
    }

    @DELETE
    @Path("/asset/{id}")
    public Response deleteAssetResponse(@PathParam("id") Integer id) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("creating or updating asset detail");

        deleteBO("UsrAsset", id);

        return Response.status(200).build();
    }
    
    // TODO: Create a generic method to retrieve a BO that can be JSON serialized
    private JsonObject getBO(String type, Integer primaryKeyValue) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
        IPnESBusinessObject genericBO = BusinessObject.read(type, primaryKeyValue);

        JsonObject response = new JsonObject();
        response.addProperty("id", genericBO.getPrimaryKey());
        response.addProperty("code", genericBO.getStringField("Code").getValue());
        response.addProperty("type", genericBO.getBOType().getPnName());
        response.addProperty("description", genericBO.getStringField("Name").getValue());

        LOG.info("Getting asset");
        return response;
    }

    private Integer createBO(String type, Asset request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
        LOG.info("Creating business object of type " + type);
        IPnESBusinessObject genericBO = BusinessObject.create(type);

        genericBO.getStringReferenceField("PropertyRef").setValue(request.getPropertyRef());
        genericBO.getStringReferenceField("ItemGroupRef").setValue(request.getItemGroupRef());
        genericBO.getBooleanField("IsSimple").setValue(request.getIsSimple());
        genericBO.getBooleanField("IsArchived").setValue(request.getIsArchived());
        genericBO = genericBO.executeSave();
        
        LOG.info("Business object created");
        
        return genericBO.getPrimaryKey();
    }

    private void updateBO(String type, Integer id, Asset request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            LOG.info("Updating business object of type " + type);
            IPnESBusinessObject assetBO = BusinessObject.read(type, id);

            assetBO.getStringReferenceField("PropertyRef").setValue(request.getPropertyRef());
            assetBO.getStringReferenceField("ItemGroupRef").setValue(request.getItemGroupRef());
            assetBO.getBooleanField("IsSimple").setValue(request.getIsSimple());
            assetBO.getBooleanField("IsArchived").setValue(request.getIsArchived());
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset Updated");
    }

    private Boolean deleteBO(String type, Integer id) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
        LOG.info("Deleting business object of type " + type);
        return BusinessObject.delete(type, id);
    }
}
