package controllers;

import models.Person;
import models.PersonList;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
 
public class PersonController extends Controller {
// More code will be added here
	@BodyParser.Of(BodyParser.Json.class)
	public static Result storePerson(){
		ObjectMapper mapper = new ObjectMapper();
		 try {
			 System.err.println("POST Data");
			 JsonNode json = request().body().asJson();
			 System.err.println("json payload: " + json);
			 Person newPerson = mapper.readValue(json.toString(), Person.class);
			 PersonList theList = PersonList.getInstance(); 
			 newPerson = theList.addPerson(newPerson);
			 ObjectNode result = Json.newObject();
			 result.put("Person", Json.toJson(newPerson));
			 return created(result);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 return badRequest("Missing information");
		 }
		
	}
	
	public static Result getPerson(Long id){
		// DEBUG 
		System.err.println("GET on id: "+ id);
		
		ObjectNode result = Json.newObject();
		PersonList theList = PersonList.getInstance();
		Person P = theList.getPersonById(id);
		if (P == null){
			return notFound(); // 404
		}
		else {
			result.put("Person", Json.toJson(P));
			return ok(result);
	
		}
	}
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePerson(Long id){
		ObjectMapper mapper = new ObjectMapper();
		try {

			 JsonNode json = request().body().asJson();
			 Person updPerson = mapper.readValue(json.toString(), Person.class);
			 PersonList theList = PersonList.getInstance(); 
			 updPerson = theList.updatePerson(updPerson);
			 if (updPerson == null){
				return notFound(); // 404 
			 }
			 else {
				 ObjectNode result = Json.newObject();
				 result.put("Person", Json.toJson(updPerson));
				 return ok(result);
			 }
		}
		catch(Exception e){
			 e.printStackTrace();
			 return badRequest("Missing information");			
		}
	}
	public static Result deletePerson(Long id){
		PersonList theList = PersonList.getInstance();
		boolean erased = theList.deletePerson(id);
		if (erased){
			// This is code 204 - OK with no content to return
			return noContent();
		}
		else {
			return notFound("Person not found");
		}

	}
}
