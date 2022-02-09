package drools.demo;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class demo1 {


    public static final void main(final String[] args) throws Exception{
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        execute( kc );
    }

    public static void execute( KieContainer kc ) throws Exception{
        KieSession ksession = kc.newKieSession("point-rulesKS");
        List<Order> orderList = getInitData();
        for (int i = 0; i < orderList.size(); i++) {
            Order o = orderList.get(i);
                System.out.println("=========================");
                System.out.println(o);
            ksession.insert(o);
            ksession.fireAllRules();
                System.out.println("=========================");
                System.out.println(o);
        }
        ksession.dispose();
    }

    private static List<Order> getInitData() {

        ArrayList<Order> orders = new ArrayList<>();
        Random randomAmout = new Random(5);
        Random randomScore = new Random(5);
        for (int i = 0; i < 2 ; i++ ){
            Order build = Order.builder()
                    .amout(randomAmout.nextInt(2000))
                    .score(randomScore.nextInt(5) * 100)
                    .build();
            orders.add(build);
        }
        System.out.println("+++++++++++++++++++++");
        System.out.println(orders);
        return orders;
    }
}
