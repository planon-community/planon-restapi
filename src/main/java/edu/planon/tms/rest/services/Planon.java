package edu.planon.tms.rest.services;

import java.util.Random;

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
import edu.planon.tms.rest.dto.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.IPnESStringField;
import nl.planon.util.pnlogging.PnLogger;
import nl.planon.enterprise.service.api.IPnESAction;
import nl.planon.enterprise.service.api.IPnESBusinessObject;
import nl.planon.enterprise.service.api.IPnESContext;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.IPnESResultSet;
import nl.planon.enterprise.service.api.IPnESStringField;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.util.pnlogging.PnLogger;


/**
 * 
 * @author Mohamed Hachem - Planon 
 *
 */
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
        LOG.info("Found asset!");

        return Response.status(200).entity(asset.toString()).build();
    }

    @POST
    @Path("/UsrAsset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
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

        return Response.status(200)
                       .entity(jsonObject.toString())
                       .build();
    }

    @POST
    @Path("/InventoryLocationAssignment")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Response createOrUpdateLocationAssignment(Location location) {
        LOG.info("creating or updating location assignment detail");
        String message = "";
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(new Gson().toJson(location));
        if (location != null && location.getPrimaryKey() == 0) {
            Random random = new Random();
            message = "Created the location successfully with key: " + random.ints(1, 100)
                                                                             .findFirst()
                                                                             .getAsInt();
        } else {
            message = "Updated the location successfully";
        }
        jsonObject.addProperty("message", message);

        return Response.status(200)
                       .entity(jsonObject.toString())
                       .build();
    }
    
    private Integer createAsset(Integer propertyValue, Integer groupValue, Boolean isSimple, Boolean isArchive)
                        throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject assetBO = BusinessObject.create("UsrAsset");
            assetBO.getStringReferenceField("PropertyRef")
                             .setValue(propertyValue);
            assetBO.getStringReferenceField("ItemGroupRef")
                             .setValue(groupValue);
            assetBO.getBooleanField("IsSimple")
                             .setValue(isSimple);
            assetBO.getBooleanField("IsArchived")
                             .setValue(isArchive);
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset created");
            
            return assetBO.getPrimaryKey();
}
    
    private void updateAsset(Integer primaryKeyValue, Integer propertyValue, Integer groupValue, Boolean isSimple, Boolean isArchive)
            throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
            IPnESBusinessObject assetBO = BusinessObject.read("UsrAsset", primaryKeyValue);
            assetBO.getStringReferenceField("PropertyRef")
                             .setValue(propertyValue);
            assetBO.getStringReferenceField("ItemGroupRef")
                             .setValue(groupValue);
            assetBO.getBooleanField("IsSimple")
                             .setValue(isSimple);
            assetBO.getBooleanField("IsArchived")
                             .setValue(isArchive);
            assetBO = assetBO.executeSave();
            
            LOG.info("Asset Updated");
    }

    private IPnESBusinessObject getAsset(Integer primaryKeyValue) throws PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
        IPnESBusinessObject assetBO = BusinessObject.read("UsrAsset", primaryKeyValue);

        LOG.info("Getting asset");
        return assetBO;
    }
}
