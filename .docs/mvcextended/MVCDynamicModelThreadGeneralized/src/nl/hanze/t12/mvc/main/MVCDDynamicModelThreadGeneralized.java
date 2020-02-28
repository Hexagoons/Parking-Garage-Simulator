package nl.hanze.t12.mvc.main;

import javax.swing.*;

import nl.hanze.t12.mvc.controller.*;
import nl.hanze.t12.mvc.logic.*;
import nl.hanze.t12.mvc.view.*;

public class MVCDDynamicModelThreadGeneralized {
	private Model model;
	private JFrame screen;
	private AbstractView countview;
	private AbstractView pieview;
	private Controller controller;
	
	public MVCDDynamicModelThreadGeneralized() {
		model=new Model();
		controller=new Controller(model);
		countview=new CountView(model);
		pieview=new PieView(model);
		screen=new JFrame("Model View Controller/Dynamic Model with thread");
		screen.setSize(450, 285);
		screen.setResizable(false);
		screen.setLayout(null);
		screen.getContentPane().add(countview);
		screen.getContentPane().add(pieview);
		screen.getContentPane().add(controller);
		countview.setBounds(10, 10, 200, 200);
		pieview.setBounds(230, 10, 200, 200);
		controller.setBounds(0, 210, 450, 50);
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		screen.setVisible(true);
	}
}
