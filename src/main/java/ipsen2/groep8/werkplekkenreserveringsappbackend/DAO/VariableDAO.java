package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.VariableRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Variable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VariableDAO {

    private final VariableRepository variableRepository;

    public VariableDAO(VariableRepository variableRepository) {
        this.variableRepository = variableRepository;
    }

    public Optional<Variable> getVariableFromDatabase(String variableid) {
        return this.variableRepository.findById(variableid);
    }

    public List<Variable> getAllVariablesFromDatabase() {
        return this.variableRepository.findAll();
    }

    public void saveVariableToDatabase(Variable variable) {
        this.variableRepository.save(variable);
    }

    public void updateVariableInDatabase(Variable variable) {
        this.variableRepository.save(variable);
    }

    public void deleteVariableFromDatabase(String variableid) {
        this.variableRepository.deleteById(variableid);
    }

}
