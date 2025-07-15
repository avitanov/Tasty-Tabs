package finki.db.tasty_tabs.entity.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String name) {
        super(String.format("Category: %s doesnt exist",name));
    }
    public CategoryNotFoundException() {
        super(String.format("Category doesnt exist"));
    }
}
