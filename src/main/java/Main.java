import model.Calculator;
import model.Solution;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.Scanner;

import static model.Solution.*;

public class Main {
    public static void main(String[] args) {

        StandardServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

        Metadata metadata =
                new MetadataSources(serviceRegistry)
                        .addAnnotatedClass(Calculator.class)
                        .addAnnotatedClass(Solution.class)
                        .getMetadataBuilder()
                        .build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

//---------------------------------------------------------------------------------------------
        System.out.print("Your expression: ");
//        String expression = "(2+2)*2*-2+42.2";
        String expression = new Scanner(System.in).nextLine();
        List<Solution.Element> elements = textAnalyze(expression);
        Solution.ElementBuffer elementBuffer = new Solution.ElementBuffer(elements);
        double result = expr(elementBuffer);
        int qty = quantityOfNumbers(expression);;
        System.out.println("Quantity of numbers in expression: " + qty);
        System.out.println(result);

//---------------------------------------------------------------------------------------------

        session.save(new Calculator(result, expression,qty));
        session.beginTransaction();

        System.out.print("Enter results of expression less or equal your number: ");
        int expressionEqualThisNumber = new Scanner(System.in).nextInt();

        List<Calculator> allExpressionLessOrEqualThisNumber =
                session.createQuery("select r from Calculator r where r.result <= " +expressionEqualThisNumber,Calculator.class).list();
        for ( var filteredResult:allExpressionLessOrEqualThisNumber)
            System.out.println(filteredResult);

        session.close();
        sessionFactory.close();
    }
}
