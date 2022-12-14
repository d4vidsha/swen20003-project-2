import bagel.*;
import bagel.util.*;

/**
 * Player class that the user controls. The player can move around the map and attack enemies.
 */
public class Player extends Entity implements Attacker {

    // player specific constants
    private static final int MAX_PLAYER_HEALTH = 100;
    private static final int DAMAGE_POINTS = 20;
    private static final int SPEED = 2;
    private static final double GATE_X = 950;
    private static final double GATE_Y = 670;
    private static final String PLAYER_NAME = "Fae";
    private static final Image[] IMAGES = {
        new Image("res/fae/faeLeft.png"),
        new Image("res/fae/faeRight.png"),
        new Image("res/fae/faeAttackLeft.png"),
        new Image("res/fae/faeAttackRight.png")
    };

    // other constants
    private static final String INVALID_STATE = "Invalid state";

    private boolean onCooldown;
    private Timer timer;
    private boolean isTimerSet;

    /**
     * Constructor for Player class.
     * @param imageLeft Image of the player facing left.
     * @param imageRight Image of the player facing right.
     * @param position Position of the player.
     */
    public Player(Point position) {
        super(IMAGES, position, SPEED, MAX_PLAYER_HEALTH, DAMAGE_POINTS, PLAYER_NAME);
        this.onCooldown = false;
        this.isTimerSet = false;
    }

    /**
     * Update the player's position given inputs.
     * @param input Input object to get inputs from.
     */
    public void update(Input input, Boundary boundary) {
        if (input.isDown(Keys.LEFT)) {
            this.moveLeft(boundary);
        } else if (input.isDown(Keys.RIGHT)) {
            this.moveRight(boundary);
        } else if (input.isDown(Keys.UP)) {
            this.moveUp(boundary);
        } else if (input.isDown(Keys.DOWN)) {
            this.moveDown(boundary);
        }
    }

    /**
     * Check if the player has won by being at the gate.
     * @return True if the player has won, false otherwise.
     */
    public boolean isAtGate() {
        Point pos = getPosition();
        return pos.x >= GATE_X && pos.y >= GATE_Y;
    }

    /**
     * Inflict damage to the target.
     * @param target Target to inflict damage to.
     */
    @Override
    public void inflictDamageTo(Entity target) {
        target.takeDamage(DAMAGE_POINTS);
        printDamage(this, target);
    }

    /**
     * Attack the target by showing the attack images.
     */
    @Override
    public void attack() {
        if (!isAttacking() && !onCooldown) {
            setState(ATTACK);
        }
    }

    /**
     * Take damage to the player.
     * @param damage Amount of damage to inflict.
     */
    @Override
    public void takeDamage(int damage) {
        // if the player is invincible, do not take damage, otherwise take damage and become invincible
        if (!isInvincible()) {
            this.setHealth(this.getHealth() - damage);
        }
    }

    /**
     * Check state and update image accordingly. Keeps track of time to determine when to switch back to idle state.
     */
    @Override
    public void checkStates() {
        if (getState() == IDLE) {
        }

        if ((getState() == ATTACK) && !onCooldown) {
            if (timer.isFinished(ShadowDimension.getFrames())) {
                isTimerSet = false;
                setState(IDLE);
                onCooldown = true;
            }
        }

        if (onCooldown) {
            if (getState() != IDLE) {
                setState(IDLE);
            }
            if (timer.isFinished(ShadowDimension.getFrames())) {
                isTimerSet = false;
                onCooldown = false;
            }
        }

        updateImages();
    }

    /**
     * Set the entity's state.
     * @param state Entity's state as an integer.
     */
    @Override
    public void setState(int state) {
        super.setState(state);

        if (!isTimerSet) {
            if (state == ATTACK) {
                timer = new Timer(ShadowDimension.getFrames(), ABILITY_ACTIVE_MS / MS_TO_SEC);
                setImages(IMAGES[IMG_ABILITY_LEFT], IMAGES[IMG_ABILITY_RIGHT]);
            } else if (state == IDLE) {
                timer = new Timer(ShadowDimension.getFrames(), ABILITY_COOLDOWN_MS / MS_TO_SEC);
                setImages(IMAGES[IMG_LEFT], IMAGES[IMG_RIGHT]);
            } else {
                throw new IllegalArgumentException(INVALID_STATE);
            }
            isTimerSet = true;
        }
    }

    /**
     * Check if the player is attacking.
     * @return True if the player is attacking, false otherwise.
     */
    public boolean isAttacking() {
        if (getState() == ATTACK) {
            return true;
        }
        return false;
    }
}
