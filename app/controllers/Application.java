package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Convertor;
import models.CurrencyMap;
import models.User;
import play.mvc.Security;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static models.CurrencyMap.*;


public class Application extends Controller {

    public static Result GO_HOME = redirect(
            routes.Application.list(0, "name", "asc", "")
    );

    public static Result index() {
        return GO_HOME;
    }


    @Transactional(readOnly=true)
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(
                list.render(
                        page(page, 10, sortBy, order, filter),
                        sortBy, order, filter
                )
        );
    }

    @Transactional(readOnly=true)
    @Security.Authenticated(Secured.class)
    public static Result edit(Long id) {
        Form<CurrencyMap> currencyForm = Form.form(CurrencyMap.class).fill(
                findById(id)
        );
        return ok(
                editForm.render(id, currencyForm)
        );
    }

    @Transactional(readOnly=true)
    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<CurrencyMap> currencyForm = Form.form(CurrencyMap.class);
        return ok(
                createForm.render(currencyForm)
        );
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result update(Long id) {
        DynamicForm requestData = Form.form().bindFromRequest();
        String valuation = requestData.get("valuation");
        Double parsedValuation = Double.parseDouble(valuation);
        CurrencyMap currency = findById(id);
        currency.valuation = parsedValuation;
        currency.update(id);
        flash("success", "Currency " + currency.name + " has been updated");
        return GO_HOME;
    }
    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result save() {

      Form<CurrencyMap> currencyForm = Form.form(CurrencyMap.class).bindFromRequest();
        if(currencyForm.hasErrors()) {
            return badRequest(createForm.render(currencyForm));
        }
        currencyForm.get().save();
        flash("success", "Currency " + currencyForm.get().name + " has been created");
        return GO_HOME;
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result delete(Long id) {
        findById(id).delete();
        flash("success", "Currency has been deleted");
        return GO_HOME;
    }


    @Transactional(readOnly=true)
    public static Result currencyExchange() {
        Form<Convertor> convertor = Form.form(Convertor.class);

        return ok(
                currencyConvertor.render(convertor)
        );
    }

    @Transactional(readOnly=true)
    public static Result currencyConvertor() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String valuation = requestData.get("valuation");
        String from = requestData.get("from");
        String to = requestData.get("to");

        long fromCurrency = Long.parseLong(from);
        long toCurrency = Long.parseLong(to);
        double amountValuation = Double.parseDouble(valuation);

        CurrencyMap fromCurrencyMap = findById(fromCurrency);
        CurrencyMap toCurrencyMap = findById(toCurrency);

        double convertedValue;
        convertedValue = getConvertedValue(valuation, from, to);
        flash("success", + amountValuation + " " + fromCurrencyMap.currencyCode + " is equivalent to "
                +  convertedValue + " to " + toCurrencyMap.currencyCode + " ! ");

        return GO_HOME;
    }

    @Transactional
    @BodyParser.Of(Json.class)
    public static Result currencyConvertorAjax() {

        final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String[] value = entry.getValue();
            map.put(key,value[0]);
        }
        String valuation = map.get("valuation");
        String from = map.get("from");
        String to = map.get("to");

        long fromCurrency = Long.parseLong(from);
        long toCurrency = Long.parseLong(to);
        double amountValuation = Double.parseDouble(valuation);

        CurrencyMap fromCurrencyMap = findById(fromCurrency);
        CurrencyMap toCurrencyMap = findById(toCurrency);

        double convertedValue = 0.0;
        String msg = "";
        try {
            convertedValue = getConvertedValue(valuation, from, to);
        }catch(Exception ex){
            msg = "Exception in converting currency";
        }
        flash("success", + amountValuation + " " + fromCurrencyMap.currencyCode + " is equivalent to "
                +  convertedValue + " to " + toCurrencyMap.currencyCode + " ! ");

        ObjectNode result = play.libs.Json.newObject();
        if(convertedValue == 0.0) {
            result.put("status", "OK");
            result.put("message", msg);
            return badRequest(result);
        } else {
            result.put("status", "OK");
            result.put("amount", convertedValue );
            return ok(result);
        }
    }


    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class);
        return ok(
                login.render(loginForm)
        );
    }

    @Transactional
    public static Result authenticate() {
        Form<Login> loginForm;
        loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                    routes.Application.index()
            );
        }
    }

    public static Result isLoggedIn() {
        ObjectNode result = play.libs.Json.newObject();
        String email = session().get("email");
        if( email != null){
            result.put("status", "OK");
            result.put("email", email );
            return ok(result);
        } else {
            result.put("status", "OK");
            result.put("msg", "User not logged In" );
            return badRequest(result);
        }
    }


    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.login()
        );
    }


}
