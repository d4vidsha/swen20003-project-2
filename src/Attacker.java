public interface Attacker {
    
    /**
     * Inflict damage to the target.
     * @param target Target to inflict damage to.
     */
    void inflictDamageTo(Targetable target);
    
    /**
     * Attack the target. Can be implemented in different ways depending on the attacker, e.g. by showing an attack 
     * animation or by printing to the console.
     */
    void attack();
}
