package model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "List_of_expessions")
@ToString
public class Calculator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private double result;
    private String expression;
    @Column (name = "numbers_quantity")
    private int qty;

    public Calculator(double result, String expression, int qty) {
        this.result = result;
        this.expression = expression;
        this.qty = qty;
    }
}