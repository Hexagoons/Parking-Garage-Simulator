package nl.hanze.t12.mvc.view;

import java.awt.*;
import nl.hanze.t12.mvc.logic.*;

public class PieView extends AbstractView {
	private static final long serialVersionUID = 5455934187803194147L;

	public PieView(Model model) {
		super(model);
		setSize(200, 200);
	}

	public void paintComponent(Graphics g) {
		int aantal=getModel().getAantal();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 200);
		g.setColor(Color.BLUE);
		g.fillArc(10, 10, 180, 180, 0, aantal);
	}	
}
