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
import nl.planon.util.pnlogging.PnLogger;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;

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
    @Path("/UsrAsset/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAssetDetail(@PathParam("id") Integer id) 
             throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("getting asset detail");

        IPnESBusinessObject asset = getAsset(id);
        LOG.info("Asset PK: " + asset.getPrimaryKey() + ", someday converting to JSON");
        
        LOG.info("ASSET DEBUG: " + asset.getNumberOfFields());
        for (Integer i = 0; i < asset.getNumberOfFields(); i++) {
            LOG.info("ASSET DEBUG: " + asset.getFieldPnName(i));
        }

        // TODO: Figure out how to map the IPnESBusinessObject fields to the target Object
        Asset testAsset = new Asset();
        testAsset.setPrimaryKey(asset.getPrimaryKey());
        testAsset.setProperytyRef(asset.getReferenceField("PropertyRef").getValue());

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(testAsset));
        jsonObject.addProperty("hello", "world!");

        return Response.status(200).entity(jsonObject.toString()).build();
    }

    @POST
    @Path("/UsrAsset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createOrUpdateAssetDetail(Asset asset) 
             throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException{
        LOG.info("creating or updating asset detail");
        String message = "";
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(asset));
      
        if (asset != null && asset.getPrimaryKey() == 0) {
            Integer primaryKey = createAsset(asset.getProperytyRef(), asset.getItemGroupRef(), asset.getIsSimple(), asset.getIsArchived() );
            message = "Created the asset successfully with code: " + primaryKey;
          
        } else {
            updateAsset(asset.getPrimaryKey(), asset.getProperytyRef(), asset.getItemGroupRef(), asset.getIsSimple(), asset.getIsArchived() );
            message = "Updated the asset successfully";
        }
        jsonObject.addProperty("message", message);

        return Response.status(200).entity(jsonObject.toString()).build();
    }

    private Integer createAsset(Integer propertyValue, Integer groupValue, Boolean isSimple, Boolean isArchive) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject assetBO = BusinessObject.create("UsrAsset");
            assetBO.getStringReferenceField("PropertyRef").setValue(propertyValue);
            assetBO.getStringReferenceField("ItemGroupRef").setValue(groupValue);
            assetBO.getBooleanField("IsSimple").setValue(isSimple);
            assetBO.getBooleanField("IsArchived").setValue(isArchive);
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset created");
            
            return assetBO.getPrimaryKey();
    }

    private void updateAsset(Integer primaryKeyValue, Integer propertyValue, Integer groupValue, Boolean isSimple, Boolean isArchive)
            throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject assetBO = BusinessObject.read("UsrAsset", primaryKeyValue);
            assetBO.getStringReferenceField("PropertyRef").setValue(propertyValue);
            assetBO.getStringReferenceField("ItemGroupRef").setValue(groupValue);
            assetBO.getBooleanField("IsSimple").setValue(isSimple);
            assetBO.getBooleanField("IsArchived").setValue(isArchive);
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset Updated");
    }

    // TODO: Create a generic method to retrieve a BO that can be JSON serialized
    private IPnESBusinessObject getAsset(Integer primaryKeyValue) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
        IPnESBusinessObject assetBO = BusinessObject.read("UsrAsset", primaryKeyValue);

        LOG.info("Getting asset");
        return assetBO;
    }
}
