package step.learning.ioc;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/").with( HomeServlet.class );
        serve("/signup").with( SignupServlet.class );
        serve("/promotional").with( PromotionalServlet.class );
        serve("/cart").with( CartServlet.class );
        serve("/add_product").with( AddProductServlet.class );
    }
}
