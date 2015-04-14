package app.controllers;

import app.base.AuthenticatedController;

public class HomeController extends AuthenticatedController {
	public void index() {
		view("token", tokenGenerator.generateToken());
	}
}
