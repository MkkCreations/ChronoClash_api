package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.model.Log;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void createLog(String name, Operation operation, User owner) {
        Log log = new Log();
        log.setName(name);

        if (logRepository.findByName(name).isPresent()) {
            log = logRepository.findByName(name).get();
            List<Operation> operations = log.getOperations();
            operations.add(operation);
            log.setOperations(operations);
        } else {
            log.setOperations(List.of(operation));
        }
        log.setOwner(owner);
        operation.setLog(log);
        logRepository.save(log);
    }

    public void deleteLog(String id) {
        logRepository.deleteById(id);
    }

    public void deleteAllLogs() {
        logRepository.deleteAll();
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}