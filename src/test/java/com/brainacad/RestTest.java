package com.brainacad;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class RestTest {

  private static final String URL = "https://reqres.in/";

  @Test//GET method
  public void checkGetResponseStatusCode1() throws IOException {
    String endpoint = "/api/users";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "page=2");

    //получаем статус код из ответа
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Response Code : " + statusCode);
    Assert.assertEquals("Response status code should be 200", 200, statusCode);
  }


  @Test//GET method body
  public void checkGetResponseBodyNotNull() throws IOException {
    String endpoint = "/api/users";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "?page=2");

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    Assert.assertNotEquals("Body shouldn't be null", null, body);
  }

  @Test//GET method LIST USERS
  public void getListUsers() throws IOException {
    String endpoint = "/api/users";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "page=2");

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    int n = JsonUtils.intFromJSONByPath(body, "$.total");
    Assert.assertEquals("Total number of users = 12", 12, n);
    Assert.assertNotEquals("Body shouldn't be null", null, body);
  }

  @Test//GET method SINGLE USER
  public void getSingleUser() throws IOException {
    String endpoint = "/api/users/2";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "");
    System.out.println("response is: " + response);

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    String fName = JsonUtils.stringFromJSONByPath(body, "$.data.first_name");
    String lName = JsonUtils.stringFromJSONByPath(body, "$.data.last_name");
    Assert.assertNotEquals("Body shouldn't be null", null, body);
    Assert.assertEquals("First name of user - 'Janet'", "Janet", fName);
    Assert.assertEquals("Last name of user - 'Weaver'", "Weaver", lName);
  }

  @Test//GET DELAYED RESPONSE
  public void getDelayedresponse() throws IOException {
    String endpoint = "/api/users?delay=10";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "page=2");

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    int code = response.getStatusLine().getStatusCode();
    Assert.assertEquals("Status code = 200", 200, code);
    Assert.assertNotEquals("Body shouldn't be null", null, body);
  }

  @Test//GET LIST
  public void getListByIdresponse() throws IOException {
    String endpoint = "/api/unknown";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "");

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);

    List idList = JsonUtils.listFromJSONByPath(body, "$..id");
    List namesList = JsonUtils.listFromJSONByPath(body, "$..name");
    System.out.println(body);

    System.out.println("Size of name: " + idList.size());
    System.out.println(idList.get(0));
    int code = response.getStatusLine().getStatusCode();
    Assert.assertEquals("Status code = 200", 200, code);
    Assert.assertEquals("First element id = 1", 1, idList.get(0));
    Assert.assertEquals("Second name = 'fuchsia rose'", "fuchsia rose", namesList.get(1));
  }

  @Test//GET LIST2
  public void getListSearchIdByYear() throws IOException {
    String endpoint = "/api/unknown";

    //Выполняем REST GET запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.get(URL + endpoint, "");

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);

    //The result is a list, because filters (@) always return lists
    List id = JsonUtils.listFromJSONByPath(body, "$.data[?(@.year==2002)].id");

    System.out.println(body);

    int code = response.getStatusLine().getStatusCode();
    Assert.assertEquals("Status code = 200", 200, code);
    Assert.assertEquals("Id of item with year 2002 = 3", 3, id.get(0));
  }

  @Test//DELETE method
  //TODO: clarify what to put in headers
  public void deleteMethod() throws IOException {
    String endpoint = "/api/users/444";

    HttpResponse response = HttpClientHelper.delete(URL + endpoint, "");
    System.out.println("response is: " + response);
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Response Code : " + statusCode);
    Assert.assertEquals("Response status code should be 204", 204, statusCode);
  }

  @Test//POST method CREATE
  public void checkPostResponseStatusCode() throws IOException {
    String endpoint = "/api/users";

    //создаём тело запроса
    String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

    //получаем статус код из ответа
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Response Code : " + statusCode);
    String body = HttpClientHelper.getBodyFromResponse(response);
    Assert.assertEquals("Response status code should be 201", 201, statusCode);
  }

  @Test//POST метод body not null
  public void checkPostResponseBodyNotNull() throws IOException {
    String endpoint = "/api/users";

    //создаём тело запроса
    String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    Assert.assertNotEquals("Body shouldn't be null", null, body);
  }

  @Test//POST method create user
  public void checkPostCreateUser() throws IOException {
    String endpoint = "/api/users";

    //создаём тело запроса
    String requestBody = "{\"name\": \"den\",\"job\": \"tester\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println(body);
    String name = JsonUtils.stringFromJSONByPath(body, "$.name");
    Assert.assertEquals("Response status code should be 201", 201, statusCode);
    Assert.assertEquals("Name of user - 'den'", "den", name);
  }

  @Test//POST method REGISTER - SUCCESSFUL
  public void checkPostRegisterSuccess() throws IOException {
    String endpoint = "/api/register";

    //создаём тело запроса
    String requestBody = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

    //Конвертируем входящий поток тела ответа в строку
    String body = HttpClientHelper.getBodyFromResponse(response);
    System.out.println(body);
    int code = response.getStatusLine().getStatusCode();
    Assert.assertEquals("Status code = 200", 200, code);
    Assert.assertNotEquals("Body shouldn't be null", null, body);
  }

  @Test//PUT method UPDATE
  public void checkPutUpdateUser() throws IOException {
    String endpoint = "/api/users";

    //создаём тело запроса
    String requestBody = "{\"name\": \"updated_user\",\"job\": \"updated_job\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.put(URL + endpoint, requestBody);
    String body = HttpClientHelper.getBodyFromResponse(response);
    int statusCode = response.getStatusLine().getStatusCode();
    String name = JsonUtils.stringFromJSONByPath(body, "$.name");
    Assert.assertEquals("Response status code should be 200", 200, statusCode);
    Assert.assertEquals("Name of new user - 'updated_user'", "updated_user", name);
  }

  @Test//PATCH method UPDATE
  public void checkPatchUpdateUser() throws IOException {
    String endpoint = "/api/users/2";

    //создаём тело запроса
    String requestBody = "{\"name\": \"morpheus\",\"job\": \"updated_job\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.patch(URL + endpoint, requestBody);
    String body = HttpClientHelper.getBodyFromResponse(response);
    int statusCode = response.getStatusLine().getStatusCode();
    String job = JsonUtils.stringFromJSONByPath(body, "$.job");
    Assert.assertEquals("Response status code should be 200", 200, statusCode);
    Assert.assertEquals("Name of new user - 'morpheus'", "updated_job", job);
  }

  @Test//PATCH method UPDATE
  public void checkPatchUpdateUserId_8() throws IOException {
    String endpoint = "/api/users/8";

    //создаём тело запроса
    String requestBody = "{\"id\": \"8\",\"email\": \"updated_email@email.com\"}";

    //Выполняем REST POST запрос с нашими параметрами
    // и сохраняем результат в переменную response.
    HttpResponse response = HttpClientHelper.patch(URL + endpoint, requestBody);
    String body = HttpClientHelper.getBodyFromResponse(response);
    int statusCode = response.getStatusLine().getStatusCode();
    String email = JsonUtils.stringFromJSONByPath(body, "$.email");
    Assert.assertEquals("Response status code should be 200", 200, statusCode);
    Assert.assertEquals("Name of new user - 'morpheus'", "updated_email@email.com", email);
  }

}
