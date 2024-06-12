package step.learning.ioc;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter( "/*" ).through( CharsetFilter.class );

        serve("/").with( HomeServlet.class );
        serve("/shop").with( ShopServlet.class );

        serve("/signup").with( SignupServlet.class );
        serve("/auth").with( AuthServlet.class );
        serve("/promotional").with( PromotionalServlet.class );
        serve("/cart").with( CartServlet.class );
        serve("/add_product").with( AddProductServlet.class );

        serve("/shop-api").with( ShopApiServlet.class );

    }
}
