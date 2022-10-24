package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.VariableDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.VariableDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.VariableMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Variable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/variable")
public class VariableController {

    private final VariableDAO variableDAO;
    private final VariableMapper variableMapper;

    public VariableController(VariableDAO variableDAO, VariableMapper variableMapper) {
        this.variableDAO = variableDAO;
        this.variableMapper = variableMapper;
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
    public String postVariable(@RequestBody @Valid VariableDTO variableDTO) throws EntryNotFoundException {
        this.variableDAO.saveVariableToDatabase(this.variableMapper.toVariable(variableDTO));
        return "Variable has been posted to the database";
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ApiResponse updateVariable(@PathVariable String id, @RequestBody @Valid VariableDTO variableDTO) throws EntryNotFoundException {
        Variable variable = this.variableDAO.getVariableFromDatabase(id).get();
        variable = this.variableMapper.mergeVariable(variable, this.variableMapper.toVariable(variableDTO));
        this.variableDAO.updateVariableInDatabase(variable);
        return new ApiResponse(HttpStatus.ACCEPTED, "Variable has been updated");
    }

    @DeleteMapping(value = "/{variableid}")
    @ResponseBody
    public String deleteVariable(@PathVariable String variableid) {
        this.variableDAO.deleteVariableFromDatabase(variableid);
        return "Variable has been deleted";
    }
}
