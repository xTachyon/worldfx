package model.behavior;

import model.action.Action;
import model.action.Actions;
import model.element.Element;

import java.util.*;

public class SimpleActionChooser implements ActionChooser {

	@Override
	public Action chooseNextAction(Element element) {
		double actualLife = element.getWorld().getManager().getLife(element);
		double perc;
		List<Action> possibleActions = new ArrayList<>();
		if (element.getLife() == 0) {
			perc = 100d;
		} else {
			perc = actualLife / element.getLife() * 100;
		}
		if (perc > 100) {
			possibleActions.add(Actions.reproduce_copy());
			possibleActions.add(Actions.reproduce());
			possibleActions.add(Actions.eat());
			possibleActions.add(Actions.move());
		} else if (perc <= 100) {
			possibleActions.add(Actions.eat());
			possibleActions.add(Actions.move());
			possibleActions.add(Actions.reproduce_copy());
		} else if (perc < 5) {
			possibleActions.add(Actions.reproduce_copy());
			possibleActions.add(Actions.eat());
			possibleActions.add(Actions.move());
		} else { // too old just wait there and hope food will come
			possibleActions.add(Actions.eat());
		}

		if (possibleActions.size() > 1 && element.getLastAction() != null) {
			possibleActions.remove(element.getLastAction());
		}

		Set<Action> definedPossibleActions = element.possibleActions();
		possibleActions.removeIf(action -> !definedPossibleActions.contains(action));
		if (possibleActions.isEmpty()) {
			return Actions.pause();
		}

		Collections.shuffle(possibleActions);
		return possibleActions.get(0);
	}

}
