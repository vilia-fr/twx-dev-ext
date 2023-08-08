package fr.vilia.twx;

import com.google.gson.*;
import com.thingworx.data.util.InfoTableInstanceFactory;
import com.thingworx.entities.RootEntity;
import com.thingworx.entities.collections.RootEntityCollection;
import com.thingworx.entities.interfaces.IShapeProvider;
import com.thingworx.entities.utils.EntityUtilities;
import com.thingworx.implementation.ServiceImplementation;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.ServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.metadata.collections.FieldDefinitionCollection;
import com.thingworx.metadata.collections.ServiceDefinitionCollection;
import com.thingworx.relationships.RelationshipTypes;
import com.thingworx.resources.Resource;
import com.thingworx.scriptfunctionlibraries.FunctionDefinition;
import com.thingworx.scriptfunctionlibraries.FunctionDefinitionCollection;
import com.thingworx.scriptfunctionlibraries.ScriptFunctionLibrary;
import com.thingworx.system.managers.ScriptFunctionLibraryManager;
import com.thingworx.thingshape.ThingShapeInstance;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public class Development extends Resource {

	private static final long serialVersionUID = 1L;

	@ThingworxServiceDefinition(name = "JsonToString", description = "Prettifies JSON to String", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "STRING")
	public String JsonToString(@ThingworxServiceParameter(name = "json", baseType = "JSON") JSONObject json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json.toString());
		return gson.toJson(je);
	}

	@ThingworxServiceDefinition(name = "GetServiceCodeForThing", description = "Returns service code as string", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "STRING")
	public String GetServiceCodeForThing(
			@ThingworxServiceParameter(name = "thingName", baseType = "THINGNAME") String thingName,
			@ThingworxServiceParameter(name = "serviceName", baseType = "STRING") String serviceName
	) throws Exception {
		return getServiceCode(RelationshipTypes.ThingworxRelationshipTypes.Thing, thingName, serviceName);
	}

	@ThingworxServiceDefinition(name = "GetServiceCodeForThingTemplate", description = "Returns service code as string", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "STRING")
	public String GetServiceCodeForThingTemplate(
			@ThingworxServiceParameter(name = "thingTemplateName", baseType = "THINGTEMPLATENAME") String thingTemplateName,
			@ThingworxServiceParameter(name = "serviceName", baseType = "STRING") String serviceName
	) throws Exception {
		return getServiceCode(RelationshipTypes.ThingworxRelationshipTypes.ThingTemplate, thingTemplateName, serviceName);
	}

	@ThingworxServiceDefinition(name = "GetServiceCodeForThingShape", description = "Returns service code as string", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "STRING")
	public String GetServiceCodeForThingShape(
			@ThingworxServiceParameter(name = "thingShapeName", baseType = "THINGSHAPENAME") String thingShapeName,
			@ThingworxServiceParameter(name = "serviceName", baseType = "STRING") String serviceName
	) throws Exception {
		return getServiceCode(RelationshipTypes.ThingworxRelationshipTypes.ThingShape, thingShapeName, serviceName);
	}

	@ThingworxServiceDefinition(name = "GetServiceCode", description = "Returns service code as string", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "STRING")
	public String GetServiceCode(
			@ThingworxServiceParameter(name = "entityType", baseType = "STRING") String entityType,
			@ThingworxServiceParameter(name = "entityName", baseType = "STRING") String entityName,
			@ThingworxServiceParameter(name = "serviceName", baseType = "STRING") String serviceName
	) throws Exception {
		ScriptFunctionLibraryManager.getInstance().getEntityCollection().keys();
		return getServiceCode(
				RelationshipTypes.ThingworxRelationshipTypes.valueOf(entityType),
				entityName,
				serviceName
		);
	}

	@ThingworxServiceDefinition(name = "GetScriptFunctionLibraries", description = "Returns all script function libraries", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "INFOTABLE", aspects = { "dataShape:EntitySearchResult" })
	public InfoTable GetScriptFunctionLibraries() throws Exception {
		InfoTable result = InfoTableInstanceFactory.createInfoTableFromDataShape("EntitySearchResult");
		RootEntityCollection libs = ScriptFunctionLibraryManager.getInstance().getEntityCollection();
		for (Map.Entry<String, RootEntity> e : libs.entrySet()) {
			ScriptFunctionLibrary lib = (ScriptFunctionLibrary) e.getValue();
			ValueCollection valueCollection = new ValueCollection();
			valueCollection.SetStringValue("type", lib.getClassName());
			valueCollection.SetStringValue("description", lib.getDescription());
			valueCollection.SetStringValue("name", lib.getName());
			result.addRow(valueCollection);
		}
		return result;
	}

	@ThingworxServiceDefinition(name = "GetScriptFunctions", description = "Returns all script functions for a given library", category = "Develop")
	@ThingworxServiceResult(name = "result", description = "Result", baseType = "INFOTABLE", aspects = { "dataShape:EntitySearchResult" })
	public InfoTable GetScriptFunctions(
			@ThingworxServiceParameter(name = "libraryName", baseType = "STRING") String libraryName
	) throws Exception {
		InfoTable result = InfoTableInstanceFactory.createInfoTableFromDataShape("EntitySearchResult");
		try {
			ScriptFunctionLibrary lib = (ScriptFunctionLibrary) ScriptFunctionLibraryManager.getInstance().getEntity(libraryName);
			// TODO: This doesn't work. For some reason we don't have a getter for parameters, onyl setter.
			FunctionDefinitionCollection services = lib.getFunctionDefinitions();
			for (FunctionDefinition def: services.values()) {
				JSONObject json = def.toJSON(null);
				ValueCollection valueCollection = new ValueCollection();
				valueCollection.SetStringValue("description", def.getDescription());
				valueCollection.SetStringValue("name", def.getName());
				valueCollection.SetStringValue("parentName", libraryName);
				valueCollection.SetStringValue(
						"type",
						"(" +
								paramToString(json.getJSONArray("parameterDefinitions")) +
								"): " +
								resultToString(json.getJSONObject("resultType"))
				);
				result.addRow(valueCollection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private String resultToString(JSONObject resultType) throws JSONException {
		return resultType.getString("baseType");
	}

	private String paramToString(JSONArray parameters) throws JSONException {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < parameters.length(); ++i) {
			JSONObject param = parameters.getJSONObject(i);
			String type = param.getString("baseType");
			String name = param.getString("name");
			if (res.length() > 0) {
				res.append(", ");
			}
			res.append(name + ": " + type);
		}
		return res.toString();
	}

	private String getServiceCode(RelationshipTypes.ThingworxRelationshipTypes entityType, String entityName, String serviceName) throws Exception {
		RootEntity entity = EntityUtilities.findEntity(entityName, entityType);

		// HACK: This is not supported by the SDK and will compile only against ThingWorx JAR files
		if (entity instanceof IShapeProvider) {
			ThingShapeInstance shape = ((IShapeProvider) entity).getInstanceShape();
			ServiceImplementation implementation = shape.getServiceImplementation(serviceName);
			Object value = implementation.getConfigurationData().getValue("Script", "code");
			if (value != null) {
				return value.toString();
			} else {
				return null;
			}
		} else {
			throw new Exception("Entity " + entityName + " does not provide ThingShape");
		}
	}
}
