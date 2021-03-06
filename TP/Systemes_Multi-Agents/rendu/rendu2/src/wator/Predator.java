package wator;

import java.awt.Color;

import core.Agent;
import core.Environment;


/**
 * This is the Predator agent used in the Simulation based on the prey - predator.
 * 
 * This class stores the data needed to be able to run the simulation :
 * the starving value
 * 
 * @author Alexis Linke - Francois Lepan
 *
 */
public class Predator extends Agent {
	
	/** The number of cycle of this predator */
	private int nbCyles;
	
	/** The number of cycle done since this predator ate something */
	private int starveCyle;
	
	/** Number of cycles a predator must exist before reproducing */
	private int breed;
	
	private int starve;
	
	public Predator(Environment env, int breed, int starve) {
		super(env);
		
		this.breed = breed;
		this.starve = starve;
		this.nbCyles = 0;
		this.starveCyle = 0;
		this.color = Color.RED;
		this.type("PREDATOR");
		this.environment.getPlace(this);
		this.currentDirection.getRandomDirection();
	}

	/**
	 * The main method of this class.
	 * 
	 * Decide what to do for this agent.
	 */
	public void doAction() {
		
		if (this.starveCyle == this.starve) { 
			this.starveToDeath();
		} else {
			
			if(this.nbCyles % this.breed == 0) {
				this.reproduct();
			}
			
			this.move();	
		}
		
		this.nbCyles++;
		this.starveCyle++;
	}

	/**
	 * An action of this Agent.
	 * 
	 * Move the agent in the environment.
	 */
	private void move() {
		
		int size = this.environment().getSize();
		
		//int newXPlace = (this.getXPlaceAfterMovement() + size) % size;
		//int newYPlace = (this.getYPlaceAfterMovement() + size) % size;
		
		int newXPlace = this.getXPlaceAfterMovement();
		int newYPlace = this.getXPlaceAfterMovement();

		// encounter a wall
		if (newXPlace >= size || newYPlace >= size || newXPlace < 0 || newYPlace < 0 ) {
			if (newXPlace >= size || newXPlace < 0) {
				this.reverseXDirection();
			}
			
			if (newYPlace >= size || newYPlace < 0) {
				this.reverseYDirection();
			}
			
		} else {
			
			Agent agent = this.environment().getAgentAt(newXPlace, newYPlace);
			
			// the new place is empty
			if (agent == null) {
				
				this.environment.moveAgent(this, newXPlace, newYPlace);
				
			} else { // the new place isn't empty
				
				if(agent.type().equals("PREY")) {
						
					this.starveCyle = 0;
					this.kill((Prey)agent);

					this.environment.moveAgent(this, newXPlace, newYPlace);
					
				} else {
					this.currentDirection.getDifferentRandomDirection();
				}
			}
		}
	}
		
	
	private void kill(Prey prey) { ((Wator)(this.environment)).removePrey(prey);}
	
	private void reproduct() { ((Wator)(this.environment)).addPredator(); }

	private void starveToDeath() { ((Wator)(this.environment)).removePredator(this); }
	
	public String getAge() { return this.nbCyles + ""; }
}
