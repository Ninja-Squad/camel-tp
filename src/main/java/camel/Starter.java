package camel;

import org.apache.camel.main.Main;

public class Starter {

    private Main main;

    public static void main(String[] args) throws Exception {
        Starter example = new Starter();
        example.boot();
    }

    public void boot() throws Exception {
        // create a Main instance
        main = new Main();
        // enable hangup support so you can press ctrl + c to terminate the JVM
        main.enableHangupSupport();

        // add routes
        //main.addRouteBuilder(new MyRouteBuilder());

        // run until you terminate the JVM
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        main.run();
    }

}
