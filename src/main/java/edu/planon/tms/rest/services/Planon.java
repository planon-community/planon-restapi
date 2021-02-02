package edu.planon.tms.rest.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    public Response getAssetDetail(@PathParam("id") Integer id) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("getting asset detail");
        JsonObject jsonObject = getBO("BaseAsset", id);

        return Response.status(200).entity(jsonObject.toString()).build();
    }

    @POST
    @Path("/asset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createOrUpdateAssetDetail(JsonObject request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("creating or updating asset detail");
        JsonObject response = new JsonObject();
        Integer id = createBO("UsrAsset", request);
        response.addProperty("id", id);

        return Response.status(200).entity(response.toString()).build();
    }

    private Integer createBO(String type, JsonObject request) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject genericBO = BusinessObject.create(type);
            genericBO.getStringReferenceField("PropertyRef").setValue(request.get("property_ref"));
            genericBO.getStringReferenceField("ItemGroupRef").setValue(request.get("item_group_ref"));
            genericBO.getBooleanField("IsSimple").setValue(request.get("is_simple"));
            genericBO.getBooleanField("IsArchived").setValue(request.get("is_archived"));
            genericBO = genericBO.executeSave();
            
            LOG.info("Asset created");
            
            return genericBO.getPrimaryKey();
    }

    private void updateAsset(Integer primaryKeyValue, Integer propertyValue, Integer groupValue, Boolean isSimple, Boolean isArchive) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject assetBO = BusinessObject.read("UsrAsset", primaryKeyValue);
            assetBO.getStringReferenceField("PropertyRef").setValue(propertyValue);
            assetBO.getStringReferenceField("ItemGroupRef").setValue(groupValue);
            assetBO.getBooleanField("IsSimple").setValue(isSimple);
            assetBO.getBooleanField("IsArchived").setValue(isArchive);
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset Updated");
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
}
