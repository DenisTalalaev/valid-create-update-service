package by.salary.validcreateupdateservice.controller;

import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsRequest;
import by.salary.validcreateupdateservice.model.CreateOrUpdateEntityRelationsResponse;
import by.salary.validcreateupdateservice.service.CreateOrUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
