package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your REST app for ICOM 4035 project 2 is ready."));
    }

}
