package jo.amm.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jo.amm.review.model.User;
import jo.amm.review.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("/main")
public class MainController {

    @Autowired
    private UserRepository userRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping(value = {"/test"})
    public String test() {
        return "test";
    }

    @GetMapping(value = {"/", "/{message}"})
    public ResponseEntity<?> testApp(@PathVariable Optional<String> message) {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUsers(@Valid @RequestBody RequestDTO requestDTO) {
        String message = "WOW it's working as will id: " + requestDTO.getId();

        if (requestDTO.getBody() instanceof ArrayList) {
            List<User> users = mapper.convertValue(requestDTO.getBody(), new TypeReference<List<User>>(){});
            userRepository.saveAll(users);
        } else {
            User user = mapper.convertValue(requestDTO.getBody(), User.class);
            userRepository.save(user);
        }

        ResponseDTO responseDTO = new ResponseDTO("0", message);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(value = "/error")
    public ResponseEntity<?> testAppPostError(@Valid @RequestBody RequestDTO requestDTO, Errors errors) {
        String message = "WOW it's working as will id: ";
        if (!StringUtils.isEmpty(requestDTO.getId())) {
            message += requestDTO.getId();
        }
        message += " " + errors.getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}
