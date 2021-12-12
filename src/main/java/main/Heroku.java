package main;

public class Heroku {

  static Integer getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 8080; //return default port if heroku-port isn't set (i.e. on localhost)
  }

}
