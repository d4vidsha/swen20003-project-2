import bagel.*;
import bagel.util.*;

public abstract class Entity extends MovingObject {

    private static final int REFRESH_RATE = 60;
    private static final int ABILITY_ACTIVE_MS = 1000;
    private static final int ABILITY_COOLDOWN_MS = 2000;
    private static final int MS_TO_SEC = 1000;

    protected static final int IDLE = 0;
    protected static final int ATTACK = 1;
    protected static final int INVINCIBLE = 2;

    private static final int IMG_LEFT = 0;
    private static final int IMG_RIGHT = 1;
    private static final int IMG_ABILITY_LEFT = 2;
    private static final int IMG_ABILITY_RIGHT = 3;

    private int frames = 0;
    private boolean onCooldown = false;

    private Image[] images;
    private int health;
    private int maxHealth;
    private int damagePoints;
    private int state;

    /**
     * Constructor for Entity class.
     */
    public Entity(Image[] images, Point position, int speed, int health, int damagePoints) {
        super(images[IMG_LEFT], images[IMG_RIGHT], position, speed);
        this.images = images;
        this.health = health;
        this.maxHealth = health;
        this.damagePoints = damagePoints;
        this.state = IDLE;
    }

    /**
     * Get the entity's health.
     * @return Entity's health as an integer.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the entity's maximum health.
     * @return Entity's maximum health as an integer.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Get the entity's damage points.
     * @return Entity's damage points as an integer.
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Get the entity's state.
     * @return Entity's state as an integer.
     */
    public int getState() {
        return state;
    }   

    /**
     * Set the entity's state.
     * @param state Entity's state as an integer.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Set the entity's health.
     * @param health Entity's health.
     */
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth;
        } else if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    /**
     * Get health percentage of the entity.
     * @return Health percentage of the entity.
     */
    public int getHealthPercentage() {
        return (int) Math.round((double) health / maxHealth * 100);
    }

    /**
     * Check if the entity is dead.
     * @return True if the entity is dead, false otherwise.
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Check state and update image accordingly. Keeps track of time to determine when to switch back to idle state.
     */
    public void checkState() {
        if (state == IDLE) {
            setImages(images[IMG_LEFT], images[IMG_RIGHT]);
        }

        if ((state == ATTACK || state == INVINCIBLE) && !onCooldown) {
            setImages(images[IMG_ABILITY_LEFT], images[IMG_ABILITY_RIGHT]);
            if (frames == REFRESH_RATE * ABILITY_ACTIVE_MS / MS_TO_SEC) {
                state = IDLE;
                frames = 0;
                onCooldown = true;
            }
            frames++;
        }

        if (onCooldown) {
            setState(IDLE);
            if (frames == REFRESH_RATE * ABILITY_COOLDOWN_MS / MS_TO_SEC) {
                onCooldown = false;
                frames = 0;
            }
            frames++;
        }
        
        updateImages();
    }
}
