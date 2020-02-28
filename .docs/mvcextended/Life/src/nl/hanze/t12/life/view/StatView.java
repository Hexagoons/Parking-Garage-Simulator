package nl.hanze.t12.life.view;

import java.awt.*;

import nl.hanze.t12.life.logic.LifeLogic;

public class StatView extends AbstractView {
	private static final long serialVersionUID = -7891669840482084995L;

	public StatView(LifeLogic life) {
		super(life);
		setSize(200,200);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 200);
		int[][] state=life.getState();
		
		if (state==null) return;
		
		float count=0;
		for(int i=0;i<state.length;i++)
			for(int j=0;j<state[i].length;j++)
				if (state[i][j]==1) count++;	
		
		int angle=(int)(360*(count/(state.length*state.length)));
		g.setColor(Color.BLUE);
		g.fillArc(10, 10, 180, 180, 0, angle);
		g.setColor(Color.LIGHT_GRAY);
		g.fillArc(10, 10, 180, 180, angle, 360-angle);
	}
}
