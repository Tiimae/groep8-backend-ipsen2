package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.VariableDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Variable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/variable")
public class VariableController {

    private final VariableDAO variableDAO;

    public VariableController(VariableDAO variableDAO) {
        this.variableDAO = variableDAO;
    }

    @RequestMapping(value = "/{variableid}", method = RequestMethod.GET)
    @ResponseBody
    public Optional<Variable> getVariable(@PathVariable String variableid) {
        return this.variableDAO.getVariableFromDatabase(variableid);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Variable> getVariables() {
        return this.variableDAO.getAllVariablesFromDatabase();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public String postVariable(@RequestBody Variable variable) {
        this.variableDAO.saveVariableToDatabase(variable);
        return "Variable has been posted to the database";
    }

    @PutMapping(value = "")
    @ResponseBody
    public String updateVariable(@RequestBody Variable variable) {
        this.variableDAO.updateVariableInDatabase(variable);
        return "Variable has been updated";
    }

    @DeleteMapping(value = "/{variableid}")
    @ResponseBody
    public String deleteVariable(@PathVariable String variableid) {
        this.variableDAO.deleteVariableFromDatabase(variableid);
        return "Variable has been deleted";
    }
}
