package step.learning.dal.dao;
import step.learning.dal.dto.PromotionalItem;
import java.util.UUID;

public class PromotionalDao {
    public PromotionalItem[] getPromotional() {
        return new PromotionalItem[] {
                new PromotionalItem(UUID.randomUUID(), "Яблука", 30.0, "apple.jpg"),
                new PromotionalItem(UUID.randomUUID(), "Помідори", 60.0, "tomato.jpg"),
                new PromotionalItem(UUID.randomUUID(), "Огірки", 30.0, "cucumber.jpg"),
        };
    }
}
