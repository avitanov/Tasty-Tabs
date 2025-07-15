package finki.db.tasty_tabs.entity.exceptions;

public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(String name) {
        super(String.format("Category: %s doesnt exist",name));
    }
    public CategoryNotFoundException() {
        super("Category doesnt exist");
    }
}
