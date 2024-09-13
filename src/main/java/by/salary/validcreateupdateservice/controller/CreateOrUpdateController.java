package by.salary.validcreateupdateservice.controller;

import by.salary.validcreateupdateservice.model.request.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.response.CreateOrUpdateEntityRelationsResponse;
import by.salary.validcreateupdateservice.service.CreateOrUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CreateOrUpdateController {

    private final CreateOrUpdateService createOrUpdateService;

    @PutMapping("/create-or-update")
    public CreateOrUpdateEntityRelationsResponse createOrUpdate(@RequestBody CreateOrUpdateEntityRelationsRequest request) {
        return createOrUpdateService.createOrUpdate(request);
    }

}
