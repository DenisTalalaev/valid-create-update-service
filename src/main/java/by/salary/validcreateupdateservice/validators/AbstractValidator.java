package by.salary.validcreateupdateservice.validators;

public interface AbstractValidator<RequestType, DataSchema> {
    boolean validate(RequestType request, DataSchema dataSchema);
}
