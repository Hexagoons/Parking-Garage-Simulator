package nl.hanze.itv1l.parkingsimulation.views;

import nl.hanze.itv1l.parkingsimulation.controllers.Controller;

import javax.swing.*;

/**
 * The view renders the contents of a model. It specifies exactly how the model data should be presented.
 * If the model data changes, the view must update its presentation as needed.
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public abstract class View<T extends Controller> extends JPanel {
    protected T controller;

    public View(T controller) {
        this.controller = controller;
    }

    public void update() {
        repaint();
    }
}
