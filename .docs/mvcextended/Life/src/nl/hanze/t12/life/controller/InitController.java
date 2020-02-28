package nl.hanze.t12.life.controller;

import java.awt.event.*;
import javax.swing.*;

import nl.hanze.t12.life.logic.*;

public class InitController extends AbstractController implements ActionListener {
	private static final long serialVersionUID = 8084081366423909672L;
	private JTextField size;
	private JTextField degree;
	private JButton init;
	
	
	public InitController(LifeLogic life) {
		super(life);
		setSize(90, 130);
		size=new JTextField();
		degree=new JTextField();
		init=new JButton("Init");
		init.addActionListener(this);
		
		this.setLayout(null);
		add(size);
		add(degree);
		add(init);
		size.setBounds(10, 10, 70, 30);
		degree.setBounds(10, 50, 70, 30);
		init.setBounds(10, 90, 70, 30);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			int sizeField=parseSize();
			float degreeField=parseDegree();
			life.setSize(sizeField);
			life.setDegree(degreeField);
			life.randomInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private int parseSize() throws NumberFormatException {
		return Integer.parseInt(size.getText());
	}
	
	private float parseDegree() throws NumberFormatException {
		return Float.parseFloat(degree.getText());
	}
	

}
