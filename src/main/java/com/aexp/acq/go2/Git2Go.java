package com.aexp.acq.go2;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.utils.CommonConstants;

import java.lang.reflect.InvocationTargetException;

public class Git2Go {

  public static void main(String[] args) {
    // App Initialization
    {
      App.init(Git2Go.class.getSimpleName());
      App.instance().loadProperties("config.properties");
      App.instance().loadEnvironmentProperties();
      App.loadDocumentModels("doc_models", "models/");
    }

    // validate the request
    {

    }

    // execute the action
    Object response = executeActions("PurgeStaleBranches");
    System.out.println("Hello and welcome!");

    // return the response and graceful shutdown
    {
      System.out.println(response);

    }
  }

  private static Object executeActions(String action) {
    try {
      // load the action class
      Class<?> clazz = Class.forName(CommonConstants.Actions.getAction(action));

      // create an instance of the class
      BaseComponent baseComponent = (BaseComponent)clazz.getConstructor(String.class).newInstance(action);
      return baseComponent.execute();
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

}
