package nl.hanze.t12.life.view;


import java.awt.*;
import nl.hanze.t12.life.logic.*;

public class FieldView extends AbstractView {
	private static final long serialVersionUID = -8200251211832614969L;

	public FieldView(LifeLogic life) {
		super(life);
		setSize(200,200);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 200);
		g.setColor(Color.BLUE);

		int[][] state=life.getState();
		
		if (state==null) return;
		
		int margin=100-state.length*2;
		for(int i=0;i<state.length;i++)
			for(int j=0;j<state[i].length;j++)
				if (state[i][j]==1) g.fillRect(margin+4*i, margin+4*j, 3, 3);	
	}
	
}
