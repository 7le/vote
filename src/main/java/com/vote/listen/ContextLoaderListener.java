package com.vote.listen;



import javax.servlet.ServletContextEvent;


public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {




        super.contextDestroyed(event);

        //
    }
}
