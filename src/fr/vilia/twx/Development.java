package fr.vilia.twx;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.resources.Resource;

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

}
