package com.ensode.jakartaeebook.jsonpstreaming;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Named
@SessionScoped
public class JsonpBean implements Serializable {

  private String jsonStr;

  @Inject
  private Customer customer;

  public String buildJson() {

    StringWriter stringWriter = new StringWriter();
    try (JsonGenerator jsonGenerator = Json.createGenerator(stringWriter)) {
      jsonGenerator.writeStartObject().
              write("firstName", "Larry").
              write("lastName", "Gates").
              write("email", "lgates@example.com").
              writeEnd();
    }
    setJsonStr(stringWriter.toString());

    return "display_json";

  }

  public String parseJson() {

    StringReader stringReader = new StringReader(jsonStr);

    JsonParser jsonParser = Json.createParser(stringReader);

    Map<String, String> keyValueMap = new HashMap<>();
    String key = null;
    String value = null;

    while (jsonParser.hasNext()) {
      JsonParser.Event event = jsonParser.next();

      if (event.equals(Event.KEY_NAME)) {
        key = jsonParser.getString();
      } else if (event.equals(Event.VALUE_STRING)) {
        value = jsonParser.getString();
      }

      keyValueMap.put(key, value);
    }

    customer.setFirstName(keyValueMap.get("firstName"));
    customer.setLastName(keyValueMap.get("lastName"));
    customer.setEmail(keyValueMap.get("email"));

    return "display_parsed_json";
  }

  public String getJsonStr() {
    return jsonStr;
  }

  public void setJsonStr(String jsonStr) {
    this.jsonStr = jsonStr;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

}
