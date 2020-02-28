package nl.hanze.itv1l.parkingsimulation.controllers;

import nl.hanze.itv1l.parkingsimulation.models.Model;
import nl.hanze.itv1l.parkingsimulation.views.View;
import java.util.ArrayList;

/**
 * The controller translates the user's interactions with the view into actions that the model will perform.
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public abstract class Controller<T extends Model> {

    /**
     * The model that will be registered to this controller.
     */
    protected T model;

    /**
     * The list of Views that are registered to this controller.
     */
    private ArrayList<View> views;

    /**
     * Registers the provided model and initializes the views list with an empty list.
     *
     * @param model A model that represents data and the rules that govern access to and updates of
     *              the simulation data.
     */
    public Controller(T model) {

        this.model = model;
        views = new ArrayList<>();
    }

    /**
     * Added the provided view to the list of views.
     *
     * @param view A view object to be registered to this controller.
     */
    public void addView(View view){
        if (view == null)
            return;

        views.add(view);
    }

    /**
     * Handles the updating process.
     */
    public void tick() {
        updateViews();
    }

    /**
     * Updates all registered views.
     */
    public void updateViews() {
        for (View view : this.views) {
            view.update();
        }
    }

    /**
     * Returns the registered model.
     *
     * @return The registered model.
     */
    public T getModel() {
        return model;
    }
}
