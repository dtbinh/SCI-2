package wator;

import java.awt.Color;

import core.Agent;
import core.Environment;


/**
 * This is the Predator agent used in the Simulation based on the prey - predator.<br>
 * <br>
 * This class stores the data needed to be able to run the simulation :<br>
 * - the maximum starving value,<br>
 * - the current starving value,<br>
 * - the spawn cycle value<br>
 * - and the age of this predator.
 * 
 * @author Alexis Linke - Francois Lepan
 */
public class Predator extends Agent {

	/** The number of cycle this predator has done */
	private int age;

	/** The number of cycle since this predator ate something */
	private int starvingValue;

	/** Number of cycle a predator must exist before reproducing */
	private int spawnCycle;

	/** The number of turn that a shark can live without eating */
	private int maximumStarvingValue;

	/**
	 * The basic Constructor.<br>
	 * - Set the color of the predator to RED,<br>
	 * - set a random direction,<br>
	 * - set its maximum cycle without eating,<br>
	 * - and set his age to 1.
	 * 
	 * @param env the Environment of this Predator
	 * @param spawnCycle The number of turn that is needed before this Predator can spawn a new Predator
	 * @param getAPlace If true get a place on the Environment.
	 */
	public Predator(Environment env, int spawnCycle, int maximumStarvingValue, boolean getAPlace) {
		
		super(env);
		this.spawnCycle = spawnCycle;
		this.maximumStarvingValue = maximumStarvingValue;
		this.starvingValue = 0;
		
		this.age = 1;
		this.color = Color.RED;

		if (getAPlace) this.environment.getPlace(this);
	}

	@Override
	public void doAction() {

		// if this predator starve to death
		if (this.starvingValue > this.maximumStarvingValue) {
			this.killAgent(this);
		} else {
			// if this predator can spawn a new predator 
			if(this.age % this.spawnCycle == 0) {
				 this.spawnNewPredator();
			}

			// move
			this.move();	
		}

		this.age++;
		this.starvingValue++;
	}

	/** 
	 *  Move the predator in the environment.<br> 
	 *  <br>
	 *  If it encounters a prey -> eat it<br>
	 *  else if it encounters another predator get another direction,<br>
	 *  else just move to the new position.
	 */
	private void move() {

		int size = this.environment().getSize();

		// get the new place for a toric environment
		int newXPlace = (this.getXPlaceAfterMovement() + size) % size;
		int newYPlace = (this.getYPlaceAfterMovement() + size) % size;

		Agent agent = this.environment().getAgentAt(newXPlace, newYPlace);

		// the new place is empty
		if (agent == null) {
			this.environment.moveAgent(this, newXPlace, newYPlace);
		}
		// the new place isn't empty
		else { 

			// it's a prey eat it
			if(agent.getClass().equals(Prey.class)) {
				// we remove the agent but 
				this.killAgent(agent);
				this.environment.moveAgent(this, newXPlace, newYPlace);
				this.starvingValue = 0;
			}
			// it's a predator just go another way
			else {
				this.currentDirection.getDifferentRandomDirection();
			}
		}
	}

	/**
	 * Add the agent to the list of agents to remove of the Wator Environment.
	 * 
	 * @param agent the agent to be removed.
	 */
	private void killAgent(Agent agent) {
		((Wator)this.environment).removeAgentFromList(agent);
	}
	
	/** 
	 * Create a new Predator without giving him a new position.
	 * His position will be set at the end of the Agents turn ( Wator.addAgentToList )
	 */
	private void spawnNewPredator() { 
		((Wator)this.environment).addAgentToList(new Predator(this.environment, this.spawnCycle, this.maximumStarvingValue, false));
	}
	
	/**
	 * Get the age of this predator.
	 * 
	 * @return his age.
	 */
	public String getAge() { return this.age + ""; }
}
